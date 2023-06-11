package kr.racto.milkyway.login

import APIS
import ApiService
import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class App :Application() {

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var apiService: ApiService
    }

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl("https://codeagain.kro.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

}