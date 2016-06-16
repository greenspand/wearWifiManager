package ro.sorin.utils.entities;


import android.net.wifi.ScanResult;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sorin on 6/12/16.
 */
public class WifiDetails {

    public String ssid;
    public String capabilities;

    public interface FunctionScanResult<A1, B> {
        B call(A1 a1);
    }

    public WifiDetails(String ssid, String capabilities) {
        this.ssid = ssid;
        this.capabilities = capabilities;
    }

    public static List<String> getWifiNames(List<ScanResult> scanResults) {
        return getWIFIScanResults(scanResults, new FunctionScanResult<ScanResult, String>() {
            @Override
            public String call(ScanResult scanResult) {
                return scanResult.SSID;
            }
        });
    }

    public static List<WifiDetails> getWifiNameAndCapabilities(List<ScanResult> scanResults) {
        return getWIFIScanResults(scanResults, new FunctionScanResult<ScanResult, WifiDetails>() {
            @Override
            public WifiDetails call(ScanResult scanResult) {
                return new WifiDetails(scanResult.SSID, scanResult.capabilities);
            }
        });
    }

    public static List<Integer> getWifiFrequencies(List<ScanResult> scanResults) {
        return getWIFIScanResults(scanResults, new FunctionScanResult<ScanResult, Integer>() {
            @Override
            public Integer call(ScanResult scanResult) {
                return scanResult.frequency;
            }
        });
    }


    public static <B> List<B> getWIFIScanResults(List<ScanResult> scanResults, FunctionScanResult<ScanResult, B> func) {
        ArrayList<B> wifis = new ArrayList<>();
        for (ScanResult result : scanResults) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!result.isPasspointNetwork()) {
                    wifis.add(func.call(result));
                }
            } else {
                wifis.add(func.call(result));
            }
        }
        return wifis;
    }

}
