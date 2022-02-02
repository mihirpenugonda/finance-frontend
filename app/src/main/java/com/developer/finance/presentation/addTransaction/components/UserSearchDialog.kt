package com.developer.finance.presentation.addTransaction.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.developer.finance.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class UserSearchDialog : ConstraintLayout {

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        inflate(context, R.layout.user_search_dialog, this)
    }

    var chipList = mutableListOf<Chip>()
    var chipListUser = mutableListOf<String>()

    var languages = arrayOf("C", "C++", "Java", "C#", "PHP", "JavaScript", "jQuery", "AJAX", "JSON")

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val searchView: EditText = findViewById(R.id.usd_searchView)

        searchView.setOnFocusChangeListener { _, hasFocus ->
            run {
                Log.d("hasFocus", hasFocus.toString())
                when (hasFocus) {
                    true -> {
                    }
                    false -> {
                        if(searchView.text.toString() != "")
                            createNewChip(searchView.text.toString())
                    }
                }
            }
        }


    }

    private fun createNewChip(chipText: String) {
        var newChip = Chip(context);
        newChip.text = chipText
        newChip.setOnCloseIconClickListener {
            var index = chipListUser.indexOf(chipText)
            chipListUser.removeAt(index)
            chipList.removeAt(index)
            refreshChipGroup()
        }
        newChip.isCloseIconVisible = true

        chipList.add(newChip)
        chipListUser.add(chipText)

        refreshChipGroup()
    }

    private fun refreshChipGroup() {
        var chipGroup: ChipGroup = findViewById(R.id.usd_chipGroup)
        chipGroup.removeAllViews()

        chipList.forEach {
            chipGroup.addView(it)
        }
    }

    public fun getSelectedUsers(): MutableList<String> {
        return chipListUser
    }

    interface UserSearchDialogListeners {
    }

}

