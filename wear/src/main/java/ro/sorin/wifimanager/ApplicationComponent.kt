package ro.sorin.wifimanager

import android.support.v4.app.Fragment
import dagger.Component
import ro.sorin.wifimanager.BaseWearActivity
import ro.sorin.wifimanager.WearApplication
import ro.sorin.wifimanager.WearEventService
import ro.sorin.wifimanager.AndroidModule
import javax.inject.Singleton

/**
 * Created by sorin on 5/29/16.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class))
interface ApplicationComponent {
    fun inject(application: WearApplication)
    fun inject(activity: BaseWearActivity)
    fun inject(service: WearEventService)
    fun inject(fragment: Fragment)

}
