package com.shenyu.server.netty.utils

import com.google.gson.Gson
import java.io.InputStreamReader


object ResourceReader {

    val gson = Gson()

    inline fun <reified T> readJsonResource(path: String): Result<T> {
        return runCatching {
            ResourceReader::class.java.classLoader.getResourceAsStream(path)?.use { stream ->
                InputStreamReader(stream).use { reader ->
                    gson.fromJson(reader, T::class.java)
                }
            } ?: throw IllegalStateException("找不到资源文件: $path")
        }.onFailure { e ->
            e.printStackTrace()
        }
    }
}
