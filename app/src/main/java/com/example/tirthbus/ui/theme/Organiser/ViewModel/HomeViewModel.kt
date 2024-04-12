package com.example.tirthbus.ui.theme.Organiser.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OhomeViewModel @Inject constructor(private val repo: YatraRepo): ViewModel() {

    private val _result2: MutableState<FirestoreState> = mutableStateOf(FirestoreState())

    val result2: State<FirestoreState> = _result2

    init {
        getYatrabyId()
    }

    fun getYatrabyId() = viewModelScope.launch {
        repo.getYatraByUserId().collect(){
            when(it){
                is ResultState.Success -> {_result2.value = FirestoreState(data = it.data)}

                is ResultState.Failure -> {
                    _result2.value = FirestoreState(
                        error = it.msg.toString()
                    )
                }
                ResultState.Loading -> {
                    _result2.value = FirestoreState(isLoading = true)
                }
            }
        }
    }
}