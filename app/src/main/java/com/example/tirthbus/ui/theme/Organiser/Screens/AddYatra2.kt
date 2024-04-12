package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.AddYatraViewModel
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState

object AddYatra2Destination: NavigationDestination {
    override val route: String
        get() = "add_yatra2"
    override val titleRes: Int
        get() = R.string.add_yatra2
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddYatraScreen2(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToNextScreen:()->Unit,
    viewModel: AddYatraViewModel = hiltViewModel(),
    yatraUiState: YatraUiState // Receive the UI state as a parameter
){
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    var uiState by remember {
        mutableStateOf(yatraUiState)
    }
   /* var yatraUiStateFromViewModel = viewModel.yatraUiState
    Log.d("User","initial yatraUistatefrom viewmodel is $yatraUiStateFromViewModel")
    yatraUiStateFromViewModel = yatraUiState
    Log.d("User","final yatraUistatefrom viewmodel is $yatraUiStateFromViewModel")*/

    val data = yatraUiState.yatraDetails.copy()

    LaunchedEffect(yatraUiState){
        Log.d("Add yatra","receving $yatraUiState from 1st screen")
    }

    val updateUiStateWithFields:(YatraDetailsResponse.Yatra) -> Unit = {
        viewModel.updateSpecificFields(yatraUiState.yatraDetails)
    }



    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.add_yatra),
                navigateToSignUpScreen = {},
                scrollBehavior = appBarScrollBehavior,
                canNavigateBack = true)
        }
    ) {
        FlowColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
        {
            Log.d("Yatra","current ui state is on 2nd screen is ${yatraUiState.yatraDetails.copy()}")
            AddYatraLayout2(
                //yatraUiState = viewModel.yatraUiState,
                yatraUiState = uiState,
                onYatraValueChange = {yatra -> uiState.yatraDetails.copy(organiserName = yatra.organiserName)  },
                onNextClick = {
                              Log.d("Yatra","here final ui state is ${yatraUiState.yatraDetails}")
                    //navigateToOraganiser()
                    /*uri?.let { viewModel.uploadImageToStorage2(viewModel.yatraUiState.yatraDetails,it, "image", context) }
                    */
                     })
        }
    }
}

@Composable
fun AddYatraLayout2(
    modifier: Modifier = Modifier,
    yatraUiState: YatraUiState,
    onYatraValueChange: (YatraDetailsResponse.Yatra) -> Unit ,
    onNextClick:() -> Unit,
){
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        AddContactForm(
            data = yatraUiState.yatraDetails,
            onItemValueChange = onYatraValueChange )
        Button(
            onClick = onNextClick,
           // enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.save))
        }

    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddYatraForm2(
    data: YatraDetailsResponse,
    modifier: Modifier = Modifier,
    onItemValueChange: (YatraDetailsResponse.Yatra) -> Unit,
    onSelectImageClick:() -> Unit,
    uri: Uri?,
    enabled: Boolean = true)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(value = data.bookingAmount.orEmpty(), onValueChange = {onItemValueChange(data.copy(bookingAmount = it))},
            label = { Text(text = stringResource(id = R.string.bookingAmount)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(value = data.numberOfSeats.orEmpty(), onValueChange = {onItemValueChange(data.copy(numberOfSeats = it))},
            label = { Text(text = stringResource(id = R.string.numberOfSeats)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

    }


}*/