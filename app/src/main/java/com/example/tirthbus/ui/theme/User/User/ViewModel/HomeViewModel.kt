package com.example.tirthbus.ui.theme.User.User.ViewModel

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.remember as remember1

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: YatraRepo): ViewModel(){

     val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    val yatrasList: Flow<PagingData<YatraDetailsResponse>> = searchQuery.flatMapLatest { query ->
        repo.getAllYatrasPaging(query)
            .cachedIn(viewModelScope)
    }

    fun searchYatras(query: String) {
        searchQuery.value = query
    }

    var exitDialogState by mutableStateOf(false)

    fun showExitConfirmationDialog() {
        exitDialogState = true
    }

    fun onConfirmExit(){
        exitDialogState = false
    }

    fun onDismissExit() {
        exitDialogState = false // Close the dialog
    }
}

    /*fun getAllYatra() = viewModelScope.launch {
        repo.getAllYatras().collect(){
            when(it){

                is ResultState.Success -> {_result.value = FirestoreState(data = it.data)}

                is ResultState.Failure -> {
                    _result.value = FirestoreState(
                        error = it.msg.toString()
                    )
                }
                ResultState.Loading -> {
                    _result.value = FirestoreState(isLoading = true)
                }
            }
        }
    }*/

//data class HomeUiState(val cardList: ResultState<List<YatraDetailsResponse>> = listOf(ResultState.))