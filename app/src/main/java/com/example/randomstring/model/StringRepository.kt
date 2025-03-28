package com.example.randomstring.model

import android.content.Context
import android.net.Uri
import androidx.room.Room.databaseBuilder
import com.example.randomstring.database.AppDatabase
import com.example.randomstring.database.StringDao
import com.example.randomstring.database.StringEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class StringRepository(context: Context) {

    private var db: AppDatabase = databaseBuilder(
        context.applicationContext, AppDatabase::class.java, "string_db"
    ).build()


    private var dao: StringDao = db.stringDao()


    fun getRandomString(context: Context, length: Int): RandomString? {
        val uri = Uri.parse("content://com.iav.contestdataprovider/text")
        val selectionArgs = arrayOf(length.toString())
        val cursor = context.contentResolver.query(uri, null, null, selectionArgs, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val json = it.getString(it.getColumnIndexOrThrow("data"))
                val jsonObject = JSONObject(json)
                val randomText = jsonObject.getJSONObject("randomText")

                return RandomString(
                    randomText.getString("value"),
                    randomText.getInt("length"),
                    randomText.getString("created")
                )
            }
        }
        return null
    }

    suspend fun insertString(string: RandomString) {
        withContext(Dispatchers.IO) {
            dao.insert(
                StringEntity(
                    value = string.value, length = string.length, created = string.created
                )
            )
        }
    }

    suspend fun getAllStrings(): List<StringEntity> {
        return withContext(Dispatchers.IO) { dao.getAllStrings() }
    }

    suspend fun deleteAllStrings() {
        withContext(Dispatchers.IO) { dao.deleteAll() }
    }

    suspend fun deleteString(string: StringEntity) {
        withContext(Dispatchers.IO) { dao.deleteString(string) }
    }
}