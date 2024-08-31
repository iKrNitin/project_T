package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
/*@Composable
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
}*/

@Composable
fun OrganiserSignUpLayout(
    signUpDetails: OrganiserDetailsResponse.Organiser,
    onValueChange: (OrganiserDetailsResponse.Organiser) -> Unit = {},
    onSignUpClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {

        OrganiserTextField(
            value = signUpDetails.organiserGroupName.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserGroupName = it)) },
            label = stringResource(id = R.string.OrganiserGroupName)
        )

        OrganiserTextField(
            value = signUpDetails.organiserName.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserName = it)) },
            label = stringResource(id = R.string.OrganiserName)
        )

        OrganiserTextField(
            value = signUpDetails.organiserCity.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserCity = it)) },
            label = stringResource(id = R.string.city)
        )

        OrganiserTextField(
            value = signUpDetails.organiserAddress.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserAddress = it)) },
            label = stringResource(id = R.string.address)
        )

        OrganiserTextField(
            value = signUpDetails.organiserEmail.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserEmail = it)) },
            label = stringResource(id = R.string.email)
        )

        OrganiserTextField(
            value = signUpDetails.organiserEmailPassword.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserEmailPassword = it)) },
            label = stringResource(id = R.string.email_pass)
        )


        OrganiserTextField(
            value = signUpDetails.organiserPhnNumber.orEmpty(),
            onValueChange = { onValueChange(signUpDetails.copy(organiserPhnNumber = it)) },
            label = stringResource(id = R.string.phnNmbr)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I accept the Privacy Policy and Terms & Conditions",
                modifier = Modifier.clickable { isChecked = !isChecked }
            )
        }

        Button(
            onClick = {
                if (isChecked) {
                    onSignUpClicked()
                } else {
                    Toast.makeText(context,"Please accept Terms&Conditions",Toast.LENGTH_SHORT).show()
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = stringResource(id = R.string.save))
        }

        Text(
            text = "Already have an Account? SignIn",
            modifier = modifier
                .clickable { onSignInClicked() }
                .padding(5.dp),
            fontWeight = FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganiserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
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