package ro.sorin.utils.extensionfunctions

import android.net.wifi.WifiConfiguration


inline fun wifiConfiguration( func : WifiConfiguration.() -> Unit): WifiConfiguration{
    val config = WifiConfiguration()
    config.func()
    return config.apply(func)
}