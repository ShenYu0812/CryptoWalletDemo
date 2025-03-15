#!/bin/bash

# 1. 创建根证书（Root CA）
echo "====== 创建根证书 ======"
openssl genrsa -out root-ca.key 4096
openssl req -x509 -new -nodes -key root-ca.key -sha256 -days 3650 -out root-ca.pem \
    -subj "/C=US/O=Foris/OU=ktor/CN=Foris Root CA/emailAddress=shenyu2it@gmail.com"

# 2. 创建中间证书（Intermediate CA）
echo "====== 创建中间证书 ======"
openssl genrsa -out intermediate-ca.key 4096
openssl req -new -key intermediate-ca.key -out intermediate-ca.csr \
    -subj "/C=US/O=Foris/OU=ktor/CN=Foris Intermediate CA/emailAddress=shenyu2it@gmail.com"

# 3. 使用根证书签发中间证书
openssl x509 -req -in intermediate-ca.csr -CA root-ca.pem -CAkey root-ca.key \
    -CAcreateserial -out intermediate-ca.pem -days 1825 -sha256 \
    -extfile <(printf "basicConstraints=critical,CA:true,pathlen:0\nkeyUsage=critical,digitalSignature,cRLSign,keyCertSign")

# 4. 创建服务器证书
echo "====== 创建服务器证书 ======"
openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr \
    -subj "/C=US/O=Foris/OU=ktor/CN=localhost/emailAddress=shenyu2it@gmail.com"

# 5. 使用中间证书签发服务器证书
openssl x509 -req -in server.csr -CA intermediate-ca.pem -CAkey intermediate-ca.key \
    -CAcreateserial -out server.pem -days 825 -sha256 \
    -extfile <(printf "subjectAltName=DNS:localhost,IP:127.0.0.1\nkeyUsage=critical,digitalSignature,keyEncipherment\nextendedKeyUsage=serverAuth")

# 6. 创建证书链
echo "====== 创建证书链 ======"
cat server.pem intermediate-ca.pem root-ca.pem > fullchain.pem

# 7. 创建PKCS12格式的keystore
echo "====== PKCS12 ======"
openssl pkcs12 -export \
    -in fullchain.pem \
    -inkey server.key \
    -out keystore.p12 \
    -name "MyDemo" \
    -CAfile root-ca.pem \
    -caname "root" \
    -password pass:UmN68whaFcKH

# 8. 转换为JKS
echo "===== Convert to JKS ====="
keytool -importkeystore \
    -srckeystore keystore.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass UmN68whaFcKH \
    -destkeystore keystore.jks \
    -deststoretype JKS \
    -deststorepass UmN68whaFcKH

# 9. 验证证书信息
echo "====== 验证证书信息 ======"
keytool -list -v -keystore keystore.jks -storepass UmN68whaFcKH

