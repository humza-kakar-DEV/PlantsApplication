package com.example.plantsservicefyp.util

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.constant.ChangeFragment
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
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
            if (index <= 15) {
                stringBuilder.append("${s} ")
            }
        }
        stringBuilder.append("....")
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
    }
    return res
}

internal fun Activity.aiLoadingAlertDialog(): AlertDialog {
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

internal fun View.animateHorizontalShake(
    offset: Float,
    repeatCount: Int = 3,
    dampingRatio: Float? = null,
    duration: Long = 1000L,
    interpolator: Interpolator = AccelerateDecelerateInterpolator()
) {
    val defaultDampingRatio = dampingRatio ?: (1f / (repeatCount + 1))
    val animValues = mutableListOf<Float>()
    repeat(repeatCount) { index ->
        animValues.add(0f)
        animValues.add(-offset * (1 - defaultDampingRatio * index))
        animValues.add(0f)
        animValues.add(offset * (1 - defaultDampingRatio * index))
    }
    animValues.add(0f)

    val anim: ValueAnimator = ValueAnimator.ofFloat(*animValues.toFloatArray())
    anim.addUpdateListener {
        this.translationX = it.animatedValue as Float
    }
    anim.interpolator = interpolator
    anim.duration = duration
    anim.start()
}

internal fun List<DocumentSnapshot>.createReceipt(totalPrice: String): StringBuilder {
    var receipt = StringBuilder()
    receipt.append("\n")
    receipt.append("RECEIPT \n")
    receipt.append("Date: \t ${LocalDateTime.now()}\n")
    receipt.append("----------------------------------\n")
    this.forEachIndexed { index, documentSnapshot ->
        documentSnapshot.toObject(Plant::class.java)?.apply {
            receipt.append("\n")
            receipt.append("item count:\t${index}\n")
            receipt.append("plant id:\t\t${plantId}\n")
            receipt.append("plant name:\t${name}\n")
            receipt.append("plant price:\t${price}\n")
            receipt.append("\n")
        }
    }
    receipt.append("----------------------------------\n")
    receipt.append("Total: \t\tRs.${totalPrice}\n")
    receipt.append("Payment: \t\tjazzcash")

    return receipt
}

internal fun Activity.closeApplication(supportFragmentManager: FragmentManager) {
    supportFragmentManager.popBackStack(
        ChangeFragment.WELCOME_FRAGMENT.value,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    );
    this.finish()
}

internal fun Activity.closeApplicationAlertDialog(supportFragmentManager: FragmentManager): AlertDialog {
    return AlertDialog.Builder(this).let {
        this.layoutInflater.inflate(
            com.example.plantsservicefyp.R.layout.close_application_alert_dialog,
            null
        ).apply {
            it.setView(this)
        }
        it.setNegativeButton("no") { p0, p1 ->
            p0.cancel()
        }
        it.setPositiveButton("yes") { p0, p1 -> closeApplication(supportFragmentManager) }
        it.setCancelable(true)
        it.create()
    }
}

internal fun FragmentManager.clearAllBackStackExcept(myList: MutableList<ChangeFragment>, fragmentName: ChangeFragment) {
    myList.remove(fragmentName)
    myList.forEach {
        this.popBackStack(
            it.value,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}

internal fun Activity.showAlert(layoutFile: Int): AlertDialog {
    return AlertDialog.Builder(this).let {
        this.layoutInflater.inflate(
            layoutFile,
            null
        ).apply {
            it.setView(this)
        }
        it.setPositiveButton("ok", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
            }
        })
        it.setCancelable(false)
        it.create()
    }
}

internal fun String.isValidPassword(): Boolean {
    this.let { password ->
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }
                .firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false
    }

    return true
}

internal fun Activity.showPaymentAlert(): AlertDialog {
    this.let {activity->
        val alertDialog = AlertDialog.Builder(this).let {
            this.layoutInflater.inflate(
                R.layout.payment_alert_dialog,
                null
            ).apply {
                it.setView(this)
            }
            it.setPositiveButton("ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    activity.onBackPressed()
                }
            })
            it.setCancelable(false)
            it.create()
        }
        return alertDialog
    }
}

fun Context.convertTo12Hr(time24Hr: String): String? {
    try {
        val sdf = SimpleDateFormat("H:mm")
        val dateObj: Date = sdf.parse(time24Hr)
        return SimpleDateFormat("K:mm").format(dateObj)
    } catch (e: ParseException) {
        e.printStackTrace()
        return e.message
    }
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, 0);
}