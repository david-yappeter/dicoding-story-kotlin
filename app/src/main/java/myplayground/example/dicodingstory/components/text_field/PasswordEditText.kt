package myplayground.example.dicodingstory.components.text_field

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import myplayground.example.dicodingstory.R

/*
    There is a bug regarding using built-in 'error' value from edit text
    https://stackoverflow.com/questions/34717302/drawable-right-in-edit-text-not-updating-after-error

    currently, there are no solution regarding this problem
 */

class PasswordEditText : TextInputEditText, View.OnTouchListener {
    private lateinit var eyeVisibleButton: Drawable
    private lateinit var eyeVisibleOffButton: Drawable
    private var isPasswordVisible = false

    companion object {
        private const val MIN_CHARACTER_LENGTH = 8
        private const val INPUT_TYPE_TEXT_PASSWORD = 129
        private const val INPUT_TYPE_TEXT_VISIBLE_PASSWORD = 145
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() { // initialize drawable
        eyeVisibleButton = ContextCompat.getDrawable(context, R.drawable.visibility) as Drawable
        eyeVisibleOffButton =
            ContextCompat.getDrawable(context, R.drawable.visibility_off) as Drawable

        inputType = INPUT_TYPE_TEXT_PASSWORD

        // override on touch listener
        setOnTouchListener(this)
        showEyeVisibleButton()

        // set on text change listener
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString().length < MIN_CHARACTER_LENGTH) {
                    "Password tidak boleh kurang dari $MIN_CHARACTER_LENGTH karakter"
                } else {
                    showCurrentEyeButton()
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun showCurrentEyeButton() {
        if (isPasswordVisible) {
            setButtonDrawables(null, null, null, null)
            showEyeVisibleOffButton()
        } else {

            setButtonDrawables(null, null, null, null)
            showEyeVisibleButton()
        }
    }

    private fun showEyeVisibleButton() {
        setButtonDrawables(endOfTheText = eyeVisibleButton)
    }

    private fun showEyeVisibleOffButton() {
        setButtonDrawables(endOfTheText = eyeVisibleOffButton)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean { // end
        if (compoundDrawables[2] != null) {
            val buttonDrawable = compoundDrawables[2]

            val eyeButtonStart: Float
            val eyeButtonEnd: Float
            var isEyeButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                eyeButtonEnd = (buttonDrawable.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < eyeButtonEnd -> isEyeButtonClicked = true
                }

            } else {
                eyeButtonStart = (width - paddingEnd - buttonDrawable.intrinsicWidth).toFloat()
                when {
                    event.x > eyeButtonStart -> isEyeButtonClicked = true
                }
            }

            if (isEyeButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        inputType = if (isPasswordVisible) {
                            showEyeVisibleButton()
                            INPUT_TYPE_TEXT_PASSWORD
                        } else {
                            showEyeVisibleOffButton()
                            INPUT_TYPE_TEXT_VISIBLE_PASSWORD
                        }
                        isPasswordVisible = !isPasswordVisible

                        return true
                    }

                    else -> return false
                }

            } else {
                return false
            }
        }

        return false
    }
}