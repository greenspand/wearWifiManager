package ro.sorin.utils.extensionfunctions

import android.net.wifi.WifiConfiguration

/**
 * Created by sorin on 4/17/16.
 */


/**
 * Creates and saves a Wifi AP configuration, then starts scanning for same named WIFI Hot Spots and decides to start the created AP or not.
 */
inline fun WifiConfiguration.configureAP(func: WifiConfiguration.() -> Unit): WifiConfiguration {
    val configure = WifiConfiguration()
    configure.func()
    configure.SSID
    configure.preSharedKey
    configure.status = WifiConfiguration.Status.ENABLED
    configure.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
    configure.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
    configure.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
    configure.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
    configure.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
    configure.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
    configure.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
    return configure
}
