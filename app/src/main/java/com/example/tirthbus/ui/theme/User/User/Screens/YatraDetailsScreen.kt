package com.example.tirthbus.ui.theme.User.User.Screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tirthbus.Data.ResultState
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.example.tirthbus.ui.theme.User.User.ViewModel.YatraDetailsViewModel

object YatraDetailsScreenDestination:NavigationDestination{
    override val route: String
        get() = "Yatra_Detail"
    override val titleRes: Int
        get() = R.string.yatraDetailScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YatraDetailScreen(
    yatraId:String,
    navigateBack:()->Unit,
    viewModel: YatraDetailsViewModel = hiltViewModel()
){
    val yatraDetailsState by viewModel.yatraDetailsState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(yatraId){
        viewModel.fetchYatraDetail(yatraId)
    }
    Scaffold(
        bottomBar = {
           /*BottomAppBar {
                OutlinedButton(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "अभी बुक करें")
                }
            }*/
        }
    ) {
        innerpadding ->
    when (val yatraState = yatraDetailsState) {
        is ResultState.Loading -> {
            // Show loading indicator
            Box(contentAlignment = Alignment.Center){
            Text(text = "Loading...")
            }
        }
        /*is ResultState.Success -> {
            // Show Yatra name
            val yatraDetails = yatraState.data
            yatraDetails?.let {
                Text(text = it.yatra?.yatraName ?: "Yatra Name Not Available")
            }
        }*/

        is ResultState.Success -> {
            YatraDetailsLayout(item = yatraState.data, modifier = Modifier.padding(innerpadding),
                onCallClick = {})
        }


        else -> {
            Text(text = "Failed to fetch yatra detail")}
    }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YatraDetailsLayout(
    modifier: Modifier = Modifier,
    item: YatraDetailsResponse?,
    onCallClick: () -> Unit,
    //list: List<String>
){
    FlowColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (item != null) {
            item.yatra?.imageUrl?.let { PhotoItem(photoUrl = it) }
        }
        BasicDetailSection(item = item)
        //BusDetailSection(item = item)
        ListSection(item = item)
        CollapsibleListSection(sectionTitle = "यात्रा में शामिल हैं:-", items = item?.yatra!!.includesList )
        //YatraDescriptionSection(item = item)
        if (item != null) {
            item.yatra?.let { CollapsibleListSection(sectionTitle = "यात्रा नियम:-", items = it.rulesList ) }
        }
        ContactDetailSection(item = item,onCallClick)
        Send(item)

    //YatraIncludeSection(list = list)

      /*  item?.let { yatraDetails ->
            CollapsibleListSection(
                sectionTitle = "Includes",
                items = yatraDetails.yatra?.includesList ?: emptyList(),
                initiallyExpanded = !yatraDetails.yatra?.includesList.isNullOrEmpty()
            )
            CollapsibleListSection(
                sectionTitle = "यात्रा नियम:-",
                items = yatraDetails.yatra?.rulesList ?: emptyList(),
                initiallyExpanded = !yatraDetails.yatra?.rulesList.isNullOrEmpty()
            )
            // Other sections...
        }*/
    }
}

@Composable
fun ImageSection(
    photos:List<String>,
    modifier: Modifier = Modifier
){
    LazyRow(contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)){
        items(photos){
                photoUrl ->
            PhotoItem(photoUrl = photoUrl)
        }
    }
}

@Composable
fun PhotoItem(photoUrl:String){
    Card(modifier = Modifier.padding(top = 10.dp)
    ){
        /*Image(painter = painterResource(id = R.drawable.loading), contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Inside)*/
        AsyncImage(
            model = photoUrl ,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Inside)
    }
}

@Composable
fun YatraIncludeSection(
    list: List<String>,
    modifier: Modifier = Modifier
){
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Yatra Includes:-")
            list.forEach {
                    text:String ->
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text(text = text)  })
            }
        }
    }
}

@Composable
fun BasicDetailSection(
    item: YatraDetailsResponse?,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(start = 5.dp)) {
            if (item != null) {
                Text(text = item.yatra?.yatraTitle ?: "",
                    fontWeight = FontWeight.Bold,
                    style = typography.titleLarge
                    )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextRow(text1 = "Departure Date - ", tex2 = item?.yatra?.departureDate?:"")
            TextRow(text1 = "Arrival Date - ", tex2 = item?.yatra?.arrivalDate?:"")
            TextRow(text1 = "Departure Point - ", tex2 = item?.yatra?.departurePoint?:"")

            TextRow(text1 = stringResource(id = R.string.totalFare), tex2 = item?.yatra?.totalAmount ?: "")

            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.bookingAmount), tex2 = item.yatra?.bookingAmount ?: "")
            }
            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.lastDateOfBooking), tex2 = item.yatra?.lastDateOfBooking ?: "")
            }
        }
    }
}

