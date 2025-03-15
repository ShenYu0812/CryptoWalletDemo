keytool -importkeystore \
    -srckeystore keystore.jks \
    -srcstoretype JKS \
    -srcstorepass UmN68whaFcKH \
    -destkeystore keystore.bks \
    -deststoretype BKS \
    -deststorepass UmN68whaFcKH \
    -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
    -providerpath ./bcprov-jdk18on-1.77.jar