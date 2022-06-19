package com.qxf.kotlin.http

class HttpUtils{
    fun get(callback: Callback,url:String ="https://www.baidu.com",cache:Boolean = false){
           callback.onSuccess("成功")
    }

    companion object{
        val baseUrl ="https://www.baidu.com";
        fun sum(vararg  nums:Int): Int{

        }
    }
}