package com.shenyu.server.netty.services

import com.google.gson.Gson
import com.shenyu.server.netty.model.dto.LiveRatesDTO
import com.shenyu.server.netty.utils.RatesFluctuationUtil.formatRates
import com.shenyu.server.netty.utils.RatesFluctuationUtil.generateRateFluctuation
import com.shenyu.server.netty.utils.ResourceReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import kotlin.random.Random.Default.nextDouble
import kotlin.time.Duration.Companion.seconds

private val logger = LoggerFactory.getLogger("socket_live_rates")

class LiveRatesService {

    private val gson = Gson()

    private var baseRates: LiveRatesDTO? = ResourceReader.readJsonResource<LiveRatesDTO>(
        path = "data/live_rates.json").getOrNull()

    fun generateLiveRatesFlow(): Flow<String> = flow {
        while (true) {
            val updatedRates = updateRates()
            emit(gson.toJson(updatedRates))

            // random_time = 30 seconds ~ 20 minutes
            // update live rates after random_time
            val delaySeconds = (30 until  1200).random()
            logger.info("update live rates after ${delaySeconds / 60.0} seconds")
            delay(delaySeconds.seconds)
        }
    }

    private fun updateRates(): LiveRatesDTO {
        val currentRates = baseRates?.copy() ?: return LiveRatesDTO(
            ok = false,
            warning = "Base rates not loaded",
            tiers = emptyList()
        )

        val currentTimeStamp = System.currentTimeMillis() / 1000

        currentRates.tiers.forEach { tier ->
            // 更新时间戳
            tier.timeStamp = currentTimeStamp

            // 更新汇率
            tier.rates = tier.rates.map { rateDTO ->
                // 随机决定是否产生剧烈波动（10%的概率）
                val isVolatile = nextDouble() < 0.1

                val currentRate = rateDTO.rate.toDoubleOrNull() ?: return@map rateDTO
                val newRate = generateRateFluctuation(currentRate, isVolatile)

                rateDTO.copy(
                    rate = formatRates(newRate, 12).toString()
                )
            }
        }

        return currentRates
    }

    fun getCurrentRates(): LiveRatesDTO? = baseRates?.copy()
}