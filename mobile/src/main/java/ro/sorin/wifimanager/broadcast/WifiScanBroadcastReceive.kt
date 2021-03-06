package ro.sorin.wifimanager.broadcast

/**
 * Created by sorin on 17.12.15.
 */


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import ro.sorin.utils.RxBus
import ro.sorin.utils.entities.WifiDetails
import ro.sorin.wifimanager.MobileApplication

import rx.Observable
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Broadcast Receiver for all Wifi Networks found after the scan.
 */
class WifiScanBroadcastReceive() : BroadcastReceiver() {

    @Inject
    lateinit var rxBus: RxBus

    init {
        MobileApplication.graph.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        var wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val action = intent.action
        if (action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
            Log.i("RECEIVE", "MSG RECEIVED${wifiManager.scanResults.size}")
            Observable.just(wifiManager.scanResults)
//                    .filter { scanResults -> scanResults != null && scanResults.size > 0 }
//                    .flatMap { scanResults -> Observable.from(scanResults) }
//                    .filter { scanResult -> scanResult.SSID != "" }
                    .subscribeOn(Schedulers.computation())
                    .subscribe { result ->
                        rxBus.send(WifiDetails.getWifiNameAndCapabilities(result))
                    }
        }
    }
}