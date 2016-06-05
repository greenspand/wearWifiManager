package ro.sorin.wifimanager

import dagger.Component
import javax.inject.Singleton

/**
 * Created by sorin on 5/29/16.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class))
interface ApplicationComponent {
    fun inject(application: MobileApplication)
    fun inject(service:MobileEventService )
    fun inject(activity: BaseActivity)
}
