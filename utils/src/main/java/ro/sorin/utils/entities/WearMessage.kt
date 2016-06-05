package ro.sorin.utils.entities

/**
 * Created by sorin on 16.12.15.
 */
class WearMessage(var path: String, var payLoad: String) {
    operator fun component1(): String {
        return path;
    }
    operator fun component2(): String {
        return payLoad;
    }
}
