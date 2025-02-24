package com.example.week3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SampleViewModel : ViewModel() {
    private val count = MutableLiveData<Int>();
    var number = 0;

    val badgeCount : LiveData<Int> get() = count

    fun increment(){
        count.postValue(++number)
    }

    override fun onCleared() {
        super.onCleared()
    }
}