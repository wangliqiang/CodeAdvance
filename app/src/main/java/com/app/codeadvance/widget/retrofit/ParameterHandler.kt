package com.app.codeadvance.widget.retrofit

abstract class ParameterHandler {
    abstract fun apply(serviceMethod: ServiceMethod, value: String?)
    internal class QueryParameterHandler(var key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String?) {
            serviceMethod.addQueryParameter(key, value!!)
        }
    }

    internal class FieldParameterHandler(var key: String) : ParameterHandler() {
        override fun apply(serviceMethod: ServiceMethod, value: String?) {
            serviceMethod.addFieldParameter(key, value!!)
        }
    }
}
