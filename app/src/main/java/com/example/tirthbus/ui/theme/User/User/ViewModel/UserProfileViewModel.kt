package com.example.tirthbus.ui.theme.User.User.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.AuthRepo
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import com.example.tirthbus.ui.theme.Organiser.ViewModel.FirestoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repo:AuthRepo,
):ViewModel(){

    private val _result2: MutableState<FirestoreState> = mutableStateOf(FirestoreState())

    val result2: State<FirestoreState> = _result2

    init {
        fetchdetail()
    }

    fun fetchdetail() = viewModelScope.launch {
        repo.fetchUserDetail().collect(){
            when(it){
                is ResultState.Success -> { _result2.value = FirestoreState(data2 = it.data)
                }

                is ResultState.Failure -> {
                    _result2.value = FirestoreState(error = it.msg.toString())
                }
                ResultState.Loading -> {
                    _result2.value = FirestoreState(isLoading = true)
                }
            }
        }
    }

   /* fun fetchUserDetail() = viewModelScope.launch {
        repo.fetchUserDetail().collect(){
            when(it){
                is ResultState.Success -> {_result2.value = FirestoreState(data2 = it)}

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
    }*/

   /* private val _userDetailsState: MutableStateFlow<ResultState<UserDetail?>> =
        MutableStateFlow(ResultState.Loading)
    val userDetailsState: StateFlow<ResultState<UserDetail?>>
        get() = _userDetailsState

    fun fetchUserDetail() = viewModelScope.launch {
        repo.fetchUserDetail().collect{
            result ->
            when(result){
                is ResultState.Loading -> Log.d("User","Loading state")
                is ResultState.Success -> Log.d("User","This is Success State")
                else -> Log.d("User","This is Error state")
            }
        }
    }*/
    /*fun getUserbyUserId() = viewModelScope.launch {
        repo.fetchUserDetail().collect{
                result ->
            when (result) {
                is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                is ResultState.Success -> {
                    Log.d("AddYatraViewModel", "Yatra added successfully: ${result.data}")
                    result.data
                }
                is ResultState.Failure -> Log.e("AddYatraViewModel", "Failed to add Yatra details: ${result.msg}")
            }
        }
    }*/

    /*private val _userDetailsState: MutableStateFlow<ResultState<UserDetail?>> =
        MutableStateFlow(ResultState.Loading)
    val userDetailsState: StateFlow<ResultState<UserDetail?>>
        get() = _userDetailsState

    private val _documentIdState: MutableStateFlow<String?> = MutableStateFlow(null)
    val documentIdState: StateFlow<String?> = _documentIdState
    private var docId:String = ""*/
  /*  init {
        viewModelScope.launch {
            fun addUser(user:UserDetail.User){
                viewModelScope.launch {
                    repo.addUser(user).collect{
                            result ->
                        when (result) {
                            is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                            is ResultState.Success -> {
                                Log.d("AddYatraViewModel", " ${result.data}")
                                docId = result.data
                                Log.d("User","doc id is $docId")
                            }
                            is ResultState.Failure -> Log.e("AddYatraViewModel", "Failed to add Yatra details: ${result.msg}")
                        }
                    }
                }
            }
            // Observe the document ID emitted by UserAuthViewModel
            fetchUserDetail(docId)
        }
    }*/
   /* fun fetchUserDetail(userDocId:String){
        viewModelScope.launch {
            repo.fetchUserDetail(userDocId).collect {
                Log.d("User","fetching function from viewmodel is working")
                _userDetailsState.value = it
                Log.d("User","")
               /* Log.d("User","Calling the fetch function from repo")
                _userDetailsState.value = it*/
            }
        }
    }*/
}

data class UserUiState(
    val userDetails:UserDetailResponse.User = UserDetailResponse.User(),
    //val isEntryValid:Boolean = false
)