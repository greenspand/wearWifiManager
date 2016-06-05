package ro.sorin.wifimanager

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ro.sorin.utils.RxBus
import javax.inject.Inject

/**
 * Created by sorin on 5/29/16.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val REQUEST_FINE_LOCATION = 0

    abstract fun getActivityTitle(): Int;

    abstract fun getRootLayout(): View;


    @Inject
    lateinit var rxBus: RxBus

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileApplication.graph.inject(this);
        requestWriteSettingsPermission()
        requestWifiScanPermissions()
    }

    private fun requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                Snackbar.make(getRootLayout(), "Wifi AP creation permission active", Snackbar.LENGTH_SHORT);
            } else {
                val intent: Intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.data = Uri.parse("package:" + packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(getActivityTitle())
    }

    private fun requestWifiScanPermissions(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(getRootLayout(), "Grant fine location permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, {
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION);
                    });
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION);
        }
        return false;
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.
                } else {
                    // The user disallowed the requested permission.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            Snackbar.make(coord_layout_main, "Wifi AP creation permission granted", Snackbar.LENGTH_SHORT);
        } else {
            Snackbar.make(coord_layout_main, "Wifi AP creation permission not granted", Snackbar.LENGTH_SHORT);

        }
    }
}
