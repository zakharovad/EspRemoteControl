package com.itrkomi.espremotecontrol.ui.settings
import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreference.OnBindEditTextListener

/**
 * EditTextPreference that only allows input of IP Addresses, using the Phone
 * input type, and automatically inserts periods at the earliest appropriate
 * interval.
 */
// Note; this probably isn't the best pattern for this - a Factory of Decorator
// pattern would have made more sense, rather than inheritance. However, this
// pattern is consistent with how other android Widgets are invoked, so I went
// with this to prevent confusion
class IPAddressPreference(context: Context?, attributeSet: AttributeSet?) :
    EditTextPreference(context!!, attributeSet) {
    init {
        setOnBindEditTextListener { editText: EditText ->
            editText.inputType = InputType.TYPE_CLASS_PHONE
            editText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
                if (end > start) {
                    val destTxt = dest.toString()
                    val resultingTxt =
                        destTxt.substring(0, dstart) + source.subSequence(
                            start,
                            end
                        ) + destTxt.substring(dend)
                    if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?".toRegex())) {
                        return@InputFilter ""
                    } else {
                        val splits =
                            resultingTxt.split("\\.".toRegex()).toTypedArray()
                        for (i in splits.indices) {
                            if(splits[i].isEmpty()){
                                continue;
                            }

                            if (Integer.valueOf(splits[i]) > 255) {
                                return@InputFilter ""
                            }
                        }
                    }
                }
                return@InputFilter null
            })
            editText.addTextChangedListener(object : TextWatcher {
                var deleting = false
                var lastCount = 0
                override fun afterTextChanged(s: Editable) {
                    if (!deleting) {
                        val working = s.toString()
                        val split =
                            working.split("\\.".toRegex()).toTypedArray()
                        val string = split[split.size - 1]
                        if (string.length == 3 || string.equals("0", ignoreCase = true)
                            || string.length == 2 && string.toInt() > 25
                        ) {
                            s.append(".")
                            return;
                        }
                    }
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    deleting = if (lastCount < count) {
                        false
                    } else {
                        true
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Nothing happens here
                }
            })
        }
    }
}