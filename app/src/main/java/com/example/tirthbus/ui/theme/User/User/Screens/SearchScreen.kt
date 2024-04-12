package com.example.tirthbus.ui.theme.User.User.Screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination

object SearchScreenDestination: NavigationDestination {
    override val route: String
        get() = "search_screen"
    override val titleRes: Int
        get() = R.string.search_screen
}

@Composable
fun SearchScreen(
    navigateToSearchResult:(String) -> Unit,
    modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            SearchBarSample(onDone = {searchQuery -> navigateToSearchResult(searchQuery)})
        }
    ) {
        
    }
}