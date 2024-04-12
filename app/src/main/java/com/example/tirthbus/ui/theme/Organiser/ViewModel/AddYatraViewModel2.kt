package com.example.tirthbus.ui.theme.Organiser.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddYatraViewModel2 @Inject constructor(private val repo:YatraRepo):ViewModel(){

    var yatraUiState2 by
    mutableStateOf(YatraUiState())
        private set

    fun updateUiState2(newYatraDetails: YatraDetailsResponse.Yatra){
        yatraUiState2= yatraUiState2.copy(yatraDetails = newYatraDetails)
        // isEntryValid = validateInput(newYatraDetails))
    }
}