package com.example.tirthbus.ui.theme.Organiser.Screens

import android.net.Uri
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
    navigateToAddYatra3:(YatraUiState)->Unit,
    viewModel: AddYatraViewModel = hiltViewModel(),
    yatraUiState: YatraUiState // Receive the UI state as a parameter
){
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    var uiState by remember {
        mutableStateOf(yatraUiState)
    }
    LaunchedEffect(yatraUiState){
        uiState = yatraUiState
        Log.d("Add yatra","receving $yatraUiState from 1st screen")
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
            AddYatra2Layout(
                includeslist = viewModel.IncludesList,
                includestempList = uiState.yatraDetails.includesList,
                onIncludesSelected = {selectedList,includestempList -> uiState = uiState.copy(
                    yatraDetails = uiState.yatraDetails.copy(includesList = selectedList)
                )},
                rulesList = viewModel.rulesList,
                rulestempList = uiState.yatraDetails.rulesList,
                onRuleSelected = {selectedList,includestempList ->
                    uiState = uiState.copy(
                    yatraDetails = uiState.yatraDetails.copy(rulesList = selectedList)
                ) },
                onNextClick = {
                    navigateToAddYatra3(uiState)
                              Log.d("Yatra","here final ui state is $uiState")
                     })
        }
    }
}

@Composable
fun AddYatra2Layout(
    onNextClick:() -> Unit,
    includeslist:List<String?>,
    rulesList:List<String?>,
    includestempList:List<String?>,
    rulestempList:List<String?>,
    onIncludesSelected: (List<String?>, List<String?>) -> Unit,
    onRuleSelected: (List<String?>, List<String?>) -> Unit,
){
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AddList2(rulesList,includeslist, includestempList ,rulestempList, onIncludesSelected,onRuleSelected )

        Button(
            onClick = onNextClick,
            //enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.next))
        }

        /* Button(
             onClick = onImageClick,
             enabled = yatraUiState.isEntryValid,
             shape = MaterialTheme.shapes.small,
             modifier = Modifier.fillMaxWidth()) {
             Text(text = stringResource(id = R.string.select_image))
         }*/
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