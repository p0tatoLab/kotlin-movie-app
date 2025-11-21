package com.example.movieapp.util

import kotlinx.coroutines.delay
import kotlin.math.pow

/**
 * リトライポリシー
 */
object RetryPolicy {

    /**
     * 指数バックオフでリトライ
     */
    suspend fun <T> retryWithExponentialBackoff(
        maxRetries: Int = 3,
        initialDelayMillis: Long = 1000,
        maxDelayMillis: Long = 10000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        var lastException: Exception? = null

        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e

                // 最後の試行でない場合は待機
                if (attempt < maxRetries - 1) {
                    delay(currentDelay)
                    currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
                }
            }
        }

        // すべてのリトライが失敗した場合
        throw lastException ?: Exception("リトライに失敗しました")
    }
}