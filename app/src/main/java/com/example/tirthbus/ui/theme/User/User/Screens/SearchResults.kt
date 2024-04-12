package com.example.tirthbus.ui.theme.User.User.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.FilterChipData
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.filterChips
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.HomeViewModel

object SearchResultsDestination:NavigationDestination{
    override val route: String
        get() = "search_results"
    override val titleRes: Int
        get() = R.string.search_result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navigateToYatraDetail: (String) -> Unit,
    searchQuery:String,
    navigateBack:()->Unit,
    viewModel:HomeViewModel = hiltViewModel()
){
    val pagingData: LazyPagingItems<YatraDetailsResponse> = viewModel.yatrasList.collectAsLazyPagingItems()
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(searchQuery){
       // viewModel.searchYatras(searchQuery)
        Log.d("SEarch","seach query is $searchQuery")
    }

    Scaffold(
        topBar = {
            AppTopBar(title = searchQuery,
                canNavigateBack = true , navigateToSignUpScreen = { /*TODO*/},
                navigateUp = {navigateBack()},
                scrollBehavior = appScrollBehavior)
        }
    ) {innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)){

        FilterChipsList(chips = filterChips)

        LazyColumn() {
            items(pagingData.itemCount) { index ->
                val yatra = pagingData[index]
                // Check if the yatra matches the search query
                if (yatra != null && yatra.yatra?.yatraName?.contains(searchQuery, ignoreCase = true)!!) {
                    YatraCard3(item = yatra, onCardClick = { yatra.key?.let {
                        navigateToYatraDetail(
                            it
                        )
                    } })
                }
            }
        }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipsList(chips: List<FilterChipData>){
    LazyRow {
        items(chips) { chipData ->
            var selected by remember { mutableStateOf(chipData.selected) }
            FilterChip(
                onClick = { selected = !selected },
                label = { Text(chipData.text) },
                selected = selected,
                modifier = Modifier.padding(2.dp),
                leadingIcon = {
                    chipData.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    TirthBusTheme {
        FilterChipsList(chips = filterChips)
    }
}
