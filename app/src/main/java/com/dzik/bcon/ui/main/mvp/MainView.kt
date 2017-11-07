package com.dzik.bcon.ui.main.mvp

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.view.View
import com.dzik.bcon.R
import com.dzik.bcon.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import javax.inject.Inject
import android.R.attr.duration
import android.widget.Toast




@SuppressLint("ViewConstructor")
class MainView @Inject constructor(
        context: MainActivity
) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.activity_main, this)
    }

    fun updateText(text: String) {
        textView2.text = text
    }

    fun toast(text: String) {
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }
}