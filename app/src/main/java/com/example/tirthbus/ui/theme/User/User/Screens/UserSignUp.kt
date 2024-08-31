package com.example.tirthbus.ui.theme.User.User.Screens

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.UserDetailResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.UserAuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SignUpScreenDestination : NavigationDestination {
    override val route: String
        get() = "sign_up"
    override val titleRes: Int
        get() = R.string.signUp
}

/*@Composable
fun SignUpScreen(
    navigateToUserHomeScreen:()->Unit,
    navigateToSignInScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserAuthViewModel = hiltViewModel()
){
    var signUpUiState = viewModel.signUpUiState

    val scope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){

        SignUpLayout(signUpDetails = signUpUiState,
            onValueChange = viewModel::updateUiState,
            onSignUpClicked = {scope.launch (Dispatchers.Main){

               viewModel.createUser(signUpUiState,this)
                /*viewModel.createUser2(signUpUiState).collect{
                    result ->
                    when(result){
                        is ResultState.Loading -> Log.d("AddYatraViewModel", "Adding Yatra details...")
                        is ResultState.Success -> {
                            Log.d("AddYatraViewModel", "Yatra added successfully: ${result.data}")
                            viewModel.addUser(signUpUiState)
                            navigateToUserHomeScreen()

                        }
                        is ResultState.Failure -> Log.e("AddYatraViewModel", "Failed to add Yatra details: ${result.msg}")
                    }
                }*/
            }

              /* viewModel.createUser(
                    UserDetail.User(signUpUiState)).collect{ when(it){

                    is ResultState.Success -> { Log.d("main","signup successfull")
                        navigateToUserHomeScreen()
                    }
                    is ResultState.Failure -> {
                        Log.d("main","signup failed")
                    }
                    ResultState.Loading -> {
                        Log.d("main","signup in progress")

                    }
                }
                }*/
               /*viewModel.addUser(signUpUiState.copy(
                    userId = signUpUiState.userId,
                    userName = signUpUiState.userName,
                    userEmail = signUpUiState.userEmail,
                    userEmailPassword = signUpUiState.userEmailPassword,
                    userPhnNumber = signUpUiState.userPhnNumber,
                    userProfileUrl = signUpUiState.userProfileUrl))*/

            },
            onSignInClicked = {navigateToSignInScreen()}
        )
    }
}*/

@Composable
fun SignUpScreen(
    navigateToUserHomeScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserAuthViewModel = hiltViewModel()
) {
    var signUpUiState = viewModel.signUpUiState

    // Access the current Activity
    val activity = LocalContext.current as? Activity

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        SignUpLayout(
            signUpDetails = signUpUiState,
            onValueChange = viewModel::updateUiState,
            onSignUpClicked = {
                scope.launch(Dispatchers.Main) {
                    // Pass the Activity to the createUser function
                    activity?.let {
                        viewModel.createUser(signUpUiState, it)
                    } ?: Log.e("SignUpScreen", "Activity is null")
                }
            },
            onSignInClicked = { navigateToSignInScreen() }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpLayout(
    signUpDetails: UserDetailResponse.User,
    onValueChange:(UserDetailResponse.User) -> Unit = {},
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

        signUpDetails.userName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(userName = it))},
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
        signUpDetails.userEmail?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(userEmail = it))},
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
        signUpDetails.userEmailPassword?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {onValueChange(signUpDetails.copy(userEmailPassword = it))},
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
        SignUpLayout(
            signUpDetails = UserDetailResponse.User("id"),
            onSignUpClicked = { /*TODO*/ },
            onSignInClicked = { /*TODO*/ })
    }
}