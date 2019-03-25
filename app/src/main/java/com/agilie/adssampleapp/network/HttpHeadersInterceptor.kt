package com.agilie.adssampleapp.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpHeadersInterceptor: Interceptor {
    private val headerContentType = "Content-Type"
    private val headerAccept = "Content-Type"
    private val contentType = "application/json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val sourceRequest = chain.request()
        val builder = sourceRequest.newBuilder()
            .header(headerContentType, contentType)
            .addHeader(headerAccept, contentType)
            .method(sourceRequest.method(), sourceRequest.body())
        return chain.proceed(builder.build())
    }
}