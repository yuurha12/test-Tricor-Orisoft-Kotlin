package com.example.mydiary.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiaryApiClient {
    private const val BASE_URL = "https://diary-test.ifdenewhallaid.com/"

    private val client: OkHttpClient by lazy { OkHttpClient.Builder().build() }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: DiaryApi by lazy { retrofit.create(DiaryApi::class.java) }
}

