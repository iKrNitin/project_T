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

object AddYatra3Destination: NavigationDestination {
    override val route: String
        get() = "add_yatra3"
    override val titleRes: Int
        get() = R.string.add_yatra3
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddYatraScreen3(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToOraganiser:()->Unit,
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
        Log.d("Add yatra","receving $yatraUiState from 2nd screen")
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
            Log.d("Yatra","current ui state is on 3rd screen is ${yatraUiState.yatraDetails.copy()}")
            AddYatra3Layout(
                yatraUiState = uiState,
                onYatraValueChange = { yatra ->
                    uiState = uiState.copy(yatraDetails = yatra) // Update uiState when yatra details change
                },
                onNextClick = { navigateToOraganiser()
                    Log.d("Yatra","here final ui state is $uiState")})
        }
    }
}


@Composable
fun AddYatra3Layout(
    modifier: Modifier = Modifier,
    yatraUiState: YatraUiState,
    onYatraValueChange: (YatraDetailsResponse.Yatra) -> Unit,
    onNextClick:() -> Unit,
){
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AddContactForm(data = yatraUiState.yatraDetails,
            onItemValueChange = onYatraValueChange)

        Button(
            onClick = onNextClick,
            //enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}