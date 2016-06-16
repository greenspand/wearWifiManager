package ro.sorin.wifimanager.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ro.sorin.utils.entities.WifiDetails
import ro.sorin.wifimanager.OnWifiListClickListener
import ro.sorin.wifimanager.R
import ro.sorin.wifimanager.viewholder.WifisViewHolder
import java.util.*

class WifiAdapter(private val onWifiListClickListener: OnWifiListClickListener) : RecyclerView.Adapter<WifisViewHolder>() {

    val items = ArrayList<WifiDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_wifi, parent, false)
        return WifisViewHolder(view)
    }

    //
    override fun onBindViewHolder(holder: WifisViewHolder, position: Int) {
        holder.sssidView.text = items[position].ssid
        holder.capabilitiesView.text = "" + items[position].capabilities
        holder.rootWifiRow.setOnClickListener { onWifiListClickListener.onItemClicked(holder, items[holder.adapterPosition], position) }
    }

    fun addItem(position: Int, item: WifiDetails) {
        items.add(position, item)
        notifyItemInserted(position)

    }

    fun addItems(items: ArrayList<WifiDetails>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()

    }

    fun removeItem(item: WifiDetails) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun getItemAtPos(pos: Int): WifiDetails {
        return items[pos]
    }


}