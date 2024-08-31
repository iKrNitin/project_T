package com.example.tirthbus.ui.theme.User.User.ViewModel

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
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {

    var signUpUiState : UserDetailResponse.User by mutableStateOf(UserDetailResponse.User())
        private set

    private val _userDetailsState: MutableStateFlow<ResultState<UserDetailResponse?>> =
        MutableStateFlow(ResultState.Loading)

    val userDetailsState: StateFlow<ResultState<UserDetailResponse?>>
        get() = _userDetailsState

    private val _documentIdState: MutableStateFlow<String?> = MutableStateFlow(null)
    val documentIdState: StateFlow<String?> = _documentIdState

    fun updateUiState(newUser:UserDetailResponse.User){
        signUpUiState = newUser
    }

    //fun createUser(authUser:UserDetail.User) = repo.createUser(authUser)
    fun createUser(authUser: UserDetailResponse.User, activity: Activity) {
        viewModelScope.launch {
            authUser.userPhnNumber?.let {
                authUser.userEmail?.let { it1 ->
                    repo.createUserWithPhone(it1, activity).collect { result ->
                        when (result) {
                            is ResultState.Loading -> Log.d("Auth", "Creating new user...")
                            is ResultState.Success -> {
                                Log.d("Auth", "User Created Successfully: ${result.data}")
                                addUser(authUser)
                            }

                            is ResultState.Failure -> Log.e("Auth", "Failed to create user: ${result.msg}")

                        }
                    }
                }
            } ?: Log.e("Auth", "Phone number is null")
        }
    }


    fun createUser2(authUser: UserDetailResponse.User) = repo.createUser(authUser)

    fun loginUser(authUser: UserDetailResponse.User) = repo.loginUser(authUser)

    fun forgotPassword(authUser: UserDetailResponse.User) = repo.resetPassword(authUser)

    fun logoutUser(authUser: UserDetailResponse.User) = repo.logoutUser(authUser)

    fun addUser(user:UserDetailResponse.User){
        viewModelScope.launch {
            repo.addUser(user).collect{
                    result ->
                when (result) {
                    is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                    is ResultState.Success -> {
                        Log.d("AddYatraViewModel", " ${result.data}")
                        _documentIdState.value = result.data
                    }
                    is ResultState.Failure -> Log.e("AddYatraViewModel", "Failed to add Yatra details: ${result.msg}")
                }
            }
        }
    }

    fun fetchUserDetail(docId:String){
        viewModelScope.launch {



           /* repo.fetchUserDetail().collect {
                Log.d("User","fetching function from viewmodel is working")
                _userDetailsState.value = it
                Log.d("User","")
                /* Log.d("User","Calling the fetch function from repo")
                 _userDetailsState.value = it*/
            }*/
        }
    }

    fun uploadImageAndAddYatra(user: UserDetailResponse.User, uri: Uri, context: Context, type: String) {
        viewModelScope.launch {
            repo.uploadUserProfile(user, uri, context, type).collect { result ->
                when (result) {
                    is ResultState.Loading -> Log.d("AddYatraViewModel", "Image upload in progress...")
                    is ResultState.Success -> {
                        Log.d("AddYatraViewModel", "Image upload successful. Uploading Yatra details...")
                        // Image upload successful, now add Yatra details
                        addUser(user.copy(userProfileUrl = result.data))
                    }
                    is ResultState.Failure -> Log.e("AddYatraViewModel", "Image upload failed: ${result.msg}")
                }
            }
        }
    }

}