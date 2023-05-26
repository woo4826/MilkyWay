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

    private var auto_login:Boolean=true

    @Synchronized
    fun getSharedValue():Boolean{
        return auto_login
    }

    @Synchronized
    fun setSharedValue(value: Boolean) {
        auto_login=value
    }
}