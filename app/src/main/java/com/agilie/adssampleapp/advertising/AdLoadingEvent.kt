package com.agilie.adssampleapp.advertising

data class AdLoadingEvent(
    val event: AdLoadingEventType,
    val errorMessage: String = ""
)

enum class AdLoadingEventType {
    SUCCESS, FAILED
}