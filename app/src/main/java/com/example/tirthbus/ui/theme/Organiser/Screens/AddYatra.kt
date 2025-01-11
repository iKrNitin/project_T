package com.example.tirthbus.ui.theme.Organiser.Screens

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.AddYatraViewModel
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import androidx.compose.material3.Text as Text1

//GoogleMapsApiKey- AIzaSyBw73OMOGz5xtZi-p6Ylr2NMNHZex9zHbc

object AddYatraDestination: NavigationDestination {
    override val route: String
        get() = "add_yatra"
    override val titleRes: Int
        get() = R.string.add_yatra
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddYatraScreen1(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToOraganiser:()->Unit,
    navigateToAddYatra2:(YatraUiState,Uri?)->Unit,
    viewModel: AddYatraViewModel = hiltViewModel()
) {
    Log.d("Main", "add yatra screen is here")
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val coroutineScope = rememberCoroutineScope()

    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            imageUris = if (uris.size > 5) uris.take(5) else uris
        }
    )

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri = it}
    )
    val context = LocalContext.current
   // val responseList = viewModel.yatraUiState.yatraDetails
    val placeClient = Places.createClient(context)
    val yatraData = viewModel.yatraUiState.yatraDetails
    var dialogState by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(id = R.string.add_yatra),
                navigateToSignUpScreen = {},
                scrollBehavior = appScrollBehavior,
                canNavigateBack = true)
        }
    ) {
        FlowColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
        {
            AddYatraLayout(
                yatraUiState = viewModel.yatraUiState,
                placesClient = placeClient,
                onYatraValueChange = {yatra -> viewModel.updateUiState(yatra) },
                onSelectImageClick = {
                    singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                //onSelectImageClick = {multiplePhotoPicker.launch("image/*")},
                uri = uri,
                imageUris = imageUris,
                modifier = Modifier
                    .fillMaxWidth(),
                includeslist = viewModel.IncludesList,
                includestempList = viewModel.yatraUiState.yatraDetails.includesList,
                onIncludesSelected = {selectedList,includestempList -> viewModel.updateUiState(viewModel.yatraUiState.yatraDetails.copy(includesList = selectedList))},
                rulesList = viewModel.rulesList,
                rulestempList = viewModel.yatraUiState.yatraDetails.rulesList,
                onRuleSelected = {selectedList,includestempList -> viewModel.updateUiState(viewModel.yatraUiState.yatraDetails.copy(rulesList = selectedList))},
                onNextClick = {
                    Log.d("Add Yatra","CLICKING ON On next")
                    Log.d("Add Yatra","sending ui state ${viewModel.yatraUiState} to next screen")
                    navigateToAddYatra2(viewModel.yatraUiState,uri)
                    /*coroutineScope.launch {
                        viewModel.uploadImageAndAddYatra(
                            viewModel.yatraUiState.yatraDetails,
                            uri?: return@launch,
                            context,
                            "image"
                        )
                    }*/

                   // Log.d("Yatra","current ui state is ${viewModel.yatraUiState.yatraDetails.copy()}")
                    //navigateToOraganiser()
                   /* uri?.let { viewModel.uploadImageAndAddYatra(viewModel.yatraUiState.yatraDetails,it, context, "image") }*/

                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddYatraLayout(
    modifier: Modifier = Modifier,
    placesClient: PlacesClient,
    yatraUiState: YatraUiState,
    onYatraValueChange: (YatraDetailsResponse.Yatra) -> Unit,
    onSelectImageClick:() -> Unit,
    uri: Uri?,
    onNextClick:() -> Unit,
    includeslist:List<String?>,
    rulesList:List<String?>,
    includestempList:List<String?>,
    rulestempList:List<String?>,
    onIncludesSelected: (List<String?>, List<String?>) -> Unit,
    onRuleSelected: (List<String?>, List<String?>) -> Unit,
    imageUris: List<Uri>,
){
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AddYatraForm(data = yatraUiState.yatraDetails,
            onItemValueChange = onYatraValueChange,
            onSelectImageClick = onSelectImageClick,
            uri = uri,
            placesClient = placesClient,
            modifier = Modifier.fillMaxWidth())

        /*AddYatraForm2(data = yatraUiState.yatraDetails ,
            onItemValueChange = onYatraValueChange ,
            onSelectImageClick = { /*TODO*/ })*/

        Button(onClick = onSelectImageClick) {
            Text(text = "Select Images")
        }

        LazyRow {
            items(imageUris) { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        //AddList2(rulesList,includeslist, includestempList ,rulestempList, onIncludesSelected,onRuleSelected )

        //AddContactForm(data = yatraUiState.yatraDetails, onItemValueChange = onYatraValueChange)

        Button(
            onClick = onNextClick,
            //enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text1(text = stringResource(id = R.string.next))
        }

       /* Button(
            onClick = onImageClick,
            enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.select_image))
        }*/
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddYatraForm(
    data: YatraDetailsResponse.Yatra,
    modifier: Modifier = Modifier,
    onItemValueChange:(YatraDetailsResponse.Yatra) -> Unit ,
    placesClient: PlacesClient,
    onSelectImageClick:() -> Unit,
    uri: Uri?,
    enabled: Boolean = true)
{
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
    }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            data.yatraTitle?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(yatraTitle = it)) },
                    label = stringResource(
                        id = R.string.yname
                    )
                    ,modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions()
                )
            }

            Row(modifier = Modifier.fillMaxWidth()){

                DateTextField2(
                    label = stringResource(id = R.string.DepartureDate),
                    dateValue = data.departureDate,
                    onDateSelected = { newDate -> onItemValueChange(data.copy(departureDate = newDate)) }
                )

                Spacer(modifier = Modifier.width(5.dp))

                data.departureTime?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(departureTime = it)) },
                    label = stringResource(
                        id = R.string.ytime
                    ),modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(),
                    )
            }
            }

            Row(modifier = Modifier.fillMaxWidth()){
                DateTextField2(
                    label = stringResource(id = R.string.DepartureDate),
                    dateValue = data.arrivalDate,
                    onDateSelected = { newDate -> onItemValueChange(data.copy(arrivalDate = newDate)) }
                )

                Spacer(modifier = Modifier.width(5.dp))

                data.arrivalTime?.let {
                    FormTextBox(value = it,
                        onValueChange = { onItemValueChange(data.copy(arrivalTime = it)) },
                        label = stringResource(
                            id = R.string.ytime
                        ),modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(),
                    )
                }
            }

            data.departurePoint?.let {
                FormTextBox(
                    value = it,
                    onValueChange = { onItemValueChange(data.copy(departurePoint = it)) },
                    label = stringResource(id = R.string.ylocation),
                    trailingIcon = { Icon(Icons.Filled.Place, contentDescription = null) }
                    ,modifier = Modifier.fillMaxWidth()
                ,keyboardOptions = KeyboardOptions())
            }
           // AutoCompletePlacesScreen(placesClient = placesClient)

            Row(modifier = Modifier.fillMaxWidth()) {
                data.totalAmount?.let {
                    FormTextBox(value = it,
                        onValueChange = {onItemValueChange(data.copy(totalAmount = it))},
                    label =  stringResource(id = R.string.totalFare)
                        ,modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                data.bookingAmount?.let { FormTextBox(value = it, onValueChange = {onItemValueChange(data.copy(bookingAmount = it))},
                    label =  stringResource(id = R.string.bookingAmount)
                    ,modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                }
            }

            DateTextField2(
                label = stringResource(id = R.string.lastDateOfBooking),
                dateValue = data.lastDateOfBooking,
                onDateSelected = { newDate -> onItemValueChange(data.copy(lastDateOfBooking = newDate)) }
            )

            data.yatraDescription?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(yatraDescription = it)) },
                    label = stringResource(id = R.string.YatraDescription),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            //DynamicEntryList()

            data.organiserName?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(organiserName = it)) },
                    label = stringResource(id = R.string.Organiser),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        }

    /*Button(
        onClick = onSelectImageClick,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()) {
        Text1(text = stringResource(id = R.string.select_single_img))
    }*/

    AsyncImage(model = uri, contentDescription = "Add Image",modifier = Modifier
        .size(150.dp)
        .clickable { onSelectImageClick() }
        .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = ShapeDefaults.Large
        ))

    if (enabled) {
        Text1(text = stringResource(id = R.string.reqrd),
            modifier = Modifier.padding(start = 16.dp))
    }
}


