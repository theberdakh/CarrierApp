package com.theberdakh.carrierapp.util

import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

fun MaterialButton.checkText(et: TextInputEditText, length: Int): Boolean {
    this.isEnabled = et.text?.length == length

    et.addTextChangedListener {
        this.checkText(et, length)
    }
    return this.isEnabled
}
