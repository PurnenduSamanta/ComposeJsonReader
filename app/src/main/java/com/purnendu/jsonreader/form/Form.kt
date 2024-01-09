package com.purnendu.jsonreader.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Form(viewModel: FormViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val name = remember { mutableStateOf("") }
    val mobile = remember { mutableStateOf("") }
    val id = remember { mutableStateOf("") }
    val dataList= viewModel.getData().collectAsState(initial = emptyList()).value
    val isOperationHappening = remember { mutableStateOf(false) }
    val isProgressIndicatorVisible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val ifAddItemDialogBoxVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.response.value)
    {
        when (val data = viewModel.response.value) {
            is Response.Loading -> { isProgressIndicatorVisible.value = true }

            is Response.Success -> { isProgressIndicatorVisible.value = false }

            is Response.Error -> { isProgressIndicatorVisible.value = false }

            is Response.Empty -> {}
        }

    }

    if (ifAddItemDialogBoxVisible.value) {
        AddItemDialogBox(
            name = name.value,
            mobile = mobile.value,
            onNameChange = { name.value = it },
            onMobileNoChange = { mobile.value = it },
            onSaveClick = {
                scope.launch {
                    viewModel.save(id.value,name.value, mobile.value)
                    ifAddItemDialogBoxVisible.value = false
                    isOperationHappening.value=!isOperationHappening.value
                    name.value=""
                    mobile.value=""
                    id.value=""
                }
            }) {
            ifAddItemDialogBoxVisible.value = !ifAddItemDialogBoxVisible.value
        }
    }


    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(color = Color.Green.copy(alpha = 0.7f))
                    .clickable { ifAddItemDialogBoxVisible.value = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = "AddIcons"
                )
            }


        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {



        if(isProgressIndicatorVisible.value)
        {
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator()
            }

            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(10.dp)
        )
        {
            items(dataList.toList().reversed())
            {
                MyItem(name = it.name, mobile = it.mobile, onDeleteClick = {
                    scope.launch { viewModel.deleteById(it.id) }
                    isOperationHappening.value=!isOperationHappening.value
                }) {
                    id.value=it.id
                    ifAddItemDialogBoxVisible.value=true
                    name.value=it.name
                    mobile.value=it.mobile
                }
                
                Spacer(modifier = Modifier.height(5.dp))
            }
        }


    }


}

@Composable
fun MyItem(name: String, mobile: String, onDeleteClick: () -> Unit, onUpdateClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(text = name)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = mobile)
        }

        Column {

            Icon(
                modifier = Modifier.clickable { onDeleteClick() },
                imageVector = Icons.Default.Delete,
                contentDescription = "deleteIcon",
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(5.dp))

            Icon(
                modifier = Modifier.clickable { onUpdateClick() },
                imageVector = Icons.Default.Edit,
                contentDescription = "editIcon",
                tint = Color.White
            )
        }


    }


}