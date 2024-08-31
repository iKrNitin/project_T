package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.Data.OrganiserDetailsResponse
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.OrganiserAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OrganiserSigninDestination : NavigationDestination {
    override val route: String
        get() = "organiser_sign_in"
    override val titleRes: Int
        get() = R.string.signUp
}

@Composable
fun OrganiserSignInScreen(
    navigateToOrganiserHomeScreen:()->Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrganiserAuthViewModel = hiltViewModel()
){
    var signInUiState = viewModel.signUpUiState

    val scope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){

        OrganiserSignInLayout(signUpDetails = signInUiState,
            onValueChange = viewModel::updateUiState,
            onSignInClicked = {scope.launch (Dispatchers.Main){

                //viewModel.createUser(signUpUiState)
                viewModel.loginOrganiser(OrganiserDetailsResponse.Organiser(organiserEmail = signInUiState.organiserEmail, organiserEmailPassword = signInUiState.organiserEmailPassword)).collect{
                        result ->
                    when(result){
                        is ResultState.Loading -> Log.d("Auth", "Loggin in Organiser")
                        is ResultState.Success -> {
                            Log.d("Auth", "Organiser log in successfully")
                            navigateToOrganiserHomeScreen()
                        }
                        is ResultState.Failure -> Log.e("Auth", "Failed to login Organiser")
                    }
                }
            }
            },
            onForgotClicked = {scope.launch(Dispatchers.Main){
                viewModel.forgotPassword(signInUiState).collect{
                    when(it){
                        is ResultState.Success -> { Log.d("auth","forgot password successfull")
                        }
                        is ResultState.Failure -> {
                            Log.d("auth","forgot password failed")
                        }
                        ResultState.Loading -> {
                            Log.d("auth","forgot password in progress")
                        }
                    }
                }
            }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganiserSignInLayout(
    signUpDetails: OrganiserDetailsResponse.Organiser,
    onValueChange:(OrganiserDetailsResponse.Organiser) -> Unit = {},
    onSignInClicked:() -> Unit,
    onForgotClicked:() -> Unit,
    modifier: Modifier = Modifier
){
    Card (modifier = modifier.padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer ),
    ){

        Icon(imageVector = Icons.Filled.AccountCircle,contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally))

        Text(text = "Upload Profile Photo")

        Divider()
        signUpDetails.organiserEmail?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(organiserEmail = it))},
                label = { Text(stringResource(id = R.string.email)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        Divider()
        signUpDetails.organiserEmailPassword?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(organiserEmailPassword = it))},
                label = { Text(stringResource(id = R.string.email_pass)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        Divider()
        Button(
            onClick = onSignInClicked,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
            Text(text = stringResource(id = R.string.save))
        }

        Text(text = "Forgot Password",
            modifier = modifier
                .clickable { onForgotClicked()}
                .padding(5.dp),
            fontWeight = FontWeight.Bold)

    }
}