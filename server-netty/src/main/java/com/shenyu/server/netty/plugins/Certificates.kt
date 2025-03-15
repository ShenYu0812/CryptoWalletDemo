package com.shenyu.server.netty.plugins

import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.connector
import io.ktor.server.engine.sslConnector
import java.io.InputStream
import java.security.KeyStore


private const val aliasName = "ktor_certificates"
private const val certificatePassword = "123456789"
private const val keyStorePassword = "123456789"

fun ApplicationEngine.Configuration.envConfig() {
    var keyStore: KeyStore? = null
    var jksInputStream: InputStream? = null
    try {
        jksInputStream = javaClass.getResourceAsStream("ssl_certificate_test.jks")
        jksInputStream?.run inputStream@{
            keyStore = KeyStore.getInstance("JKS").runCatching ks@{
                this@ks.load(this@inputStream, keyStorePassword.toCharArray())
                this@ks
            }.getOrNull()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        jksInputStream?.close()
    }

    if (keyStore == null) {
        keyStore = buildKeyStore {
            certificate(aliasName) {
                password = certificatePassword
                domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
            }
        }.apply ks@{
//            this@ks.saveToFile(keyStoreFile, keyStorePassword)
        }
    }

    connector {
        port = 8081
    }

    sslConnector(
        keyStore = keyStore!!,
        keyAlias = aliasName,
        keyStorePassword = { keyStorePassword.toCharArray() },
        privateKeyPassword = { certificatePassword.toCharArray() }) {
        port = 8443
        //keyStorePath = keyStoreFile
    }
}