package com.mionix.baseapp.repo

import com.mionix.baseapp.api.Api
import com.mionix.baseapp.base.Response
import com.mionix.baseapp.base.ResponseError
import com.mionix.baseapp.model.LovePercentModel

class LovePercentRepo(private val api: Api) {
    suspend fun getPercentage(fname:String,sname:String) : Response<LovePercentModel> {
        return try {
            Response.success(api.getPercentage(fname,sname))
        }catch (e: Exception){
            Response.error(ResponseError(101,e.message.toString()))
        }
    }
}