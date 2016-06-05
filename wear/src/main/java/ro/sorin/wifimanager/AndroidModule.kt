package ro.sorin.wifimanager

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.preference.PreferenceManager
import dagger.Module

import javax.inject.Singleton

import dagger.Provides
import ro.sorin.utils.RxBus
import ro.sorin.wifimanager.adapter.WifiAPAdapter
import ro.sorin.wifimanager.AppScope
import javax.inject.Named

/**
 * Created by sorin on 5/29/16.
 */
@Module
class AndroidModule(private val application: Application) {

    /**
     * Allow the application context to be injected but require that it be annotated with [ ][ForApplication] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @AppScope
    fun provideApplicationContext(): Context {
        return application
    }
    @Provides
    @Singleton
    fun provideLocationManager(): LocationManager {
        return application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

   @Provides
   @Singleton
   fun provideRxBus(): RxBus{
       return RxBus
   }

    @Provides
    @Singleton
    fun providesSharedPref(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideWifiManager(): WifiManager {
        return application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    @Provides
    @Singleton
    @Named("wifiConfig")
    fun provideWifiConfig(): WifiConfiguration {
        return WifiConfiguration()
    }
}
