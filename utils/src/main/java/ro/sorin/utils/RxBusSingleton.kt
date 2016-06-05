package ro.sorin.utils

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subjects.Subject
import java.util.*

/**
 * Created by sorin on 31.10.15.
 */
 object RxBusSingleton {

    private val bus = SerializedSubject<Any, Any>(PublishSubject.create())
     fun send(any: Any) {
        bus.onNext(any)
    }

     fun toObservable(): Observable<Any> {
        return bus
    }

     fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}
