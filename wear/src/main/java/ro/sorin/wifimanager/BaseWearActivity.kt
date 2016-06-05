package ro.sorin.wifimanager

import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ro.sorin.utils.RxBus
import javax.inject.Inject

/**
 * Created by sorin on 5/29/16.
 */
abstract class BaseWearActivity : AppCompatActivity() {

    abstract fun getActivityTitle(): Int;

    @Inject
    lateinit var rxBus : RxBus

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WearApplication.graph.inject(this);
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(getActivityTitle())
    }
}
