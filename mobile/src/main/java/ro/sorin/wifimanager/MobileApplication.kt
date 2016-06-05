package ro.sorin.wifimanager

import android.app.Application
import android.location.LocationManager
import javax.inject.Inject

/**
 * Created by sorin on 5/29/16.
 */
class MobileApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }



    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        graph.inject(this)
    }
}
