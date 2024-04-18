package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.YatraRepo
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.AddYatraViewModel
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

object TandCDestination: NavigationDestination {
    override val route: String
        get() = "t_and_c"
    override val titleRes: Int
        get() = R.string.tandc
}

@Composable
fun TandCScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: TandCViewModel = hiltViewModel()
){
    val resultState by viewModel.result.collectAsState()
    val context = LocalContext.current

    when(val result = resultState ){
        is ResultState.Loading -> {
            Text(text = "Loading... ")
        }
        is ResultState.Success -> {
            TandCLayout(list = result.data)
        }
        is ResultState.Failure -> {
            Text(text = "Failed to fetch data ")
        }
    }
}

@Composable
fun TandCLayout(list:List<String>){
    LazyColumn{
        items(list){
            item -> Text(text = item, modifier = Modifier.padding(10.dp))
        }
    }
}


