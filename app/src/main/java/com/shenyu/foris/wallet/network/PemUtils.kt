package com.shenyu.foris.wallet.network

import android.content.Context
import com.shenyu.foris.wallet.network.SslSettings.logCertificateChain
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory


object PemUtils {

    fun loadCertificatesFromPem(context: Context, rawResId: Int): List<X509Certificate> {
        val certificateFactory = CertificateFactory.getInstance("X.509")

        return context.resources.openRawResource(rawResId).use { inputStream ->
            certificateFactory.generateCertificates(inputStream).map {
                it as X509Certificate
            }
        }
    }

    fun createTrustManagerFromPem(certificates: List<X509Certificate>): Array<TrustManager> {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            certificates.forEachIndexed { index, certificate ->
                setCertificateEntry("root-cert-$index", certificate)
            }
        }

        logCertificateChain(keyStore)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        return tmf.trustManagers
    }

}
