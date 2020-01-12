package com.sargis.guardiannews.guadriandsapi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GuardiansService {
    @GET("search?q")
    fun getArticles(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("page-size") pageSize: Int,
        @Query("format") format: String = "json",
        @Query("show-fields") showFields: String = "starRating,headline,thumbnail,short-url",
        @Query("api-key") apiKey: String = API_KEY
    ): Call<MainResponse>

    companion object {
        const val API_KEY = "6d229758-72b5-4a21-9c6f-995993220866"

        private var API_BASE_URL = "https://content.guardianapis.com/"

        private var httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        private var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        private var retrofit: Retrofit = builder
            .client(httpClient.build())
            .build()

        fun getApi(): GuardiansService {
            return retrofit.create(GuardiansService::class.java)
        }
    }
}
