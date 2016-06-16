package ro.sorin.wifimanager

import android.support.v7.widget.RecyclerView
import ro.sorin.utils.entities.WifiDetails

/**
 * Created by sorin on 18.12.15.
 */
interface OnWifiListClickListener {
    fun onItemClicked(holder: RecyclerView.ViewHolder, item: WifiDetails, pos: Int)
}
