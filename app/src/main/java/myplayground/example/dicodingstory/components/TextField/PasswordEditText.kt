package myplayground.example.dicodingstory.components.TextField

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText : TextInputEditText {

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
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < MIN_CHARACTER_LENGTH) {
                    error = "Password tidak boleh kurang dari $MIN_CHARACTER_LENGTH karakter"
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }
}