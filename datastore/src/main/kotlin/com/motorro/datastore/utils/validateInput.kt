package com.motorro.datastore.utils

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

/**
 * Validates input on focus change and text change
 * @param validate Validation function. Returns null if input is invalid or validated value
 * @param errorText Error text function. Returns error text for invalid input
 * @param onValid Callback on valid input
 */
fun <T: Any> TextInputEditText.validateInput(validate: (String) -> T?, errorText: (String) -> String, onValid: (T) -> Unit) {
    var focused = false
    var validating = false

    fun doValidate() {
        val text = this.text.toString()
        val result = validate(text)
        when {
            null != result -> {
                error = null
                onValid(result)
            }
            validating -> {
                error = errorText(text)
            }
        }
    }

    setOnFocusChangeListener { _, hasFocus ->
        when  {
            hasFocus && focused.not() -> {
                focused = true
            }
            hasFocus.not() && focused -> {
                validating = true
                doValidate()
            }
        }
    }

    addTextChangedListener {
        doValidate()
    }
}