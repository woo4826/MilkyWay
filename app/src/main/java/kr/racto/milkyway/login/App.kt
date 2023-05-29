package kr.racto.milkyway.login

import android.app.Application

open class App :Application() {

    companion object{
        lateinit var prefs:PreferenceUtil
    }
    override fun onCreate() {
        prefs=PreferenceUtil(applicationContext)
        super.onCreate()
    }

}