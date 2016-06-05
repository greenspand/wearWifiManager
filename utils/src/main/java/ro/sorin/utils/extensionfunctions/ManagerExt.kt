package ro.sorin.utils.extensionfunctions

import android.net.wifi.WifiConfiguration

/**
 * Created by sorin on 4/17/16.
 */


inline fun wifiConfiguration( func : WifiConfiguration.() -> Unit): WifiConfiguration{
    val config = WifiConfiguration()
    config.func()
    return config.apply(func)
}

