package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tirthbus.Data.inputChipData
import com.example.tirthbus.Data.list3
import com.example.tirthbus.ui.theme.Organiser.ViewModel.AddYatraViewModel
import com.example.tirthbus.ui.theme.Theme.Purple80
import com.example.tirthbus.ui.theme.Theme.PurpleGrey40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddList(
    viewModel: AddYatraViewModel = hiltViewModel(),
    list:List<String?>,
    tempList:List<String?>,
    onChipSelected: (List<String?>, List<String?>) -> Unit,
    modifier: Modifier = Modifier
){
    val appScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Text(
            text = "यात्रा में शामिल हैं:-",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        IncludeChip(list = viewModel.IncludesList,
            tempList = viewModel.yatraUiState.yatraDetails.includesList ,
            onChipSelected = {selectedList,tempList -> viewModel.updateUiState(viewModel.yatraUiState.yatraDetails.copy(includesList = selectedList)) } )

        /*Button(onClick = {coroutineScope.launch{
            viewModel.updateUiState(viewModel.yatraUiState.yatraDetails.copy(includesList = viewModel.yatraUiState.yatraDetails.includesList)) // Update yatraDetails with the selected list
            viewModel.addYatra(viewModel.yatraUiState.yatraDetails) }},
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth())  {
            Text(text = stringResource(id = R.string.save))
        }*/
    }
}

@Composable
fun AddList2(
    rulesList:List<String?>,
    includesList: List<String?>,
    includestempList:List<String?>,
    rulestempList:List<String?>,
    facilityTempList:List<String?>,
    onIncludesSelected: (List<String?>, List<String?>) -> Unit,
    onRuleSelected: (List<String?>, List<String?>) -> Unit,
    onFacilitySelected: (List<String?>, List<String?>) -> Unit,
    modifier: Modifier = Modifier){
    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
    {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Text(
                text = "यात्रा में शामिल हैं:-",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
            )
            IncludeChip(
                list = includesList,
                tempList = includestempList,
                onChipSelected = onIncludesSelected
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = "बस सुविधाएं:-",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
            )
            IncludeChip2(
                list = list3,
                tempList = facilityTempList,
                onChipSelected = onFacilitySelected
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        )
        {
            Text(
                text = "यात्रा नियम एवं शर्तें:-",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
            )
            IncludeChip(list = rulesList, tempList = rulestempList, onChipSelected = onRuleSelected)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IncludeChip(
    list:List<String?>,
    tempList:List<String?>,
    onChipSelected: (List<String?>, List<String?>) -> Unit
){
    var responseList by rememberSaveable {
        mutableStateOf(tempList)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        maxItemsInEachRow = 2
    ) {
        list.forEachIndexed{
                index: Int, s: String? ->
            InputChip(
                selected = responseList.contains(s) ,
                onClick = { responseList = if (responseList.contains(s)){
                    responseList.minus(s)
                }
                else {
                    responseList.plus(s)
                }
                    // Call the callback function to update selected chips
                    onChipSelected(responseList, tempList)},
                label = {
                    if (s != null) {
                        Text(
                            text = s,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                border = InputChipDefaults.inputChipBorder(
                    borderColor = if (!responseList.contains(s)) PurpleGrey40 else Color.Transparent,
                    borderWidth = if (responseList.contains(s)) 0.dp else 2.dp
                ),
                colors = InputChipDefaults.inputChipColors(
                    containerColor = if (responseList.contains(s)) Purple80 else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    if (responseList.contains(s)) Icon(
                        Icons.Default.Check,
                        contentDescription = "")
                    else null
                },
                modifier = Modifier.padding(horizontal = 3.dp)
            )
            // println("response list is $responseList")
            Log.d("User","response list is $responseList")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IncludeChip2(
    list: List<Pair<String?, ImageVector>>,
    tempList: List<String?>,
    onChipSelected: (List<String?>, List<String?>) -> Unit
) {
    var responseList by rememberSaveable {
        mutableStateOf(tempList)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        maxItemsInEachRow = 2
    ) {
        list.forEachIndexed { index, (label, icon) ->
            InputChip(
                selected = responseList.contains(label),
                onClick = {
                    responseList = if (responseList.contains(label)) {
                        responseList - label
                    } else {
                        responseList + label
                    }
                    // Call the callback function to update selected chips
                    onChipSelected(responseList, tempList)
                },
                label = {
                    if (label != null) {
                        Row {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                border = InputChipDefaults.inputChipBorder(
                    borderColor = if (!responseList.contains(label)) PurpleGrey40 else Color.Transparent,
                    borderWidth = if (responseList.contains(label)) 0.dp else 2.dp
                ),
                colors = InputChipDefaults.inputChipColors(
                    containerColor = if (responseList.contains(label)) Purple80 else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    if (responseList.contains(label)) Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.padding(3.dp)
            )
            // println("response list is $responseList")
            Log.d("User", "response list is $responseList")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IncludeChip3(
    list: List<Pair<String, ImageVector>>, // Change the parameter type to List<Pair<String, ImageVector>>
    tempList: List<Pair<String, ImageVector>>, // Change the parameter type to List<Pair<String, ImageVector>>
    onChipSelected: (List<Pair<String, ImageVector>>, List<Pair<String, ImageVector>>) -> Unit
) {
    var responseList by rememberSaveable {
        mutableStateOf(tempList)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        maxItemsInEachRow = 2
    ) {
        list.forEachIndexed { index, pair ->
            val label = pair.first
            val icon = pair.second

            InputChip(
                selected = responseList.contains(pair),
                onClick = {
                    responseList = if (responseList.contains(pair)) {
                        responseList - pair
                    } else {
                        responseList + pair
                    }
                    onChipSelected(responseList, tempList)
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                border = InputChipDefaults.inputChipBorder(
                    borderColor = if (!responseList.contains(pair)) PurpleGrey40 else Color.Transparent,
                    borderWidth = if (responseList.contains(pair)) 0.dp else 2.dp
                ),
                colors = InputChipDefaults.inputChipColors(
                    containerColor = if (responseList.contains(pair)) Purple80 else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    if (responseList.contains(pair)) {
                        Icon(
                            icon,
                            contentDescription = ""
                        )
                    } else {
                        null
                    }
                }
            )
            Log.d("User", "response list is $responseList")
        }
    }
}
