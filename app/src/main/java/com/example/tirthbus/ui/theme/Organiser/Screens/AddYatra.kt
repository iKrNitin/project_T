package com.example.tirthbus.ui.theme.Organiser.Screens

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tirthbus.AppTopBar
import com.example.tirthbus.Data.AlertDialogBox
import com.example.tirthbus.Data.YatraDetailsResponse
import com.example.tirthbus.R
import com.example.tirthbus.ui.theme.Navigation.NavigationDestination
import com.example.tirthbus.ui.theme.Organiser.ViewModel.AddYatraViewModel
import com.example.tirthbus.ui.theme.Organiser.ViewModel.YatraUiState
import com.example.tirthbus.ui.theme.Theme.TirthBusTheme
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Text as Text1

//GoogleMapsApiKey- AIzaSyBw73OMOGz5xtZi-p6Ylr2NMNHZex9zHbc

object AddYatraDestination: NavigationDestination {
    override val route: String
        get() = "add_yatra"
    override val titleRes: Int
        get() = R.string.add_yatra
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddYatraScreen1(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToOraganiser:()->Unit,
    navigateToAddYatra2:(YatraUiState)->Unit,
    viewModel: AddYatraViewModel = hiltViewModel()
) {
    Log.d("Main", "add yatra screen is here")
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val coroutineScope = rememberCoroutineScope()

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
                uri = uri,
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
                    navigateToAddYatra2(viewModel.yatraUiState)
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

        //AddList2(rulesList,includeslist, includestempList ,rulestempList, onIncludesSelected,onRuleSelected )

        //AddContactForm(data = yatraUiState.yatraDetails, onItemValueChange = onYatraValueChange)

        Button(
            onClick = onNextClick,
            //enabled = yatraUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()) {
            Text1(text = stringResource(id = R.string.save))
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
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            data.yatraName?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(yatraName = it)) },
                    label = stringResource(
                        id = R.string.yname
                    ))
            }

            data.date?.let {
                FormTextBox(
                    value = it,
                    onValueChange = { onItemValueChange(data.copy(date = it)) },
                    label = stringResource(id = R.string.Date),
                    trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) })
            }

            data.yatraTime?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(yatraTime = it)) },
                    label = stringResource(
                        id = R.string.ytime
                    ))
            }

            /*data.yatraLocation?.let {
                FormTextBox(
                    value = it,
                    onValueChange = { onItemValueChange(data.copy(yatraLocation = it)) },
                    label = stringResource(id = R.string.ylocation),
                    trailingIcon = { Icon(Icons.Filled.Place, contentDescription = null) })
            }*/
            AutoCompletePlacesScreen(placesClient = placesClient)

            data.totalAmount?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(totalAmount = it)) },
                    label = stringResource(
                        id = R.string.totalFare
                    ))
            }

            data.bookingAmount?.let {
                FormTextBox(
                    value = it,
                    onValueChange = { onItemValueChange(data.copy(bookingAmount = it)) },
                    label = stringResource(id = R.string.bookingAmount))
            }

            data.lastDateOfBooking?.let {
                FormTextBox(value = it,
                    onValueChange = { onItemValueChange(data.copy(lastDateOfBooking = it)) },
                    label = stringResource(id = R.string.lastDateOfBooking),
                    trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) }
                )
            }
        }
    }

   /* Button(
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

    if (enabled) {
        Text1(text = stringResource(id = R.string.reqrd),
            modifier = Modifier.padding(start = 16.dp))
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
                FormTextBox(value = data.organiserName?:"",
                    onValueChange = { onItemValueChange(data.copy(organiserName = it)) },
                    label = stringResource(
                        id = R.string.Organiser
                    ))
            }


            data.contactName1?.let {
                FormTextBox2(
                    value = data.contactName1?:"",
                    onValueChange = { onItemValueChange(data.copy(contactName1 = it)) },
                    label = "Contact Person",
                    modifier = Modifier.weight(0.5f),
                    trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) })
            }

                data.contactPhn1?.let {
                    FormTextBox2(
                        value = it,
                        onValueChange = { onItemValueChange(data.copy(contactPhn1 = it)) },
                        label = "Contact Phone",
                        modifier = Modifier.weight(0.5f),
                        trailingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                    )
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
                trailingIcon: @Composable (() -> Unit)? = null){

    TextField(value = value,
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
    onDateSelected: (startDate: LocalDate, endDate: LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss) {

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(){
    // Decoupled snackbar host state from scaffold state for demo purposes.
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState, Modifier.zIndex(1f))

    val state = rememberDateRangePickerState()
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        // Add a row with "Save" and dismiss actions.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Close, contentDescription = "Localized description")
            }
            TextButton(
                onClick = {
                    snackScope.launch {
                        val range =
                            state.selectedStartDateMillis!!..state.selectedEndDateMillis!!
                        // Convert milliseconds to LocalDate
                        val startDate = Instant.ofEpochMilli(state.selectedStartDateMillis!!)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        val endDate = Instant.ofEpochMilli(state.selectedEndDateMillis!!)
                            .atZone(ZoneId.systemDefault()).toLocalDate()

// Format LocalDate to a more human-readable format
                        val formattedStartDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

// Show the formatted dates in the snackbar
                        snackState.showSnackbar("Saved range (dates): $formattedStartDate to $formattedEndDate")
                    }
                },
                enabled = state.selectedEndDateMillis != null
            ) {
                Text1(text = "Save")
            }
        }
        DateRangePicker(state = state, modifier = Modifier.weight(1f))
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddYatraPreview(){
    TirthBusTheme {
        AddContactForm(
            data = YatraDetailsResponse.Yatra(organiserName = "Nitin Kumar"),
            onItemValueChange = {}
        )
    }
}
