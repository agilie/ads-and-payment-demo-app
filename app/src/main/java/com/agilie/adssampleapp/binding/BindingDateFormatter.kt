package com.agilie.adssampleapp.binding

import java.text.SimpleDateFormat
import java.util.*

class BindingDateFormatter {
    companion object {
        const val STRIPE_CARD_FORMAT = "dd MMM, HH:mm"
    }

    fun getStripeDate(date: Date): String {
        val formatter = SimpleDateFormat(STRIPE_CARD_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }
}