package com.example.tirthbus.ui.theme.Organiser.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.AppBottomBar
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.OhomeViewModel
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.Screens.YatraCard
import kotlinx.coroutines.launch

object OrganiserHomeScreenDestination : NavigationDestination {
    override val route: String
        get() = "Organiser_Home_Screen"
    override val titleRes: Int
        get() = R.string.Organiser
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganiserHomeScreen(
    navigateToUser: () -> Unit,
    navigateToAddYatra: () -> Unit,
    viewModel: OhomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val res2 = viewModel.result2.value

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = ShapeDefaults.Medium,
                drawerContentColor = MaterialTheme.colorScheme.primary,
                drawerTonalElevation = 10.dp) {
                Text(text = "Drawer", modifier = Modifier.padding(16.dp))
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.user)) },
                    selected = false,
                    onClick = { navigateToUser() },
                    icon = { Icon(imageVector = Icons.Filled.Face, contentDescription = "hey") })

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.help)) },
                    icon = { Icon(imageVector = Icons.Filled.Call, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }  })

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.about)) },
                    icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }  })
            }
        }){
        Scaffold(
            topBar =
            {
                AppTopBar(
                    title = stringResource(id = R.string.Organiser), canNavigateBack = false,
                    onDrawerClick = {scope.launch { drawerState.open() }},
                    scrollBehavior = appScrollBehavior,
                    navigateToSignUpScreen = {})
            },
            floatingActionButton = {
                //Using Box and Align to center FAB
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    contentAlignment = Alignment.Center) {
                    OutlinedButton(onClick = {navigateToAddYatra()}) {
                        Text(text = stringResource(id = R.string.add_yatra))
                    }
                }
                /* FloatingActionButton(onClick = { navigateToAddYatra() }) {
                 Icon(imageVector = Icons.Filled.Add, contentDescription = null) }*/
            },
            bottomBar = {
               // AppBottomBar()
            }
        ) {
                innerpadding ->
            if (res2.data.isNotEmpty()){
                LazyColumn(contentPadding = innerpadding,){
                    items(
                        res2.data,
                        key = {it.key!!}
                    ){
                        items -> YatraCard(item = items,
                            modifier = modifier.padding(5.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrganiserHomePreview(){
    TirthBusTheme {

    }
}