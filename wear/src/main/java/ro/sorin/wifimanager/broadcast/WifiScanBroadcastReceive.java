package ro.sorin.wifimanager.broadcast;

/**
 * Created by sorin on 17.12.15.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Broadcast Receiver for all Wifi Netowrks found after the scan.
 */
public class WifiScanBroadcastReceive extends BroadcastReceiver {

    private final static String TAG = WifiScanBroadcastReceive.class.getSimpleName();
//    private int scanCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String action = intent.getAction();
        if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

            Observable.just(wifiManager.getScanResults()).filter(new Func1<List<ScanResult>, Boolean>() {
                @Override
                public Boolean call(List<ScanResult> scanResults) {
                    return scanResults != null && scanResults.size() > 0;
                }
            }).flatMap(new Func1<List<ScanResult>, Observable<ScanResult>>() {
                @Override
                public Observable<ScanResult> call(List<ScanResult> scanResults) {
                    return Observable.from(scanResults);
                }
            }).filter(new Func1<ScanResult, Boolean>() {
                @Override
                public Boolean call(ScanResult scanResult) {
                    return !scanResult.SSID.equals("");
                }
            }).subscribeOn(Schedulers.computation()).subscribe(new Action1<ScanResult>() {
                @Override
                public void call(ScanResult result) {
                }
            });
        }
    }
}