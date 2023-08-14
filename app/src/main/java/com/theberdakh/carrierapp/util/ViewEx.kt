package com.theberdakh.carrierapp.util

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.theberdakh.carrierapp.app.App


fun makeToast(msg: String) {
    Toast.makeText(App.instance, msg, Toast.LENGTH_SHORT).show()
}

fun showSnackBar(view: View, msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, msg, length).show()
}