@Composable
fun Send(
    item: YatraDetailsResponse?
){
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT,"\"${item!!.yatra!!.organiserName} द्वारा आयोजित बस यात्रा ${item.yatra!!.departureDate} को ${item.yatra!!.yatraTitle} जा रही है जिसका किराया ${item.yatra!!.includesList} सहित ${item.yatra!!.totalAmount} रुपए प्रति सवारी है जो भी सज्जन जाना चाहे वह बुकिंग राशि देकर अपनी सीट बुक करा ले।\"\n")
    type = "text/plain"}
    val shareIntent = Intent.createChooser(sendIntent, null)
    Button(onClick = {shareLauncher.launch(shareIntent)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = MaterialTheme.shapes.large) {
        Icon(Icons.Default.Share ,contentDescription = null)
        Text(text = " शेयर करेंं",)
    }
}

@Composable
fun BusDetailSection(
    item: YatraDetailsResponse?,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(5.dp)) {

           /* if (item != null) {
                TextRow(text1 = stringResource(id = R.string.busType), tex2 = item.yatra?.busType ?: "")
            }
            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.numberOfSeats), tex2 = item.yatra?.numberOfSeats ?: "")
            }*/
            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.totalFare), tex2 = item.yatra?.totalAmount ?: "")
            }
            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.bookingAmount), tex2 = item.yatra?.bookingAmount ?: "")
            }
            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.lastDateOfBooking), tex2 = item.yatra?.lastDateOfBooking ?: "")
            }

        }
    }
}

@Composable
fun ContactDetailSection(
    item: YatraDetailsResponse?,
    onCallClick:()-> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(5.dp)) {

            //Text(text = stringResource(id = R.string.Organiser), fontWeight = FontWeight.Bold)

            if (item != null) {
                TextRow(text1 = stringResource(id = R.string.Organiser), tex2 = item.yatra?.organiserName ?: "")
            }
            /*
            if (item != null) {
                TextRow(text1 = item.yatra?.contactName1?:"", tex2 = item.yatra?.contactPhn1 ?: "")
            }*/

            Button(onClick = {onCallClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                shape = MaterialTheme.shapes.large) {
                Icon(Icons.Default.Call ,contentDescription = null)
                Text(text = " सीट बुक करें ",)
            }

           /* if (item != null) {
                TextRow(text1 = item.yatra?.contactName2?:"", tex2 = item.yatra?.contactPhn2 ?: "")
            }
            if (item != null) {
                Text(text = item.yatra?.paymentMethod?:"")
            }*/
            
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListSection(
    item: YatraDetailsResponse?,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "Bus Facilities")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                maxItemsInEachRow = 1
            ){
                if (item != null) {
                    item.yatra?.busFacilities?.forEach() { item ->
                        // Replace Text with the composable you want to use to display each item
                        Text(text = item.toString(), modifier = Modifier.padding(8.dp))
                    }
                }
            }

        }
    }
}

@Composable
fun CollapsibleListSection(
    sectionTitle: String,
    items: List<String?>,
    initiallyExpanded: Boolean = true
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = sectionTitle,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier.clickable { expanded = !expanded },
            trailingContent = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                )
            }
        )
        if (expanded) {
            items.forEach { item ->
                ListItem(headlineContent = {
                    if (item != null) {
                        Text(text = item)
                    }
                })
            }
        }
    }
}
}

@Composable
fun CollapsibleListSection2(
    sectionTitle: String,
    items: List<String?>,
    initiallyExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            ListItem(
                modifier = Modifier.clickable { expanded = !expanded },
                headlineContent = {
                    Text(
                        text = sectionTitle,
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            )

            AnimatedVisibility(visible = expanded) {
                Column {
                    items.forEach { item ->
                        ListItem(headlineContent = {
                            if (item != null) {
                                Text(text = item,
                                    modifier = Modifier.alpha(alpha))
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun YatraDescriptionSection(
    item: YatraDetailsResponse?,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(5.dp)) {
            if (item != null) {
                Text(text = "यात्रा जानकारी:-",
                    fontWeight = FontWeight.Bold,
                )
            }
            if (item != null) {
                item.yatra?.yatraDescription?.let { Text(text = it) }
            }
        }
    }
}

@Composable
fun TextRow(text1:String,tex2:String){
    Row {
        Text(text = text1, style = typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = tex2, style = typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun YatraDetailScreenPreview(){
    TirthBusTheme {
        YatraDetailsLayout(item = YatraDetailsResponse(YatraDetailsResponse.Yatra("Shri Khatushyam ji dham yatra","23 may 2034","raat 4 bje", organiserName = "Nitin Kumar", includesList = listOf("item1","item2","item3","item4"), rulesList = listOf("rule1","rule2","rule3","rule4"), busFacilities = listOf("AC Bus","SEater","WaterBottle"))),
            onCallClick = {})
       /* CollapsibleListSection2(sectionTitle = "Includes", items = listOf("item1","item2","item3","item4") )*/
    }
}