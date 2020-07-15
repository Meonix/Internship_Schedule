package com.mionix.baseapp.di

import com.mionix.baseapp.viewmodel.LovePercentViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LovePercentViewmodel(get()) }
//    viewModel { GuidanceEventListViewModel(get()) }
//    viewModel { CompanyListViewModel(get()) }
}