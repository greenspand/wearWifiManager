package ro.sorin.wifimanager.ui
import android.widget.ToggleButton
import org.jetbrains.anko.*
import ro.sorin.utils.*
import ro.sorin.utils.entities.WearMessage
import ro.sorin.wifimanager.WearActivity


class MainUI : AnkoComponent<WearActivity> {
    val ID_WIFI_TOGGLE = 1;
    val ID_WIFI_AP_TOGGLE = 2;

    override fun createView(ui: AnkoContext<WearActivity>) = ui.apply {
        verticalLayout {
            padding = dip(30)
            toggleButton {
                turnWifiOnOff()
                id = ID_WIFI_TOGGLE
                text = "Enable wifi"
                textSize = 24f
            }
            toggleButton {
                startStopWIFIAp()
                text = "Create AP"
                textSize = 24f
                id = ID_WIFI_AP_TOGGLE
            }
        }
    }.view

    fun ToggleButton.turnWifiOnOff() {
        this.setOnCheckedChangeListener { compoundButton, b ->
            if (b) RxBusSingleton.send(WearMessage(WEAR_MSG, WIFI_ON)) else RxBusSingleton.send(WearMessage(WEAR_MSG, WIFI_OFF))
        }
    }

    fun ToggleButton.startStopWIFIAp() {
        this.setOnCheckedChangeListener { compoundButton, b ->
            if (b) RxBusSingleton.send(WearMessage(WEAR_MSG, WIFI_AP_ON)) else RxBusSingleton.send(WearMessage(WEAR_MSG, WIFI_AP_OFF))
        }
    }
}