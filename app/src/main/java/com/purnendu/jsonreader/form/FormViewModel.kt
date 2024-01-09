package com.purnendu.jsonreader.form

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID
import kotlin.random.Random


class FormViewModel(private val  application: Application):AndroidViewModel(application) {

    private val _response: MutableState<Response> = mutableStateOf(Response.Empty)
    val response get() = _response

    suspend fun save(id: String, name: String, mobile: String) {
        _response.value = Response.Loading
        val jsonObject = JSONObject()
        jsonObject.put("Id", id.ifEmpty { UUID.randomUUID().toString() })
        jsonObject.put("Name", name)
        jsonObject.put("Mobile", mobile)

        val file = File(application.filesDir, "my_json")
        val jsonArray = JSONArray()

        // Read existing data, if any, from the file
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                val existingArray = JSONArray(jsonString)

                // Check if ID is not empty
                if (id.isNotEmpty()) {
                    var found = false
                    for (i in 0 until existingArray.length()) {
                        val obj = existingArray.getJSONObject(i)
                        if (obj.getString("Id") == id) {
                            // Update the existing object
                            existingArray.put(i, jsonObject)
                            found = true
                            break
                        }
                    }
                    // If not found, add the new object
                    if (!found) {
                        existingArray.put(jsonObject)
                    }
                } else {
                    // If ID is empty, add the new object directly
                    existingArray.put(jsonObject)
                }

                // Copy the updated array to jsonArray
                for (i in 0 until existingArray.length()) {
                    jsonArray.put(existingArray.getJSONObject(i))
                }
            }
        } else {
            // If the file doesn't exist or is empty, add the new object directly
            jsonArray.put(jsonObject)
        }

        // Write the updated JSON array back to the file
        withContext(Dispatchers.IO) {
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(jsonArray.toString())
            bufferedWriter.close()
        }
        _response.value = Response.Success("Success")
    }

    fun getData(): Flow<List<MyModel>> {
        _response.value = Response.Loading
        val file = File(application.filesDir, "my_json")
        val list = mutableListOf<MyModel>()

        if (file.exists()) {
            val jsonString = file.readText()
            if(jsonString.isEmpty())
                return emptyFlow()
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("Id")
                val name = jsonObject.getString("Name")
                val mobile = jsonObject.getString("Mobile")
                list.add(MyModel(id,name, mobile))
            }
        }
        _response.value = Response.Success("Success")
        return flowOf(list.toList())
    }

    suspend fun deleteById(id: String) {
        _response.value = Response.Loading
        val file = File(application.filesDir, "my_json")
        val jsonArray = JSONArray()

        // Read existing data from the file
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                val existingArray = JSONArray(jsonString)

                // Find and remove the object by ID
                for (i in 0 until existingArray.length()) {
                    val obj = existingArray.getJSONObject(i)
                    if (obj.getString("Id") == id) {
                        existingArray.remove(i)
                        break
                    }
                }

                // Copy the updated array to jsonArray
                for (i in 0 until existingArray.length()) {
                    jsonArray.put(existingArray.getJSONObject(i))
                }
            }
        }

        // Write the updated JSON array back to the file
        withContext(Dispatchers.IO) {
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(jsonArray.toString())
            bufferedWriter.close()
        }
        _response.value = Response.Success("Success")
    }

    fun generateMobileNumber(): String {
        val prefix = listOf("6", "7", "8", "9")
        val randomPrefix = prefix[Random.nextInt(prefix.size)]

        val randomNumber = StringBuilder()
        randomNumber.append(randomPrefix)

        for (i in 1..9) {
            randomNumber.append(Random.nextInt(0, 10))
        }

        return randomNumber.toString()
    }

    fun generateIndianName(): String {
        val firstNameList = listOf("Raj", "Amit", "San", "Priya", "Anu", "Deep", "Kav", "Vijay", "Neha", "Kamal")
        val lastNameList = listOf("Singh", "Kumar", "Sharma", "Patel", "Gupta", "Reddy", "Das", "Jha", "Rao", "Devi")
        val firstName = firstNameList.random()
        val lastName = lastNameList.random()

        return "$firstName $lastName"
    }

    data class MyModel(val id:String,val name: String, val mobile: String)
}