package com.c242ps188.mentally_app.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.C242PS188.mentally_app.R

class MyEditText: AppCompatEditText {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length < 8) {
            setError(context.getString(R.string.error_password), null)
        } else {
            error = null
        }
    }

}