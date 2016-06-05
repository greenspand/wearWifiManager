package ro.sorin.wifimanager

import android.net.wifi.ScanResult
import android.support.v7.widget.RecyclerView

/**
 * Created by sorin on 18.12.15.
 */
interface OnWifiListClickListener {
    fun onItemClicked(holder: RecyclerView.ViewHolder, item: ScanResult, pos: Int)
}
