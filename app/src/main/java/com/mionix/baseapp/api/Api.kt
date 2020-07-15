package com.mionix.baseapp.api

import com.mionix.baseapp.model.LovePercentModel
import retrofit2.http.*

interface Api {
//    @POST("appli/Other/MasterInfo")
//    suspend fun getMasterInfoList(): BaseListDataResponse<MutableList<MasterList>>
//
//    @POST("appli/Other/Master")
//    suspend fun getMasterDetail(
//        @Query("master_id") id: Int,
//        @Query("type") type: Int,
//        @Query("if_modified_since") modified: String
//    ): BaseDataResponse<BaseListMasterInfo<MutableList<AppealKey>>>
//
//    @POST("appli/Guidance/Detail")
//    suspend fun getGuidanceEventListDetail(@Query("guidance_cd") guidanceCd: Int): BaseDataResponse<GuidanceEventListDetailModel>
//    @POST("appli/Company/List")
//    suspend fun getCompanyList(): BaseDataResponse<DataCompanyList>
      @GET("getPercentage")
      suspend fun getPercentage(@Query("fname") fname: String,@Query("sname") sname: String): LovePercentModel
}