@Composable
fun DateTextField2(
    label: String,
    dateValue: String?,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate by remember { mutableStateOf(dateValue ?: "") }

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val cal = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear} ($dayOfWeek)"
            onDateSelected(selectedDate)
        },
        year, month, day
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(text = label) },
        modifier = modifier.clickable { datePickerDialog.show() },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier.clickable {
                    datePickerDialog.show()
                }
            )
        },

        readOnly = true
    )
}


@Composable
fun FormTextBoxWithDivider(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        FormTextBox(
            value = value,
            onValueChange = onValueChange,
            label = label,
            modifier = modifier,
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions()
        )
        Divider()
    }
}


@Composable
fun AddContactForm(
    data: YatraDetailsResponse.Yatra,
    modifier: Modifier = Modifier,
    onItemValueChange:(YatraDetailsResponse.Yatra) -> Unit ,
    enabled: Boolean = true)
{
    Card (
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            data.organiserName?.let {
                FormTextBox2(value = data.organiserName?:"",
                    onValueChange = { onItemValueChange(data.copy(organiserName = it)) },
                    label = stringResource(
                        id = R.string.Organiser
                    ))
            }
            }
        }
    }
    /*Button(
        onClick = onSelectImageClick,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()) {
        Text1(text = stringResource(id = R.string.select_single_img))
    }

    AsyncImage(model = uri, contentDescription = null,modifier = Modifier
        .size(200.dp)
        .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = ShapeDefaults.Large
        ))*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextBox(value:String,
                onValueChange:(String) -> Unit,
                label:String,
                trailingIcon: @Composable (() -> Unit)? = null,
                modifier: Modifier = Modifier,
                keyboardOptions: KeyboardOptions){

    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = { Text1(text = label)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier,
        singleLine = true,
        trailingIcon = trailingIcon,
        shape = ShapeDefaults.Large,
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextBox2(value:String,
                onValueChange:(String) -> Unit,
                label:String,
                trailingIcon: @Composable (() -> Unit)? = null,
                 modifier: Modifier = Modifier){

    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = { Text1(text = label)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledBorderColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = trailingIcon,
        shape = ShapeDefaults.Large
    )
}

@Composable
fun SinglePhotoPicker(
    viewModel: AddYatraViewModel = hiltViewModel(),
    onUploadClick:()->Unit
){
    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri = it}
    )

    val context = LocalContext.current

    Column {
        Button(onClick = {
            singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text1(text = "Pick Single Image")
        }

        AsyncImage(model = uri, contentDescription = null,
            modifier = Modifier.size(200.dp))

        Button(onClick = {
            uri?.let {
                //viewModel.uploadImageToStorage2(uri!!,"image",context)
                onUploadClick()
            }
        }) {
            Text1(text = "Upload")
        }
    }
}


@Composable
fun DatePickerDialog(
    selectedDate: String?,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            onDateSelected("$dayOfWeek, $dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (selectedDate != null) {
        val sdf = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(selectedDate)
        date?.let {
            calendar.time = it
            datePickerDialog.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    datePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    LaunchedEffect(Unit) {
        datePickerDialog.show()
    }
}


@Composable
fun FormTextBoxWithDatePicker(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { /* Text field is read-only, so this is empty */ },
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },  // Show the date picker when clicked
        enabled = enabled,
        trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
        singleLine = true,
        readOnly = true  // Makes the text field non-editable
    )

    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = value,
            onDateSelected = {
                onValueChange(it)
                showDatePicker = false  // Dismiss the date picker after selection
            },
            onDismissRequest = {
                showDatePicker = false  // Dismiss if the user cancels the date picker
            }
        )
    }
}


