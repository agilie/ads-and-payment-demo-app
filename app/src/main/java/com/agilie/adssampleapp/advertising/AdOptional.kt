package com.agilie.adssampleapp.advertising

class AdOptional<V> private constructor(val value: Any) {

    companion object {

        @JvmStatic
        fun <T: Any> create(value: T): AdOptional<T> {
            return AdOptional(value)
        }

        @JvmStatic
        fun <T> createError(error: Throwable): AdOptional<T> {
            return AdOptional(error)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getValueNullable(): V {
        val result = value as? Throwable ?: return value as V
        return null as V
    }

    fun getError(): Throwable? {
        val error = value
        if (error is Throwable) {
            return error
        }
        return null
    }

    fun isError(): Boolean {
        return value is Throwable
    }
}