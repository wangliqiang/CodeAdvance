package com.app.codeadvance.widget.retrofit

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER


@Target(FUNCTION)
@Retention(RUNTIME)
annotation class POST(val value: String = "")

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class GET(val value: String = "")

@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class Query(val value: String)


@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class Field(val value: String)

