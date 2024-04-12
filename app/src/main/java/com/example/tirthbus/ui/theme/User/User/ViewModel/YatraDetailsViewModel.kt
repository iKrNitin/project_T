package com.example.tirthbus.ui.theme.User.User.ViewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.YatraRepo
import com.example.tirthbus.ui.theme.Organiser.ViewModel.FirestoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YatraDetailsViewModel @Inject constructor(private val repo: YatraRepo):ViewModel(){

    private val _yatraDetailsState: MutableStateFlow<ResultState<YatraDetailsResponse?>> =
        MutableStateFlow(ResultState.Loading)

    val yatraDetailsState: StateFlow<ResultState<YatraDetailsResponse?>>
        get() = _yatraDetailsState

    fun fetchYatraDetail(yatraId: String) {
        viewModelScope.launch {
            repo.fetchYatraDetail(yatraId).collect {
                _yatraDetailsState.value = it
            }
        }
    }

    fun callMember (context: Context,number:String){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    }


}