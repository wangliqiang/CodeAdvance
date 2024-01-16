package com.app.codeadvance.widget.retrofit

import com.app.codeadvance.widget.retrofit.ParameterHandler.FieldParameterHandler
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import java.lang.reflect.Method

class ServiceMethod(builder: Builder) {
    private val callFactory: Call.Factory
    private val relativeUrl: String?
    private val hasBody: Boolean
    private val parameterHandler: Array<ParameterHandler?>
    private var formBuild: FormBody.Builder? = null
    private var baseUrl: HttpUrl
    private var httpMethod: String
    private var urlBuilder: HttpUrl.Builder? = null

    init {
        baseUrl = builder.retrofit.baseUrl
        callFactory = builder.retrofit.callFactory
        httpMethod = builder.httpMethod
        relativeUrl = builder.relativeUrl
        hasBody = builder.hasBody
        parameterHandler = builder.parameterHandler
        //如果是有请求体,创建一个okhttp的请求体对象
        if (hasBody) {
            formBuild = FormBody.Builder()
        }
    }

    operator fun invoke(args: Array<Any>): Any {
        // 处理请求的地址和参数
        for (i in parameterHandler.indices) {
            val handlers = parameterHandler[i]
            // handler内本来就记录了key，现在给到对应的value
            handlers!!.apply(this, args[i].toString())
        }
        val url: HttpUrl
        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl!!)
        }
        url = urlBuilder!!.build()
        // 请求体
        var formBody: FormBody? = null
        if (formBody != null) {
            formBody = formBuild!!.build()
        }
        val request: Request = Request.Builder().url(url).method(httpMethod, formBody).build()
        return callFactory.newCall(request)
    }

    // get请求,把 k-v 拼到 url 里面
    fun addQueryParameter(key: String, value: String) {
        if (urlBuilder == null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl!!)
        }
        urlBuilder?.addQueryParameter(key, value)
    }

    // Post,把 k-v 放到 请求体中
    fun addFieldParameter(key: String, value: String) {
        formBuild?.add(key, value)
    }

    class Builder(val retrofit: Retrofit, method: Method) {
        private val methodAnnotations: Array<Annotation>
        private val parameterAnnotations: Array<Array<Annotation>>
        lateinit var parameterHandler: Array<ParameterHandler?>
        lateinit var httpMethod: String
        lateinit var relativeUrl: String
        var hasBody = false

        init {
            // 获取方法上的所有注解
            methodAnnotations = method.annotations
            // 获取方法参数的所有注解 （一个参数可以有多个注解，一个方法又会有多个参数）
            parameterAnnotations = method.parameterAnnotations
        }

        fun build(): ServiceMethod {

            // 解析方法上的注解，只处理POST和GET
            for (methodAnnotation in methodAnnotations) {
                if (methodAnnotation is POST) {
                    // 记录当前请求方式
                    httpMethod = "POST"
                    // 记录请求url的path
                    relativeUrl = methodAnnotation.value
                    // 是否有请求体
                    hasBody = true
                } else if (methodAnnotation is GET) {
                    httpMethod = "GET"
                    relativeUrl = methodAnnotation.value
                    hasBody = false
                }
            }

            // 解析方法参数上的注解
            val length = parameterAnnotations.size
            parameterHandler = arrayOfNulls(length)
            for (i in 0 until length) {
                // 一个参数上的所有注解
                val annotations = parameterAnnotations[i]
                // 处理参数上的每一个注解
                for (annotation in annotations) {
                    if (annotation is Field) {
                        val value = annotation.value
                        parameterHandler[i] = FieldParameterHandler(value)
                    } else if (annotation is Query) {
                        val value = annotation.value
                        parameterHandler[i] = FieldParameterHandler(value)
                    }
                }
            }
            return ServiceMethod(this)
        }
    }
}
