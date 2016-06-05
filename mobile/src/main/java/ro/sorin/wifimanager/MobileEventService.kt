package ro.sorin.wifimanager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.MessageApi
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.sorin.wifiapmanager.WifiApManager
import ro.sorin.utils.*
import ro.sorin.utils.entities.WearMessage
import ro.sorin.utils.extensionfunctions.*
import ro.sorin.wifimanager.broadcast.WifiScanBroadcastReceive
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by sorin on 17.12.15.
 */
class MobileEventService : Service(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {

    companion object {
        private val TAG = MobileEventService::class.java.simpleName
    }

    private lateinit var wifiScanReceiver: WifiScanBroadcastReceive
    private val binder = LocalBinder()
    private lateinit var client: GoogleApiClient
    private lateinit var subscriptions: CompositeSubscription

    @Inject
    lateinit var wifiAPManager: WifiApManager

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var rxBus : RxBus

    private var isRegistered = false


    //BInder implementation details
    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        val service: MobileEventService
            get() = this@MobileEventService
    }

    override fun onCreate() {
        super.onCreate()
        MobileApplication.graph.inject(this)
        wifiScanReceiver = WifiScanBroadcastReceive()
        registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        client = getApiClient(this)
        client.connect()
        subscriptions = CompositeSubscription()
        subscriptions.add(rxBus.toObservable().subscribe(getRxBusObserver()))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        subscriptions.unsubscribe()
        unregisterReceiver(wifiScanReceiver)
        Wearable.MessageApi.removeListener(client, this)
        if (client.isConnected) {
            client.disconnect()
        }
        client.unregisterConnectionCallbacks(this)
        client.unregisterConnectionFailedListener(this)
        super.onDestroy()
    }

    fun getRxBusObserver(): Action1<Any> {
        return Action1 { event ->
            if (event is WearMessage) {
//                destructor
                val(path, msg) = event
//                preferences test
                preferences.editPrefs {
                    setString(path to msg)
                }
                d("From Preferences " + preferences.getString(path, "") as String)
                sendMessageToWear(path, msg)
            } else if (event is ScanResult) {
                toast(event.SSID)
            }
        }
    }


    fun getApiClient(context: Context): GoogleApiClient {
        return GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    override fun onConnected(bundle: Bundle?) {
        Wearable.MessageApi.addListener(client, this)
        if (!isRegistered) {
            registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            isRegistered = true
        }

    }

    override fun onConnectionSuspended(i: Int) {
        Wearable.MessageApi.removeListener(client, this)
        if (isRegistered) {
            unregisterReceiver(wifiScanReceiver)
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onMessageReceived(event: MessageEvent) {
        when (event.path) {
            WEAR_MSG -> {
                when (String(event.data)) {
                    WIFI_ON -> wifiAPManager.wifiManager.isWifiEnabled = true
                    WIFI_OFF -> wifiAPManager.wifiManager.isWifiEnabled = false
                    WIFI_AP_ON -> {
                        wifiAPManager.wifiManager.isWifiEnabled = true
                        val wifiConfig = WifiConfiguration()
                        wifiConfig.configureAP {
                            SSID = WIFI_SSID
                            preSharedKey = WIFI_PWD
                        }
                        wifiAPManager.wifiManager.addNetwork(wifiConfig)
                        wifiAPManager.wifiManager.saveConfiguration()
                        wifiAPManager.wifiApConfiguration = wifiConfig
                        wifiAPManager.setWifiApEnabled(wifiConfig, true)
                        toast("WIFI AP is:${wifiAPManager.isWifiApEnabled}")
                    }
                    WIFI_AP_OFF -> {
                        wifiAPManager.setWifiApEnabled(null, false)
                        toast("WIFI AP is: ${wifiAPManager.isWifiApEnabled}")
                    }
                    START_WIFI_SCAN ->{ wifiAPManager.wifiManager.startScan()
                    d("Scan Started")
                    }
                }
            }
        }
    }


    //Send stations evant to android wear device
    private fun sendMessageToWear(path: String, text: String) {
        Observable.just<GoogleApiClient>(client).subscribeOn(Schedulers.computation()).map { googleApiClient ->
            val nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await()
            nodes.nodes
        }.filter { nodes -> nodes != null && nodes.size > 0 }.subscribe { nodes ->
            for (node in nodes) {
                Wearable.MessageApi.sendMessage(client, node.id, path, text.toByteArray())
                d("Msg sent to wear")
            }
        }
    }
}