@Composable
fun TwoTextFieldsInRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "First Text",
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Text(
            text = "Second Text",
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
    }
}


@Composable
fun DynamicTextFieldList() {
    var currentText by remember { mutableStateOf("") }
    var entriesList by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // TextField for current input
        TextField(
            value = currentText,
            onValueChange = { currentText = it },
            label = { Text("Enter a value") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to add the current input to the list
        Button(
            onClick = {
                if (currentText.isNotEmpty()) {
                    entriesList = entriesList + currentText
                    currentText = "" // Clear the TextField after adding
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the list of entries
        Text("Entries:")
        LazyColumn {
            items(entriesList) { entry ->
                Text(text = entry, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun DynamicEntryList() {
    var textFieldValue by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf(listOf<String>()) }
    val suggestions = listOf("Khatu", "Balaji", "Haridwar", "Rishikesh")

    Column {
        // TextField with Add Button
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Enter item") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                if (textFieldValue.isNotBlank()) {
                    entries = entries + textFieldValue
                    textFieldValue = ""
                }
            }) {
                Text("Add")
            }
        }

        // Suggestions List (shown only when typing)
        if (textFieldValue.isNotBlank()) {
            // Filter suggestions and limit to top two
            val filteredSuggestions = suggestions.filter {
                it.contains(textFieldValue, ignoreCase = true)
            }.take(2)

            // Overlay suggestions
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    elevation = CardDefaults.cardElevation(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    LazyColumn {
                        items(filteredSuggestions) { suggestion ->
                            Text(
                                text = suggestion,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        textFieldValue = suggestion
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Entries List with Remove Button
        LazyColumn {
            items(entries) { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(entry, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        entries = entries.filterNot { it == entry }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddYatraPreview(){
    TirthBusTheme {
       DynamicEntryList()
    }
}
