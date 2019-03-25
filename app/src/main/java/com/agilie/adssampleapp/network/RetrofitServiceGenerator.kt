package com.agilie.adssampleapp.network

import com.agilie.adssampleapp.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RetrofitServiceGenerator {

    companion object {
        private val CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors()
        private val THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(CORE_POOL_SIZE)

        private val httpClient: OkHttpClient by lazy {
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            httpClient.connectTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }

            httpClient.addNetworkInterceptor(HttpHeadersInterceptor())

            return@lazy httpClient.build()
        }

        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callbackExecutor(THREAD_POOL_EXECUTOR)
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build()

        private fun createGson(): Gson {
            return GsonBuilder().apply {
                serializeNulls()
                registerTypeAdapter(Date::class.java, DateConverter())
            }
                .create()
        }

        fun <S> createService(clazz: Class<S>): S {
            return retrofit.create(clazz)
        }
    }
}