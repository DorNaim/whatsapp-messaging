package com.softwador.whatsapp.messaging.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainPageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
//    val text: LiveData<String> = Transformations.map(_index) {
//        "שלח הודעת whatsapp לטלפון לא שמור!"
//    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}