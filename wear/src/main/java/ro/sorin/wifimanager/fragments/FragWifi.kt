package ro.sorin.wifimanager.fragments

import android.net.wifi.ScanResult
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ro.sorin.utils.RxBusSingleton
import ro.sorin.wifimanager.OnWifiListClickListener
import ro.sorin.wifimanager.R
import ro.sorin.wifimanager.adapter.WifiAPAdapter
import ro.sorin.wifimanager.adapter.WifiAdapter
import rx.subscriptions.CompositeSubscription

/**
 * Created by sorin on 16.12.15.
 */
class FragWifi : Fragment() {


    companion object {
        fun newInstance(): FragWifi {
            val frag = FragWifi()
            return frag
        }
    }

    internal var adapter: WifiAdapter? = null

    internal var busSub: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        busSub = CompositeSubscription()
        busSub?.add(RxBusSingleton.toObservable().subscribe({ o ->
            if (o is ScanResult) {
                adapter?.addItem((adapter as WifiAdapter).items.size, o)
            }
        }))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.frag_wifi, container, false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WifiAdapter(onWifiListClickListener)

    }

    internal var onWifiListClickListener: OnWifiListClickListener = object : OnWifiListClickListener {
        override fun onItemClicked(holder: RecyclerView.ViewHolder, item: ScanResult, pos: Int) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        busSub?.unsubscribe()
        super.onDestroy()
    }


}
