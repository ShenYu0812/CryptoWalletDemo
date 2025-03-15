package com.shenyu.server.netty.plugins

import com.shenyu.server.netty.verifyKeyStore
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.extensions.HashAlgorithm
import io.ktor.network.tls.extensions.SignatureAlgorithm
import io.ktor.server.engine.*
import io.ktor.server.netty.NettyApplicationEngine
import org.slf4j.LoggerFactory
import javax.security.auth.x500.X500Principal


private val logger = LoggerFactory.getLogger("SSL_Config")

private const val aliasName = "MyDemo"
private const val certificatePassword = "UmN68whaFcKH"
private const val keyStorePassword = "UmN68whaFcKH"

fun NettyApplicationEngine.Configuration.envConfig() {
    val keyStore = buildKeyStore {
        certificate(aliasName) {
            hash = HashAlgorithm.SHA256
            sign = SignatureAlgorithm.RSA
            daysValid = 825
            keySizeInBits = 2048
            password = certificatePassword
            domains = listOf("127.0.0.1")
            subject = X500Principal("CN=localhost, OU=ktor, O=Foris, C=US, emailAddress=shenyu2it@gmail.com")
        }
    }
    verifyKeyStore(keyStore, aliasName)

    connector {
        port = 8081
    }

    sslConnector(
        keyStore = keyStore,
        keyAlias = aliasName,
        keyStorePassword = { keyStorePassword.toCharArray() },
        privateKeyPassword = { certificatePassword.toCharArray() }
    ) {
        port = 8443
    }

}
