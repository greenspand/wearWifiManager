package ro.sorin.wifimanager.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import ro.sorin.wifimanager.R;

/**
 * Created by sorin on 18.12.15.
 */
public class WifisViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout rootWifiRow;
    public TextView sssidView;
    public TextView capabilitiesView;

    public WifisViewHolder(View itemView) {
        super(itemView);
        rootWifiRow = (LinearLayout) itemView.findViewById(R.id.root_wifi_row);
        sssidView = (TextView) itemView.findViewById(R.id.tv_ssid);
        capabilitiesView = (TextView) itemView.findViewById(R.id.tv_capabilities);
    }
}
