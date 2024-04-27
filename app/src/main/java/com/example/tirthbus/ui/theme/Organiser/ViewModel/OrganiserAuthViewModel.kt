package com.example.tirthbus.ui.theme.Organiser.ViewModel

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.AuthRepo
import com.example.tirthbus.Data.OrganiserDetailsResponse
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrganiserAuthViewModel @Inject constructor(private val repo:AuthRepo) : ViewModel(){
    var signUpUiState : OrganiserDetailsResponse.Organiser by mutableStateOf(OrganiserDetailsResponse.Organiser())
        private set

    private val _organiserDetailsState: MutableStateFlow<ResultState<OrganiserDetailsResponse?>> =
        MutableStateFlow(ResultState.Loading)

    val organiserDetailsState: StateFlow<ResultState<OrganiserDetailsResponse?>>
        get() = _organiserDetailsState

    private val _documentIdState: MutableStateFlow<String?> = MutableStateFlow(null)
    val documentIdState: StateFlow<String?> = _documentIdState

    fun updateUiState(newOrganiser: OrganiserDetailsResponse.Organiser){
        signUpUiState = newOrganiser
    }

    //fun createUser(authUser:UserDetail.User) = repo.createUser(authUser)
    fun createOrganiser(organiser: OrganiserDetailsResponse.Organiser) = repo.createOrganiser(organiser)

    fun createUser2(authUser: UserDetailResponse.User) = repo.createUser(authUser)

    fun loginOrganiser(organiser: OrganiserDetailsResponse.Organiser) = repo.loginOrganiser(organiser)

    fun forgotPassword(organiser: OrganiserDetailsResponse.Organiser) = repo.resetOrganiserPasword(organiser)

    fun logoutOrganiser(organiser: OrganiserDetailsResponse.Organiser) = repo.logoutOrganiser(organiser)

    fun addOrganiser(organiser: OrganiserDetailsResponse.Organiser){
        viewModelScope.launch {
            repo.addOrganiser(organiser).collect{
                    result ->
                when (result) {
                    is ResultState.Loading -> Log.d("Auth ViewModel", "Adding Organiser details...")
                    is ResultState.Success -> {
                        Log.d("Auth ViewModel", " ${result.data}")
                        _documentIdState.value = result.data
                    }
                    is ResultState.Failure -> Log.e("Auth ViewModel", "Failed to add Organiser details: ${result.msg}")
                }
            }
        }
    }

    fun createOrganiserWithPhone(phone:String,activity: Activity) = repo.createUserWithPhone(phone,activity)

    fun signInWithCredential(code:String) = repo.signWithOtp(code)
}