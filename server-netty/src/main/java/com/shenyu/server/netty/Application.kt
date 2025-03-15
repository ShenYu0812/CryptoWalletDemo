package com.shenyu.server.netty

import com.shenyu.server.netty.plugins.configureRouting
import com.shenyu.server.netty.plugins.configureSockets
import com.shenyu.server.netty.plugins.envConfig
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory
import java.security.KeyStore
import java.security.cert.X509Certificate

private val logger = LoggerFactory.getLogger("ktor.application")

fun main() {
    runCatching {
        embeddedServer(
            factory = Netty,
            environment = applicationEnvironment { log = logger },
            configure = { envConfig() },
            module = Application::module
        ).start(wait = true)
    }.onFailure { e ->
        e.printStackTrace()
    }
}

fun Application.module() {
    configureRouting()
    configureSockets()
}


fun verifyKeyStore(keyStore: KeyStore, alias: String) {
    val chain = keyStore.getCertificateChain(alias)
    require(chain != null && chain.isNotEmpty()) { 
        "证书链不完整: alias=$alias" 
    }
    
    chain.forEachIndexed { index, cert ->
        if (cert is X509Certificate) {
            logger.info("""
                证书信息 - $alias
                证书链 #$index:
                主题: ${cert.subjectDN}
                颁发者: ${cert.issuerDN}
                序列号: ${cert.serialNumber}
                有效期: ${cert.notBefore} - ${cert.notAfter}
                签名算法: ${cert.sigAlgName}
                类型: ${if (cert.basicConstraints != -1) "CA" else "终端"}
            """.trimIndent())

        }
    }
}