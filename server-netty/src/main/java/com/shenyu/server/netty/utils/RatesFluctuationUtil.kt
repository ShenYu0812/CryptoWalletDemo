package com.shenyu.server.netty.utils

import kotlin.math.pow
import kotlin.random.Random

@Suppress("Unused")
object RatesFluctuationUtil {
    /**
     * 生成汇率波动
     * @param originalRate 原始汇率
     * @param isVolatile 是否剧烈波动 true:剧烈波动 false:小幅波动
     * @return 波动后的汇率
     */
    fun generateRateFluctuation(originalRate: Double, isVolatile: Boolean): Double {
        require(originalRate > 0) { "汇率必须大于0" }

        return when {
            isVolatile -> generateVolatileRate(originalRate)
            else -> generateSmallFluctuation(originalRate)
        }.coerceAtLeast(0.000001) // 确保价格始终大于0
    }

    /**
     * 生成小幅波动汇率
     * 波动范围：原汇率的±0.1%到±2%之间
     */
    private fun generateSmallFluctuation(rates: Double): Double {
        // 随机生成0.1%到2%之间的波动比例
        val fluctuationPercentage = Random.nextDouble(0.001, 0.02)
        // 随机决定是涨还是跌
        val isIncrease = Random.nextBoolean()

        return if (isIncrease) {
            rates * (1 + fluctuationPercentage)
        } else {
            rates * (1 - fluctuationPercentage)
        }
    }

    /**
     * 生成剧烈波动汇率
     * 波动范围：原汇率的±5%到±30%之间
     */
    private fun generateVolatileRate(rate: Double): Double {
        // 随机生成5%到30%之间的波动比例
        val fluctuationPercentage = Random.nextDouble(0.05, 0.30)
        // 随机决定是涨还是跌
        val isIncrease = Random.nextBoolean()

        return if (isIncrease) {
            rate * (1 + fluctuationPercentage)
        } else {
            rate * (1 - fluctuationPercentage)
        }
    }

    /**
     * 生成一系列连续的汇率波动
     * @param originalRate 原始汇率
     * @param count 需要生成的汇率数量
     * @param volatileProbability 剧烈波动的概率 (0.0-1.0)
     * @return 波动汇率列表
     */
    fun generateRateSequence(
        originalRate: Double,
        count: Int,
        volatileProbability: Double = 0.1
    ): List<Double> {
        require(count > 0) { "数量必须大于0" }
        require(volatileProbability in 0.0..1.0) { "概率必须在0到1之间" }

        val rates = mutableListOf<Double>()
        var currentRates = originalRate

        repeat(count) {
            val isVolatile = Random.nextDouble() < volatileProbability
            currentRates = generateRateFluctuation(currentRates, isVolatile)
            rates.add(currentRates)
        }

        return rates
    }

    /**
     * 生成只涨不跌的小幅波动汇率
     * 波动范围：原价汇率的+0.1%到+1.5%之间
     * @param rate 原始汇率
     * @return 波动后的汇率（只会上涨）
     */
    fun generateSmallIncreaseFluctuation(rate: Double): Double {
        require(rate > 0) { "汇率必须大于0" }

        // 随机生成0.1%到1.5%之间的波动比例
        val increasePercentage = Random.nextDouble(0.001, 0.015)

        // 只增加不减少
        return rate * (1 + increasePercentage)
    }

    /**
     * 生成一系列只涨不跌的连续汇率
     * @param originalRate 原始汇率
     * @param count 需要生成的汇率数量
     * @return 波动汇率列表（汇率只会上涨）
     */
    fun generateIncreasingRateSequence(
        originalRate: Double,
        count: Int
    ): List<Double> {
        require(count > 0) { "数量必须大于0" }

        val rates = mutableListOf<Double>()
        var currentRate = originalRate

        repeat(count) {
            currentRate = generateSmallIncreaseFluctuation(currentRate)
            rates.add(currentRate)
        }

        return rates
    }

    /**
     * 格式化汇率，保留指定位数的小数
     * @param rate 汇率
     * @param decimals 小数位数
     * @return 格式化后的汇率
     */
    fun formatRates(rate: Double, decimals: Int = 6): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return Math.round(rate * factor) / factor
    }
}