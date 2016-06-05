package ro.sorin.wifimanager.adapter

import android.net.wifi.ScanResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import ro.sorin.wifimanager.OnWifiListClickListener
import ro.sorin.wifimanager.R
import ro.sorin.wifimanager.viewholder.WifisViewHolder

class WifiAPAdapter(private val onWifiListClickListener: OnWifiListClickListener) : RecyclerView.Adapter<WifisViewHolder>() {

    val items = ArrayList<ScanResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_wifi, parent, false)
        return WifisViewHolder(view)
    }

    //
    override fun onBindViewHolder(holder: WifisViewHolder, position: Int) {
//        holder.tvSSID.text = items[position].SSID
//        holder.tvStrength.text = "" + items[position].frequency
//        holder.rootWifi.setOnClickListener { onWifiListClickListener.onItemClicked(holder, items[holder.adapterPosition], position) }
    }

    fun addItem(position: Int, item: ScanResult) {
        items.add(position, item)
        notifyItemInserted(position)

    }

    fun addItems(items: ArrayList<ScanResult>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()

    }

    fun removeItem(item: ScanResult) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun getItemAtPos(pos: Int): ScanResult {
        return items[pos]
    }


}