package com.shenyu.server.netty.plugins

import io.ktor.server.engine.*
import io.ktor.server.netty.NettyApplicationEngine
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


private val logger = LoggerFactory.getLogger("SSL_Config")

private const val aliasName = "MyDemo"
private const val certificatePassword = "UmN68whaFcKH"
private const val keyStorePassword = "UmN68whaFcKH"

fun NettyApplicationEngine.Configuration.envConfig() {
    val chainStream = Thread.currentThread().contextClassLoader
        .getResourceAsStream("ssl/fullchain.pem")
        ?: throw IllegalStateException("无法找到fullchain.pem")
    val privateKeyStream = Thread.currentThread().contextClassLoader
        .getResourceAsStream("ssl/server.key")
        ?: throw IllegalStateException("无法找到server.key")

    connector {
        port = 8081
    }

    sslConnector(
        keyStore = createKeyStore(chainStream, privateKeyStream),
        keyAlias = aliasName,
        keyStorePassword = { keyStorePassword.toCharArray() },
        privateKeyPassword = { certificatePassword.toCharArray() }
    ) {
        port = 8443
    }

}


fun createKeyStore(
    chainStream: InputStream,
    privateKeyStream: InputStream
): KeyStore {
    try {
        val keyStore = KeyStore.getInstance("PKCS12", "BC").apply {
            load(null, null)
        }

        val certificateFactory = CertificateFactory.getInstance("X.509", "BC")
        val certificates = certificateFactory.generateCertificates(chainStream)
            .map { it as X509Certificate }
            .toTypedArray()

        val privateKey = privateKeyStream.use { stream ->
            val pemParser = PEMParser(InputStreamReader(stream))
            val pemObject = pemParser.readObject()
            val converter = JcaPEMKeyConverter().setProvider("BC")

            when (pemObject) {
                is PEMKeyPair -> {
                    logger.info("读取到PEM密钥对")
                    converter.getKeyPair(pemObject).private
                }
                is PrivateKeyInfo -> {
                    logger.info("读取到私钥信息")
                    converter.getPrivateKey(pemObject)
                }
                else -> {
                    logger.error("不支持的私钥格式: ${pemObject?.javaClass?.name}")
                    throw IllegalStateException("不支持的私钥格式")
                }
            }
        }

        keyStore.setKeyEntry(
            aliasName,
            privateKey,
            keyStorePassword.toCharArray(),
            certificates
        )
        return keyStore
    } catch (e: Exception) {
        logger.error("创建KeyStore失败", e)
        throw e
    }
}


fun generateRSAKeyPair(): KeyPair = runCatching {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    keyPairGenerator.generateKeyPair()
}.onFailure { e ->
    throw RuntimeException(e)
}.getOrThrow()