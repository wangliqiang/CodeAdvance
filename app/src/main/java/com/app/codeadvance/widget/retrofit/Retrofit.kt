package com.app.codeadvance.widget.retrofit

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

class Retrofit(callFactory: Call.Factory, baseUrl: HttpUrl) {
    // 参考Retrofit的实现，增加一层缓存
    private val serviceMethodCache: MutableMap<Method, ServiceMethod> = ConcurrentHashMap()

    // 使用OkHttp提供的Call.Factory
    var callFactory: Call.Factory

    // 使用OkHttp提供的HttpUrl
    var baseUrl: HttpUrl

    init {
        this.callFactory = callFactory
        this.baseUrl = baseUrl
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader, arrayOf<Class<*>>(service)
        ) { o, method, objects ->
            val serviceMethod = loadServiceMethod(method)
            serviceMethod!!.invoke(objects)
        } as T
    }

    private fun loadServiceMethod(method: Method): ServiceMethod? {
        var result = serviceMethodCache[method]
        if (result != null) {
            return result
        }
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]
            if (result == null) {
                result = ServiceMethod.Builder(this, method).build()
                serviceMethodCache[method] = result!!
            }
        }
        return result
    }

    class Builder {
        private var baseUrl: HttpUrl? = null
        private var callFactory: Call.Factory? = null
        fun callFactory(callFactory: Call.Factory?): Builder {
            this.callFactory = callFactory
            return this
        }

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl.toHttpUrl()
            return this
        }

        fun build(): Retrofit {
            checkNotNull(baseUrl) { "Base URL required." }
            var callFactory: Call.Factory? = callFactory
            if (callFactory == null) {
                callFactory = OkHttpClient()
            }
            return Retrofit(callFactory, baseUrl!!)
        }
    }
}
