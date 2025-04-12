package com.motorro.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.network.data.UserListItem
import com.motorro.network.net.usecase.GetUserList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListModel(
    private val getUserList: GetUserList
): ViewModel() {

    private val _users = MutableStateFlow<List<UserListItem>>(emptyList())
    val users: StateFlow<List<UserListItem>> get() = _users.asStateFlow()

    init {
        viewModelScope.launch {
            _users.value = loadUserList()
        }
    }

    fun deleteUser(userId: Int) {

    }

    private suspend fun loadUserList(): List<UserListItem> {
        return getUserList()
            .onFailure { Log.e(TAG, "Failed to load users: $it") }
            .getOrDefault(emptyList())
    }

    companion object {
        const val TAG = "UserListModel"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserListModel::class.java)) {
                return UserListModel(app.getUserList) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}