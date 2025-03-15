package com.shenyu.foris.wallet.network

import android.util.Log
import com.blankj.utilcode.util.Utils
import com.shenyu.foris.wallet.R
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


object SslSettings {

    // 创建信任管理器
    fun createTrustManager(): X509TrustManager {
        val context = Utils.getApp().applicationContext
        val certificates = PemUtils.loadCertificatesFromPem(context, R.raw.root_ca)
        val trustManagers = PemUtils.createTrustManagerFromPem(certificates)
        return trustManagers.first { it is X509TrustManager } as X509TrustManager
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
