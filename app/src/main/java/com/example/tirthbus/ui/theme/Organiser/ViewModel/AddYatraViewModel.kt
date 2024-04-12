package com.example.tirthbus.ui.theme.Organiser.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.YatraRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddYatraViewModel @Inject constructor(private val repo: YatraRepo):ViewModel(){

    var yatraUiState by
        mutableStateOf(YatraUiState())
        private set

    fun updateUiState(newYatraDetails: YatraDetailsResponse.Yatra){
        yatraUiState = YatraUiState(yatraDetails = newYatraDetails,
            isEntryValid = validateInput(newYatraDetails) )
    }

    //Function to update specific field
    fun updateSpecificFields(updatedFields:YatraDetailsResponse.Yatra){
        val currentDetails = yatraUiState.yatraDetails.copy(
            organiserName = updatedFields.organiserName,
            contactName1 = updatedFields.contactName1 ?: yatraUiState.yatraDetails.contactName1,
            contactPhn1 = updatedFields.contactPhn1 ?: yatraUiState.yatraDetails.contactPhn1,
        )
        updateUiState(currentDetails)
    }

    fun updateUiState2(newYatraDetails:YatraDetailsResponse.Yatra){
        yatraUiState.yatraDetails.copy(contactName1 = newYatraDetails.contactName1)
    }

    private fun validateInput(uiState: YatraDetailsResponse.Yatra = yatraUiState.yatraDetails): Boolean{
        return with(uiState){
            yatraName!!.isNotBlank() && date!!.isNotBlank()
        }
    }


    fun uploadImageAndAddYatra(yatra: YatraDetailsResponse.Yatra, uri: Uri, context: Context, type: String) {
        viewModelScope.launch {
            repo.uploadImage2(yatra, uri, context, type).collect { result ->
                when (result) {
                    is ResultState.Loading -> Log.d("AddYatraViewModel", "Image upload in progress...")
                    is ResultState.Success -> {
                        Log.d("AddYatraViewModel", "Image upload successful. Uploading Yatra details...")
                        // Image upload successful, now add Yatra details
                        addYatraWithImageUrl(yatra.copy(imageUrl = result.data))
                    }

                    is ResultState.Failure -> Log.e("AddYatraViewModel", "Image upload failed: ${result.msg}")
                }
            }
        }
    }

    fun uploadImageAndAddYatra2(yatra: YatraDetailsResponse.Yatra, uri: Uri, context: Context, type: String) {
        viewModelScope.launch {
            repo.uploadImage2(yatra, uri, context, type)}}

    fun addYatra(yatra: YatraDetailsResponse.Yatra){
        viewModelScope.launch {
            repo.addYatra(yatra).collect{
                    result -> when(result){
                        is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                is ResultState.Success -> Log.d("AddYatraViewModel", "Yatra added successfully: ${result.data}")
                is ResultState.Failure -> Log.d("Add Yatra","yatra addition failed")
            }
            }
        }
    }

    private fun addYatraWithImageUrl(yatra: YatraDetailsResponse.Yatra) {
        viewModelScope.launch {
            repo.addYatra(yatra).collect { result ->
                when (result) {
                    is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                    is ResultState.Success -> Log.d("AddYatraViewModel", "Yatra added successfully: ${result.data}")
                    is ResultState.Failure -> Log.e("AddYatraViewModel", "Failed to add Yatra details: ${result.msg}")
                }
            }
        }
    }

    var IncludesList = listOf<String>(
        "आने जाने का किराया",
        "रहना ठहरना",
        "सुबह का नाश्ता",
        "दोपहर का भोजन",
        "रात्रि भोजन",)

    var rulesList = listOf<String>(
        "यात्रा रद्द करने पर अग्रिम भुगतान वापस नहीं किया जाएगा",
        "एक सीट पर एक ही व्यक्ति को बैठने की अनुमति होगी",
        "किसी कारण से यात्रा कार्यक्रम/रूट में बदलाव हो सकता है",
        "5 साल से ऊपर आयु के बच्चे का पूरा टिकट लगेगा",
        "5 साल से ऊपर आयु के बच्चे का आधा टिकट लगेगा",
        "बस पार्क होने के बाद ऑटो या रिक्शा का किराया भक्तों को स्वयं भुगतान करना होगा",
        "यात्रा के समय से आधा घंटा पहले यात्रा के स्थान पर आना आवश्यक है"
    )
}

data class FirestoreState(
    val data: List<YatraDetailsResponse> = emptyList(),
    val data2: List<UserDetailResponse> = emptyList(),
    val yatra: YatraDetailsResponse.Yatra = YatraDetailsResponse.Yatra(),
    val user: UserDetailResponse.User = UserDetailResponse.User(),
    val error:String = "",
    val isLoading:Boolean = false
)

data class YatraUiState(
    val yatraDetails:YatraDetailsResponse.Yatra = YatraDetailsResponse.Yatra(),
    val isEntryValid:Boolean = false
)