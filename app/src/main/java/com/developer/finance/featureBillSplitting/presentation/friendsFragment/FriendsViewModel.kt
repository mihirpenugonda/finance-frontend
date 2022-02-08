package com.developer.finance.featureBillSplitting.presentation.friendsFragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor() : ViewModel(){

    fun getText(): String {
        return "Hello"
    }

}