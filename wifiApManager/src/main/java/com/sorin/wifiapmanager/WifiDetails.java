package com.sorin.wifiapmanager;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by sorin on 21.12.14.
 */
public class WifiDetails {

    /**RETRIEVING LOCAL HOST IP ADDRESSES*/
    /**
     * Returns the wifi address of the hot spot ad hoc server
     *
     * @param wifimanager
     * @return
     */
    public String getWifiAddress(WifiManager wifimanager) {
        DhcpInfo info = wifimanager.getDhcpInfo();
        int rawAddress = info.serverAddress;
        return formatWifiAddress(rawAddress);
    }

    /**
     * Formats the int wifi address to a string format
     *
     * @param rawAddress
     * @return
     */
    public String formatWifiAddress(int rawAddress) {
        return (rawAddress & 0xFF) + "." + ((rawAddress >> 8) & 0xFF) + "." + ((rawAddress >> 16) & 0xFF) + "." + ((rawAddress >> 24) & 0xFF);
    }

    /**
     * GETS LOCAL IP ADDRESS NO MATTER IF IT IS HOTSPOT OR CONNECTED TO NETWORK
     *
     * @return
     */
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

}
