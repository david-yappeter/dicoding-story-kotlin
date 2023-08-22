package myplayground.example.dicodingstory.components.TextField

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

class EmailEditText : TextInputEditText {
    companion object {
        private const val MIN_CHARACTER_LENGTH = 8
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    "Format email tidak valid"
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }
}