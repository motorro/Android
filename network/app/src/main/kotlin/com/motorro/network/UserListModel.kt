package com.motorro.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import com.motorro.network.data.CreateUserData
import com.motorro.network.data.Phone
import com.motorro.network.data.Profile
import com.motorro.network.data.UserListItem
import com.motorro.network.net.usecase.CreateUser
import com.motorro.network.net.usecase.GetUserList
import com.motorro.network.session.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URI
import kotlin.random.Random
import kotlin.time.Instant

class UserListModel(
    private val sessionManager: SessionManager,
    private val getUserList: GetUserList,
    private val createUser: CreateUser
): ViewModel() {

    private val refresh = MutableSharedFlow<Unit>()

    val users: StateFlow<List<UserListItem>> get() = sessionManager.loggedIn
        .combine(refresh.onStart { emit(Unit) }) { _, _ -> }
        .map { loadUserList() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    val addEnabled: StateFlow<Boolean> = sessionManager.loggedIn

    fun createUser(data: CreateUserData) = viewModelScope.launch {
        Log.d(TAG, "Creating user: $data")

        val faker = Faker()

        val profile = Profile(
            userId = 0,
            name = data.name,
            userpic = URI("https://picsum.photos/id/${Random.nextInt(1, 60)}/200/200"),
            registered = Instant.DISTANT_PAST,
            age = Random.nextInt(18, 99).toInt(),
            phone = Phone(
                countryCode = 7,
                number = faker.phoneNumber().phoneNumber()
            ),
            interests = setOf(
                faker.food().dish(),
                faker.food().fruit(),
                faker.food().vegetable()
            )
        )

        createUser(profile)
            .onFailure { Log.e(TAG, "Failed to create user: $it") }
            .onSuccess {
                Log.d(TAG, "User created: $it")
                refresh.emit(Unit)
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
                return UserListModel(
                    app.sessionManager,
                    app.getUserList,
                    app.createUser
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}