package com.shenyu.server.netty.plugins

import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.ServerReady
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("server_lifecycle")

class LifecycleObserver<TEngine : ApplicationEngine,
        TConfiguration : ApplicationEngine.Configuration>(
    private val server: EmbeddedServer<TEngine, TConfiguration>,
    private var listener: ServerLifecycleListener? = null
) {

    fun setupLifecycleHooks() {
        with(server.monitor) {
            subscribe(ApplicationStarting) {
                logger.info("服务器正在启动...")
            }

            subscribe(ApplicationStarted) {
                logger.info("服务器已启动完成")
                // 服务器启动成功后的操作
                listener?.onServerStarted()
            }

            subscribe(ServerReady) {
                logger.info("服务器已就绪，可以接受请求")
                val ports = server.engineConfig.connectors.map { it.port }
                logger.info("监听端口: $ports")
                listener?.onServerReady(ports)
            }

            subscribe(ApplicationStopping) {
                logger.info("服务器正在停止...")
                // 清理资源
                listener?.cleanup()
            }

            subscribe(ApplicationStopped) {
                logger.info("服务器已停止")
                listener = null
            }
        }
    }

}


interface ServerLifecycleListener {
    fun onServerStarted()
    fun onServerReady(ports: List<Int>)
    fun cleanup()
}