package com.developer.finance.common.components

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent


class DropdownEditText(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatEditText(context, attrs){

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP)  {
            clearFocus()
            this.setText("")
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun addTextChangedListener(watcher: TextWatcher?) {
        val newWatcher = object: TextWatcher {
            var mEnterOccurred = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var textStr = s.toString()
                if (textStr.contains("\n")) {
                    // Wipe off the \n
                    textStr = textStr.replace("\n", "")
                    mEnterOccurred = true
                    setText(textStr)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(mEnterOccurred) {
                    // Change focus to another EditText
                    clearFocus()
                    // Change flag to default
                    mEnterOccurred = false
                }
            }
        }
        return super.addTextChangedListener(newWatcher)
    }

}