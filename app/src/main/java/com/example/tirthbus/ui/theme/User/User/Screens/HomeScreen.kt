package com.example.tirthbus.ui.theme.User.User.Screens

import android.location.Location
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.tirthbus.AppBottomBar
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.TopDestinations
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.Data.bannerList
import com.example.tirthbus.Data.topDestinationList1
import com.example.tirthbus.Data.topDestinationList2
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.Screens.GetCurrentLocation
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.HomeViewModel
import com.example.tirthbus.ui.theme.User.User.ViewModel.UserAuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object UserHomeScreenDestination : NavigationDestination {
    override val route: String
        get() = "Home_Screen"
    override val titleRes: Int
        get() = R.string.HomeScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    navigateToOraganiser:()->Unit,
    navigateToYatraDetail: (String) -> Unit,
    navigateToSearchResult:(String) -> Unit,
    navigateToSearchScreen:() -> Unit,
    navigateToSignUpScreen:()->Unit,
    onAccountCircleClick:() -> Unit,
    onBookingsClick:() -> Unit,
    onHomeClick:() -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    viewModel2: UserAuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    //val res = viewModel.result.value
    //val pagingData = viewModel.yatrasList.collectAsLazyPagingItems()
    val pagingData: LazyPagingItems<YatraDetailsResponse> =
        viewModel.yatrasList.collectAsLazyPagingItems()
    val dialogState = viewModel.exitDialogState
    val locationState = remember { mutableStateOf<Location?>(null) }

    GetCurrentLocation { location ->
        locationState.value = location
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.6f),
                drawerShape = ShapeDefaults.Medium,
                drawerContentColor = MaterialTheme.colorScheme.primary,
                drawerTonalElevation = 10.dp
            ) {
                Text(
                    text = "TirthBus",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.Organiser)) },
                    selected = false,
                    onClick = { navigateToOraganiser() },
                    icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "hey") })

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.help)) },
                    icon = { Icon(imageVector = Icons.Filled.Call, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } })

                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.about)) },
                    icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } })
            }
        }) {
        Scaffold(
            topBar =
            {
                AppTopBar(
                    title = stringResource(id = R.string.app_name),
                    canNavigateBack = false,
                    navigateToSignUpScreen = {
                        viewModel2.logoutUser(viewModel2.signUpUiState)
                        navigateToSignUpScreen()
                    },
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    scrollBehavior = appScrollBehavior
                )
                /*HomeScreenTopBar(onDrawerClick = {scope.launch { drawerState.open() }})*/
            },
            bottomBar = {
                AppBottomBar(
                    onHomeClick = { onHomeClick() },
                    onAccountCircleClick = { onAccountCircleClick() },
                    onBookingsClick = { onBookingsClick() }
                )
            }
        ) { innerpadding ->
            /* if (res.data.isNotEmpty()){
                 LazyColumn(
                     contentPadding = innerpadding,
                 ){
                     items(
                         res.data,
                         key = {
                             it.key!!
                         }
                     ){
                         items -> YatraCard(item = items,
                             modifier = modifier.padding(5.dp)

                             )
                     }
                 }

             }*/

            Box(
                modifier = Modifier
                    .padding(innerpadding)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    /*SearchBar { searchQuery ->
                        navigateToSearchResult(searchQuery)
                    }*/
                    /*SearchBarSample(onDone = {searchQuery -> navigateToSearchResult(searchQuery)})*/

                    SearchCard {
                        navigateToSearchScreen()
                    }

                    Text(
                        text = "Top Yatra Destinations",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(
                            top = 25.dp,
                            bottom = 10.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        fontWeight = FontWeight.Bold
                    )

                    // ImageSlider(images = bannerList)

                    TopDestinationLayout2(data = topDestinationList1,
                        navigateToDestination = { searchQuery -> navigateToSearchResult(searchQuery) })
                    TopDestinationLayout2(data = topDestinationList2,
                        navigateToDestination = { searchQuery -> navigateToSearchResult(searchQuery) })

                    Text(
                        text = "Yatras Departing from your city",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 25.dp,
                            bottom = 10.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        softWrap = true
                    )

                    /*Text(text = "Your City",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(3.dp))*/

                    LazyRow(modifier = modifier) {
                        items(pagingData.itemCount) { index ->
                            val yatra = pagingData[index]
                            if (yatra != null) {
                                //YatraCard(item = yatra)
                                YatraCard4(item = yatra, onCardClick = {
                                    yatra.key?.let {
                                        navigateToYatraDetail(
                                            it
                                        )
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackPressHandler(
    onBackPressed: () -> Unit
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current
        ?.onBackPressedDispatcher
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(dispatcher) {
        dispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSample(onDone:(String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    val suggestions = listOf<String>("KhatuShyam","Balaji","Vaishno Devi","Ayodhya","Vrindavan")
    val additionals = listOf<String>("Sikar, Rajasthan","Churu, Rajasthan","Jammu, Jammu Kashmir","Ayodhya, Uttar Pradesh","Mathura, Uttar Pradesh")

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }) {
        androidx.compose.material3.SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f },
            query = text,
            onQueryChange = { newText ->
                text = newText
                //onSearch(newText)
            },
            onSearch = {onDone(text)},
            active = active,
            onActiveChange = {
                             active = it
            },
            placeholder = { Text("Enter Yatra Destination") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
        ) {
                suggestions.forEachIndexed { index,suggestion ->
                    ListItem(
                        headlineContent = { Text(suggestion) },
                        supportingContent = { Text(additionals.getOrNull(index) ?: "") },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        modifier = Modifier
                            .clickable {
                                text = suggestion
                                active = false
                                onDone(suggestion)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
        }

        /*LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val list = List(100) { "Text $it" }
            items(count = list.size) {
                Text(list[it],
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
            }
        }*/
    }
}

@Composable
fun SearchBar(onClick:() -> Unit) {
    Card(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp)
        .fillMaxWidth()
        .clickable { onClick() },
        shape = ShapeDefaults.Large,
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary)
    ){
        Row {
            Icon(Icons.Default.Search, contentDescription = null,
                modifier = Modifier.padding(start = 5.dp,top = 5.dp, bottom = 5.dp))
        Text(text = "Search Yatra Destinations",
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCard(onClick:() -> Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(2f),
           /*border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),*/
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = ShapeDefaults.Large){
        Text(text = "Search Your",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        Text(text = "Destination",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        SearchBar {
            onClick()
        }
    }
}

@Composable
fun TopDestinationLayout(urlList:List<String?>) {
    LazyRow{
        items(urlList){
            imageURl ->
            AsyncImage(
                model = imageURl ,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(5.dp)
                    .clip(shape = ShapeDefaults.Large))

        }
    }
}

@Composable
fun TopDestinationLayout2(
    data:List<TopDestinations>,
    navigateToDestination:(String) -> Unit) {
    LazyRow{
        items(data){
                content ->
            Column {
            AsyncImage(
                model = content.imageURls ,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(5.dp)
                    .clip(shape = ShapeDefaults.Large)
                    .clickable { content.imageText?.let { navigateToDestination(it) } })

                content.imageText?.let {
                    Text(text = it,
                        Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(onDrawerClick: () -> Unit = {}){

    TopAppBar(title = { /*TODO*/ },
        actions = {
            IconButton(onClick = { onDrawerClick() }) {
                Icon(Icons.Default.Menu, contentDescription = null )
            }
           /* androidx.compose.material3.SearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                leadingIcon = { Icon(Icons.Default.Search, contentDescription =null)},
                trailingIcon = { Icon(Icons.Default.Clear, contentDescription =null)},
                placeholder = { Text(text = "Search Yatra ") },
                colors = SearchBarDefaults.colors(Color.Transparent)
            ){

            }*/
        },
        /*colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )*/
    )
}

@Composable
fun YatraCard(
    item:YatraDetailsResponse,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            item.yatra?.yatraName.let {
                it?.let { it1 ->
                    Text(text = it1,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            item.yatra?.date.let { it?.let { it1 -> YatraCardRow(rowText = "दिनांक - ", it1) } }

            item.yatra?.yatraLocation.let { it?.let { it1 -> YatraCardRow(rowText = "स्थान - ", rowText1 = it1) } }

            item.yatra?.yatraTime?.let { YatraCardRow(rowText = "समय - ", rowText1 = it) }
        }
    }
}

@Composable
fun YatraCard2(
    item: YatraDetailsResponse,
    modifier: Modifier = Modifier,
    onCardClick:()->Unit
) {
    val imageUrl = item.yatra?.imageUrl
    Log.d("Main", "Image url is $imageUrl")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .clickable { onCardClick() }// Aspect ratio for the card
    ) {
        // Load image from Firebase Storage

        AsyncImage(model = item.yatra?.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .clip(shape = ShapeDefaults.Medium))
        // Content overlaid on the image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(color = Color.White.copy(alpha = 0.5f))
                .clip(shape = ShapeDefaults.Medium)
                .align(Alignment.BottomStart),
            // Add padding to the content
            // Align content to the bottom of the card
        ) {
            Text(
                text = item.yatra?.yatraName ?: "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black // Text color
            )
            Spacer(modifier = Modifier.height(8.dp)) // Add space between text fields
            Text(
                text = "दिनांक - ${item.yatra?.date ?: ""}",
                color = Color.Black // Text color
            )
            Text(
                text = "स्थान - ${item.yatra?.yatraLocation ?: ""}",
                color = Color.Black // Text color
            )
            /*Text(
                text = "समय - ${item.yatra?.yatraTime ?: ""}",
                color = Color.Black// Text color
            )*/
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YatraCard3(
    item: YatraDetailsResponse,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
){
    Card(
        modifier = modifier
            .aspectRatio(2.9f)
            .fillMaxWidth()
            .clickable { onCardClick() }
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row {
            AsyncImage(model = item.yatra?.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.3f))

            FlowColumn(modifier = Modifier.padding(5.dp)) {
                item.yatra?.yatraName?.let {
                    Text(text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,)
                }
                Row() {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null )
                    item.yatra?.date?.let {
                        Text(text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(0.5f))
                    }
                    Icon(Icons.Default.CurrencyRupee, contentDescription = null)
                    item.yatra?.totalAmount?.let {
                        Text(text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,)
                    }
                }
                Row {
                    /*Text(text = stringResource(id = R.string.Organiser))
                    Spacer(modifier = Modifier.width(1.dp))
                    item.yatra?.organiserName?.let { Text(text = it) }*/
                }
                Row {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Text(text = item.yatra!!.yatraLocation!!,
                    style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Text(text = item.yatra!!.organiserName!!,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold)
                }
               /* Row {
                    Icon(Icons.Default.Face, contentDescription = null,
                        modifier = Modifier.size(15.dp))
                    Text(text = "100 Peoples viewing this yatra",
                        style = MaterialTheme.typography.bodySmall)
                }*/
            }
        }

        /*Row(modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            item.yatra?.yatraLocation?.let { Text(text = it) }
        }*/
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YatraCard4(
    item: YatraDetailsResponse,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
){
    Card(
        modifier = modifier
            .width(350.dp)
            .height(150.dp)
            .clickable { onCardClick() }
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row {
            AsyncImage(model = item.yatra?.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .fillMaxHeight(1f))

            FlowColumn(modifier = Modifier.padding(5.dp)) {
                item.yatra?.yatraName?.let {
                    Text(text = it,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,)
                }
                Row() {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null )
                    item.yatra?.date?.let {
                        Text(text = it,
                            modifier = Modifier.weight(0.5f))
                    }
                    Icon(Icons.Default.CurrencyRupee, contentDescription = null)
                    item.yatra?.totalAmount?.let {
                        Text(text = it,
                            fontWeight = FontWeight.Bold,)
                    }
                }
                Row {
                    /*Text(text = stringResource(id = R.string.Organiser))
                    Spacer(modifier = Modifier.width(1.dp))
                    item.yatra?.organiserName?.let { Text(text = it) }*/
                }
                Row {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Text(text = item.yatra!!.yatraLocation!!)
                }
                /* Row {
                     Icon(Icons.Default.Face, contentDescription = null,
                         modifier = Modifier.size(15.dp))
                     Text(text = "100 Peoples viewing this yatra",
                         style = MaterialTheme.typography.bodySmall)
                 }*/
            }
        }

        /*Row(modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            item.yatra?.yatraLocation?.let { Text(text = it) }
        }*/
    }
}

@Composable
fun YatraCardRow(
    rowText:String,
    rowText1: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp) // Adjust the padding here
    ) {
        Text(text = rowText,
            style = MaterialTheme.typography.titleLarge,)
        Text(text = rowText1,
            style = MaterialTheme.typography.titleLarge,)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    initialSearchText: String,
    modifier: Modifier = Modifier
){
    var searchText by remember { mutableStateOf(initialSearchText) }
    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Search") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { searchText = "" }) {
                    Icon(Icons.Filled.Clear, contentDescription = null)
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Handle search action
            }
        )
    )
}

@Composable
fun ImageSlider(images: List<String>){
    var currentImageIndex by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier
            .weight(1f)
            .height(100.dp)
            .fillMaxWidth()
            .padding(16.dp)) {
            // Scrollable Row of Cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(images) { index, image ->
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .height(200.dp)
                            .clickable {
                                if (index != currentImageIndex && !isAnimating) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        val delayMillis = 500L
                                        // Wait for the card to change color before animating
                                        delay(delayMillis / 2)
                                        currentImageIndex = index
                                        delay(delayMillis)
                                        isAnimating = false
                                    }
                                }
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            contentDescription = "",
                            model = image as String,
                            modifier = Modifier
                                .width(300.dp)
                                .height(300.dp)
                        )
                    }
                }

            }

        }
    }
    // Automatic Image Slider
    LaunchedEffect(currentImageIndex) {
        while (true) {
            delay(5000L)
            if (!isAnimating) {
                val nextIndex = (currentImageIndex + 1) % images.size
                currentImageIndex = nextIndex
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    TirthBusTheme {
        ImageSlider(images = bannerList)
    }
}