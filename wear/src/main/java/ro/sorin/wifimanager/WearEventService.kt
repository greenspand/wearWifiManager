package ro.sorin.wifimanager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import ro.sorin.utils.RxBus
import ro.sorin.utils.entities.WearMessage
import ro.sorin.utils.extensionfunctions.d
import ro.sorin.utils.extensionfunctions.toast
import ro.sorin.wifimanager.broadcast.WifiScanBroadcastReceive
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by sorin on 15.12.15.
 */
class WearEventService : Service(),
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        MessageApi.MessageListener,
        ChannelApi.ChannelListener {

    companion object {
        private val TAG = WearEventService::class.java.simpleName
    }

    lateinit var client: GoogleApiClient
    lateinit var busSubscription: CompositeSubscription
    internal var nodID = ""
    private var isRegistered = false

    @Inject
    lateinit var wifiManager: WifiManager

    @Inject
    lateinit var rxBus : RxBus


    lateinit var wifiScanBroadcastReceive: WifiScanBroadcastReceive

    override fun onCreate() {
        super.onCreate()
        WearApplication.graph.inject(this)
        client = getApiClient(this)
        client.connect()
        busSubscription = CompositeSubscription()
        busSubscription.add(rxBus.toObservable().subscribe(rxBusObserver))
        wifiScanBroadcastReceive = WifiScanBroadcastReceive()
    }

    /**
     * Here we send our message to our hand held device
     */
    val rxBusObserver: Action1<Any>
        get() = Action1 { event ->
            if (event is WearMessage) {
                val path = event.path.toString()
                val msg = event.payLoad.toString()
                sendMessageToMobile(path, msg)
            }
        }


    fun getApiClient(context: Context): GoogleApiClient {
        return GoogleApiClient.Builder(context).addApi(Wearable.API).addConnectionCallbacks(this).build()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onMessageReceived(msg: MessageEvent) {
        d(String(msg.data))
        toast(String(msg.data))
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.MessageApi.removeListener(client, this)
        if (client.isConnected ) {
            client.disconnect()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onConnected(bundle: Bundle?) {
        wifiManager.startScan()
        Wearable.MessageApi.addListener(client, this)
        if (!isRegistered) {
            isRegistered = true
            registerReceiver(wifiScanBroadcastReceive, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "Disconnected " + i)
        if (isRegistered) {
            unregisterReceiver(wifiScanBroadcastReceive)
        }
    }

    //Send stations msg to android wear device
    private fun sendMessageToMobile(path: String, text: String) {
        if (nodID == "") {
            Observable.just(client)
                    .subscribeOn(Schedulers.computation())
                    .map { googleApiClient -> Wearable.NodeApi.getConnectedNodes(googleApiClient).await().nodes }
                    .filter { nodes -> nodes != null && nodes.size > 0 }
                    .subscribe { nodes ->
                nodID = nodes[0].id
                Wearable.MessageApi.sendMessage(client, nodID, path, text.toByteArray())
            }
        } else {
            Wearable.MessageApi.sendMessage(client, nodID, path, text.toByteArray())
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    /**
     * Channel API Listener
     */
    override fun onChannelOpened(channel: Channel) {

    }

    override fun onChannelClosed(channel: Channel, i: Int, i1: Int) {

    }

    override fun onInputClosed(channel: Channel, i: Int, i1: Int) {

    }

    override fun onOutputClosed(channel: Channel, i: Int, i1: Int) {

    }
}
