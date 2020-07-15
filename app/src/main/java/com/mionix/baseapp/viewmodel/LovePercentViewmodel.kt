package com.mionix.baseapp.viewmodel

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mionix.baseapp.base.Status
import com.mionix.baseapp.model.LovePercentModel
import com.mionix.baseapp.repo.LovePercentRepo
import com.nice.app_ex.base.BaseViewModel
import kotlinx.coroutines.launch

class LovePercentViewmodel(private val lovePercentRepo :LovePercentRepo): BaseViewModel() {
    private val _percentage = MutableLiveData<LovePercentModel>()
    val percentage: LiveData<LovePercentModel>
        get() = _percentage
    fun getPercentage(fname:String,sname:String) {
        viewModelScope.launch {
            val result = lovePercentRepo.getPercentage(fname,sname)
            if (result.status == Status.SUCCESS) {
                // Timber.d("PHT, ${result.data?.data}")
                //mSharePreferences.setMasterInfoResponse(result.data)
                _percentage.value = result.data
            } else {

                Log.d("DUY",result.error.toString())
            }

            Log.d("DUY",result.error.toString())
        }
    }
}