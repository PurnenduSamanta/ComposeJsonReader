package com.purnendu.jsonreader.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddItemDialogBox(
    name:String,
    mobile:String,
    onNameChange:(String)->Unit,
    onMobileNoChange:(String)->Unit,
    onSaveClick:()->Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = { onDismissRequest() })
    {

        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            TextField(
                label={Text("Name")},
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {onNameChange(it) })

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                label={Text("Mobile No")},
                modifier = Modifier.fillMaxWidth(),
                value = mobile,
                onValueChange = { onMobileNoChange(it) })

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(), onClick = { onSaveClick() }) {
                Text(text = "Save", color = Color.White)
            }


        }


    }

}
