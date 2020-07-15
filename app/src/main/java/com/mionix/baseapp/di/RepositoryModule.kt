package com.mionix.baseapp.di



import com.mionix.baseapp.model.local.Preferences
import com.mionix.baseapp.repo.LovePercentRepo
import com.securepreferences.SecurePreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
      single { LovePercentRepo(get()) }
//      single { GuidanceEventListDetailRepo(get()) }
      single { provideAppSharePreferences(get()) }
//      single { CompanyListRepo(get()) }
      single { provideSecurePreferences(androidApplication() as MyApplication)}
}
fun provideSecurePreferences(app: MyApplication): SecurePreferences {
      return SecurePreferences(app, "", "kanzume.xml")
}
fun provideAppSharePreferences(sharedPreferences: SecurePreferences) : Preferences {
      return Preferences(sharedPreferences)
}