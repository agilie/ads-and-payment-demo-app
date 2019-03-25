package com.agilie.adssampleapp.network

import android.util.Log
import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateConverter : JsonSerializer<Date>, JsonDeserializer<Date> {

    companion object {
        const val RESPONSE_DATE_FORMAT = "yyyy-MM-dd"
    }

    val responseDateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat(RESPONSE_DATE_FORMAT, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override fun serialize(
        src: Date?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(responseDateFormatter.format(src))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? {
        if (json != null && !json.isJsonNull) {
            val dateStr = json.asString
            try {
                val date = responseDateFormatter.parse(dateStr)
                return date
            } catch (e: Exception) {
                Log.d(DateConverter::class.java.simpleName, "Wrong date format")
                e.printStackTrace()
            }
        }
        return null
    }
}