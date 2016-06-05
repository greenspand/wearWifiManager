package ro.sorin.utils.rxbusmodels

import android.net.wifi.ScanResult

import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by sorin on 3/31/16.
 */
object  NetworksModell {
    private val subject = PublishSubject.create<ScanResult>()

    fun set(scan: ScanResult) {
        subject.onNext(scan)
    }

    val scanResult: Observable<ScanResult>
        get() = subject

}
