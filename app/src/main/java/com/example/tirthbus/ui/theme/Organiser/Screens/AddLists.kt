package com.example.tirthbus.ui.theme.Organiser.Screens

import android.util.Log
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    onIncludesSelected: (List<String?>, List<String?>) -> Unit,
    onRuleSelected: (List<String?>, List<String?>) -> Unit,
    modifier: Modifier = Modifier){
    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Text(
            text = "यात्रा में शामिल हैं:-",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        IncludeChip(list = includesList,
            tempList = includestempList ,
            onChipSelected = onIncludesSelected )

        IncludeChip(list = rulesList, tempList = rulestempList, onChipSelected = onRuleSelected)
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
                }
            )
            // println("response list is $responseList")
            Log.d("User","response list is $responseList")
        }
    }
}