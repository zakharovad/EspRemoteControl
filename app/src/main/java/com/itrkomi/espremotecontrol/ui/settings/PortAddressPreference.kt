package com.itrkomi.espremotecontrol.ui.settings

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import androidx.preference.EditTextPreference

class PortAddressPreference(context: Context?, attributeSet: AttributeSet?) :
    EditTextPreference(context!!, attributeSet) {
    init {
        setOnBindEditTextListener {editText: EditText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(
                InputFilter { source, start, end, dest, dstart, dend ->
                    if (end > start){
                        val resultingTxt =
                            dest.toString().substring(0, dstart) + source.subSequence(
                                start,
                                end
                            ) + dest.toString().substring(dend)
                        if (!resultingTxt.matches("^[0-9]{1,5}$".toRegex())) {
                            return@InputFilter ""
                        }
                    }
                    return@InputFilter null
                }
            )
        }

    }
}