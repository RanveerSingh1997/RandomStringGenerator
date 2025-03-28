package com.example.randomstring.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomstring.database.StringEntity
import com.example.randomstring.model.StringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private lateinit var repository: StringRepository

    private val _allStrings = MutableStateFlow<List<StringEntity>>(emptyList())
    val allStrings: StateFlow<List<StringEntity>> = _allStrings


    fun initialize(context: Context) {
        repository = StringRepository(context)
        loadStrings()
    }

    fun fetchRandomString(context: Context, length: Int) {
        viewModelScope.launch {
            val randomString = repository.getRandomString(context, length)
            randomString?.let {
                repository.insertString(it)
                loadStrings()
            }
        }
    }

    private fun loadStrings() {
        viewModelScope.launch {
            _allStrings.value = repository.getAllStrings()
        }
    }

    fun deleteAllStrings() {
        viewModelScope.launch {
            repository.deleteAllStrings()
            loadStrings()
        }
    }

    fun deleteString(string: StringEntity) {
        viewModelScope.launch {
            repository.deleteString(string)
            loadStrings()
        }
    }
}