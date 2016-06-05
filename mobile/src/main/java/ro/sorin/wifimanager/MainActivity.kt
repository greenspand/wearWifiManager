package ro.sorin.wifimanager

import android.content.Intent
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ro.sorin.utils.WEAR_MSG
import ro.sorin.utils.entities.WearMessage
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class MainActivity : BaseActivity() {
    override fun getRootLayout(): View {
        return coord_layout_main;
    }

    override fun getActivityTitle(): Int {
        return R.string.app_name
    }


    lateinit  var subscriptions: CompositeSubscription
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar);
        startService(Intent(this, MobileEventService::class.java))
        subscriptions = CompositeSubscription()
        subscriptions.add(rxBus.toObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { o ->
                    when (o is ScanResult) {

                    }
                })

        fab.setOnClickListener { rxBus.send(WearMessage(WEAR_MSG, "Hellow from Mobile"))}
    }


    override fun onDestroy() {
        super.onDestroy()
        subscriptions.unsubscribe()
        stopService(Intent(this, MobileEventService::class.java))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
