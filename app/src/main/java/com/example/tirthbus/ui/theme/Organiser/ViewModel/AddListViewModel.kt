package com.example.tirthbus.ui.theme.Organiser.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddListViewModel @Inject constructor(private val repo:YatraRepo):ViewModel(){
    var yatraUiState by mutableStateOf(YatraUiState())
        private set

    var List = listOf<String>(
        "आने जाने का किराया",
        "रहना ठहरना",
        "सुबह का नाश्ता",
        "दोपहर का भोजन",
        "रात्रि भोजन",)
}