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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.Data.OrganiserDetailsResponse
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.OrganiserAuthViewModel
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.UserAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OrganiserSignUpDestination : NavigationDestination {
    override val route: String
        get() = "organiser_sign_up"
    override val titleRes: Int
        get() = R.string.signUp
}

@Composable
fun OrganiserSignUpScreen(
    navigateToOrganiserHomeScreen:()->Unit,
    navigateToOrganiserSignInScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrganiserAuthViewModel = hiltViewModel()
){
    var signUpUiState = viewModel.signUpUiState

    val scope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){

        OrganiserSignUpLayout(signUpDetails = signUpUiState,
            onValueChange = viewModel::updateUiState,
            onSignUpClicked = {scope.launch (Dispatchers.Main){

                //viewModel.createUser(signUpUiState)
                viewModel.createOrganiser(signUpUiState).collect{
                        result ->
                    when(result){
                        is ResultState.Loading -> Log.d("Auth", "Adding Organiser details...")
                        is ResultState.Success -> {
                            Log.d("Auth", "Organiser added successfully: ${result.data}")
                            viewModel.addOrganiser(signUpUiState)
                            navigateToOrganiserHomeScreen()
                        }
                        is ResultState.Failure -> Log.e("Auth", "Failed to add Organiser details: ${result.msg}")
                    }
                }
            }
            },
            onSignInClicked = {navigateToOrganiserSignInScreen()}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganiserSignUpLayout(
    signUpDetails: OrganiserDetailsResponse.Organiser,
    onValueChange:(OrganiserDetailsResponse.Organiser) -> Unit = {},
    onSignUpClicked:() -> Unit,
    onSignInClicked:() -> Unit,
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

        signUpDetails.organiserGroupName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(organiserGroupName = it))},
                label = { Text(stringResource(id = R.string.name)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
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
            onClick = onSignUpClicked,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
            Text(text = stringResource(id = R.string.save))
        }

        Text(text = "Already have an Account?SignIn",
            modifier = modifier
                .clickable { onSignInClicked() }
                .padding(5.dp),
            fontWeight = FontWeight.Bold)

    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview(){
    TirthBusTheme {
        OrganiserSignUpLayout(
            signUpDetails = OrganiserDetailsResponse.Organiser("id"),
            onSignUpClicked = { /*TODO*/ },
            onSignInClicked = { /*TODO*/ })
    }
}