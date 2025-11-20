package com.example.movieapp.util

/**
 * ネットワークエラーの種類を表現
 */
sealed class NetworkException(message: String) : Exception(message) {
    class NetworkError(message: String = "ネットワークエラーが発生しました") : NetworkException(message)
    class ServerError(message: String = "サーバーエラーが発生しました") : NetworkException(message)
    class UnknownError(message: String = "不明なエラーが発生しました") : NetworkException(message)
    class NoConnectionError(message: String = "インターネット接続を確認してください") : NetworkException(message)
}