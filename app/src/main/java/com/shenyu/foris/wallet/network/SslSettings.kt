package com.shenyu.foris.wallet.network

import android.util.Log
import com.blankj.utilcode.util.Utils
import com.shenyu.foris.wallet.R
import io.ktor.network.tls.certificates.KeyType
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.extensions.HashAlgorithm
import io.ktor.network.tls.extensions.SignatureAlgorithm
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import javax.security.auth.x500.X500Principal


private const val aliasName = "MyDemo"
private const val kPassword = "UmN68whaFcKH"

object SslSettings {
    // 获取客户端信任证书库
    private fun getClientTrustStore(): KeyStore {
        val context = Utils.getApp().applicationContext
        val keyStore = KeyStore.getInstance("BKS")
        context.resources.openRawResource(R.raw.keystore).use { inputStream ->
            keyStore.load(inputStream, kPassword.toCharArray())
        }
        return keyStore
    }

    private fun buildClientKeyStore(): KeyStore {
        return buildKeyStore {
            certificate(aliasName) builder@{
                hash = HashAlgorithm.SHA256
                sign = SignatureAlgorithm.RSA
                daysValid = 825
                keySizeInBits = 2048
                password = kPassword
                keyType = KeyType.Client
                domains = listOf("127.0.0.1")
                subject = X500Principal("CN=localhost, OU=ktor, O=Foris, C=US, emailAddress=shenyu2it@gmail.com")
            }
        }
    }

    // 创建信任管理器
    fun createTrustManager(): X509TrustManager {
        val trustStore = buildClientKeyStore()
        logCertificateChain(trustStore)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(trustStore)
        return tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
    }

    // 创建SSL上下文
    fun createSSLContext(trustManager: X509TrustManager): SSLContext {
        return SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), SecureRandom())
        }
    }

    fun logCertificateChain(keyStore: KeyStore) {
        val aliases = keyStore.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            val certificate = keyStore.getCertificate(alias) as? X509Certificate
            certificate?.let {
                Log.d("SSL_Config", """
                证书信息 - $alias:
                主题: ${it.subjectDN}
                颁发者: ${it.issuerDN}
                序列号: ${it.serialNumber}
                有效期: ${it.notBefore} - ${it.notAfter}
                签名算法: ${it.sigAlgName}
                类型: ${if (it.basicConstraints != -1) "CA" else "前端"}
            """.trimIndent())
            }
        }
    }
}


@Suppress("Unused", "CustomX509TrustManager", "TrustAllX509TrustManager")
class FakeTrustManager: X509TrustManager {

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }

}
