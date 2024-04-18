package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TandCViewModel @Inject constructor(private val yatraRepo: YatraRepo): ViewModel(){

    private val _result: MutableStateFlow<ResultState<List<String>>> =
        MutableStateFlow(ResultState.Loading)

    val result: StateFlow<ResultState<List<String>>>
        get() = _result

    fun fetchTandC(){
        Log.d("TC","calling fetch function from viewmodel")
        viewModelScope.launch {
            yatraRepo.getTandC().collect {
                _result.value = it
            }
        }
    }
}