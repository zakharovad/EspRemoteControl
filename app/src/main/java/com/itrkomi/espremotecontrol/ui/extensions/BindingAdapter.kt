package com.itrkomi.espremotecontrol.ui.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["app:hide"])
fun hide(view: View, bool:Boolean){
    view.visibility = if (bool) View.GONE else View.VISIBLE
}
@BindingAdapter(value = ["app:show"])
fun show(view: View, bool:Boolean){
    view.visibility = if (bool) View.VISIBLE else View.GONE
}