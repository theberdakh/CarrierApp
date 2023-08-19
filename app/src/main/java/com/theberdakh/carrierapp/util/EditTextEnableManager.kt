package com.theberdakh.carrierapp.util

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theberdakh.carrierapp.app.App
import java.util.regex.Pattern

fun MaterialButton.checkText(et: TextInputEditText, length: Int): Boolean {
    this.isEnabled = et.text?.length == length


    et.addTextChangedListener {
        this.checkText(et, length)
    }
    return this.isEnabled
}

fun TextInputEditText.setErrorText(
    parent: TextInputLayout,
    errorText: String = "Toltiriw kerek!",
    doAfter: Boolean = false,
    condition: (Editable?) -> Boolean

): Boolean {

    var setError = true

    if (doAfter){
        this.doAfterTextChanged {
            if (condition(this.text)) {
                parent.error = errorText
            } else {
                parent.error = null
                setError = false
            }
        }
    }
    else {
        this.addTextChangedListener {
            if (condition(this.text)) {
                parent.error = errorText
            } else {
                parent.error = null
                setError = false
            }
        }
    }

    return !setError
}




fun AutoCompleteTextView.setCustomAdapter(vararg options: String){

    val arrayAdapter = ArrayAdapter(
        App.instance,
        R.layout.simple_spinner_dropdown_item,
        options)

    this.setText(options[0])

    this.setAdapter(arrayAdapter)



}