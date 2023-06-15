package com.example.plantsservicefyp.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*

internal fun Context.log(message: String) {
    Log.d("hm123", "data => ${message}")
}

internal fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

internal fun Context.alterText(description: String): String {
    var numWords: Int = 0
    val token = StringTokenizer(description, " ")
    var word: String = token.nextToken()
    numWords = token.countTokens()
//    numWords variable counts words in a paragraph and returns length
    numWords++
    if (numWords > 10) {
        var stringBuilder = StringBuilder()
        description.split(" ").forEachIndexed { index, s ->
            if (index <= 10) {
                stringBuilder.append("${s} ")
            }
        }
        stringBuilder.append("...")
        return stringBuilder.toString()
    } else {
        return description
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun Uri.base64EncodeFromUri(context: Context): String? {
    var res: String? = null
    try {
//        val file = File(fileString)
//        val fis = FileInputStream(file)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            res = Base64.getEncoder()
                .encodeToString(context.contentResolver.openInputStream(this)?.readAllBytes())
        }
    } catch (e: Exception) {
        context.log(e.message.toString())
    }
    return res
}

internal fun Activity.createAlertDialog (): AlertDialog {
    return AlertDialog.Builder(this).let {
        this.layoutInflater.inflate(
            com.example.plantsservicefyp.R.layout.sell_fargment_alert_dialog,
            null
        ).apply {
            it.setView(this)
        }
        it.setCancelable(false)
        it.create()
    }
}