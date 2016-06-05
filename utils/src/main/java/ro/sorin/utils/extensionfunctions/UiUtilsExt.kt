package ro.sorin.utils.extensionfunctions

import android.app.AlertDialog
import android.app.Notification
import android.content.Context
import android.widget.Toast

/***
 *
 * Created by Sorin Albu-Irimies on 03/04/16 <br>
 */



fun Context.toast(msg : String, length : Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, length).show()

inline fun alertDialog(context : Context, func : AlertDialog.Builder.() -> Unit) : AlertDialog{
    val builder = AlertDialog.Builder(context)
    builder.func()
    return builder.create()
}

inline fun notification(context: Context, func : Notification.Builder.() -> Unit) : Notification {
    val builder = Notification.Builder(context)
    builder.func()
    return builder.build()
}