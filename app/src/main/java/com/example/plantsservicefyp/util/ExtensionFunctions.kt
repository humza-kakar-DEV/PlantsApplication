package com.example.plantsservicefyp.util

import android.content.Context
import android.util.Log
import android.widget.Toast

internal fun Context.log (message: String) {
    Log.d("hm123", "data => ${message}")
}

internal fun Context.toast (message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}