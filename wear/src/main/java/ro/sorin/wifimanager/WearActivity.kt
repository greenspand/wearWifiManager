package ro.sorin.wifimanager


import android.content.Intent
import android.net.wifi.ScanResult
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.activity_wifi_networks.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import ro.sorin.utils.*
import ro.sorin.utils.entities.WearMessage
import ro.sorin.utils.extensionfunctions.toast
import ro.sorin.wifimanager.adapter.WifiAdapter
import rx.functions.Action1
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class WearActivity : BaseWearActivity() {

    override fun getActivityTitle(): Int {
        return R.string.app_name
    }

    lateinit var wifiAdapter: WifiAdapter

    companion object {
        private val TAG = WearActivity::class.java.simpleName
    }

    private lateinit var busSubscription: CompositeSubscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_networks)
        wifiAdapter = WifiAdapter(object : OnWifiListClickListener {
            override fun onItemClicked(holder: RecyclerView.ViewHolder, item: ScanResult, pos: Int) {
            }
        });
        tog_wifi_state.turnWifiOnOff()
        tog_wifi_ap_state.startStopWifiAp()
        fab_get_wifi_networks.scanForWifiNetworks()
        rv_wifi_list.adapter = wifiAdapter
        startService(Intent(this, WearEventService::class.java))
    }

    fun FloatingActionButton.scanForWifiNetworks() {
        this.setOnClickListener {
            rxBus.send(WearMessage(WEAR_MSG, START_WIFI_SCAN))
        }
    }

    fun ToggleButton.turnWifiOnOff() {
        this.setOnCheckedChangeListener { compoundButton, b ->
            if (b) rxBus.send(WearMessage(WEAR_MSG, WIFI_ON)) else rxBus.send(WearMessage(WEAR_MSG, WIFI_OFF))
        }
    }

    fun ToggleButton.startStopWifiAp() {
        this.setOnCheckedChangeListener { compoundButton, b ->
            if (b) rxBus.send(WearMessage(WEAR_MSG, WIFI_AP_ON)) else rxBus.send(WearMessage(WEAR_MSG, WIFI_AP_OFF))
        }
    }

    override fun onPause() {
        super.onPause()
        busSubscription.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        busSubscription = CompositeSubscription()
        busSubscription.add(rxBus.toObservable().subscribe(rxBusObserver))
    }


    val rxBusObserver: Action1<Any>
        get() = Action1 { event ->
            if (event is WearMessage) {
                val msg = event.payLoad
                val path = event.path
                toast(path + msg)
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, WearEventService::class.java))
    }
}
