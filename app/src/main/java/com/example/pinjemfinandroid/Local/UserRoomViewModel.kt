package com.example.pinjemfinandroid.Local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinjemfinandroid.Local.dao.UserDataDao
import com.example.pinjemfinandroid.Local.entity.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRoomViewModel @Inject  constructor(
    private val userDataDao: UserDataDao
) : ViewModel(){

    private val _users = MutableLiveData<List<UserData>>()
    val users: LiveData<List<UserData>> = _users

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = userDataDao.getAllUsers()
        }
    }

    fun saveUser(user: UserData) {
        viewModelScope.launch {
            userDataDao.insertUser(user)
            loadUsers() // refresh data
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            userDataDao.clearAll()
            loadUsers()
        }
    }
}