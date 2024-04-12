package com.example.tirthbus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tirthbus.ui.theme.Navigation.AppNavHost
import com.example.tirthbus.ui.theme.User.User.Screens.SignUpScreenDestination
import com.example.tirthbus.ui.theme.User.User.Screens.UserHomeScreenDestination
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun MyApp(navController: NavHostController = rememberNavController()){
    LaunchedEffect(navController){
        val user = Firebase.auth.currentUser
        if (user != null){
            navController.navigate(UserHomeScreenDestination.route)
        }
        else{
            navController.navigate(SignUpScreenDestination.route)
        }
    }
    AppNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title:String,
    canNavigateBack:Boolean,
    navigateToSignUpScreen:() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp:() -> Unit = {},
    onDrawerClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    val context = LocalContext.current


    TopAppBar(
        title = { Text(text = title, textAlign = TextAlign.Center) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = {/*Toast.makeText(context,"I am working",Toast.LENGTH_SHORT).show()*/
                navigateToSignUpScreen()   },
                modifier = modifier.size(50.dp)){
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = stringResource(id = R.string.Menu))
            }
        },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = {navigateUp()},
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                        id = R.string.Back
                    )
                    )
                }
            }//Toast.makeText(context,"this is toast",Toast.LENGTH_SHORT).show()
            else{
                IconButton(onClick =onDrawerClick ) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        },

            /*colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )*/


    )
}


@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    onAccountCircleClick:() -> Unit,
    onBookingsClick:() -> Unit,
    onHomeClick:() -> Unit,
){
    BottomAppBar(modifier = Modifier.fillMaxWidth(),
        //containerColor = MaterialTheme.colorScheme.primaryContainer,

        actions =  {
            IconButton(onClick = {onHomeClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = null)

            }
            IconButton(onClick = {onBookingsClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
            }
            IconButton(onClick = {onAccountCircleClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
            }
        }
    )
}