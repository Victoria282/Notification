package ru.unit6.course.android.retrofit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.unit6.course.android.retrofit.data.api.ApiHelper
import ru.unit6.course.android.retrofit.data.database.AppDatabase
import ru.unit6.course.android.retrofit.data.model.User

class MainRepository(
    private val apiHelper: ApiHelper,
    appDatabase: AppDatabase?
) {

    private val userDao = appDatabase?.userDao()

    suspend fun getUsers(): List<User>? = withContext(Dispatchers.IO) {
        val apiResult = apiHelper.getUsers()
        userDao?.insertAllUsers(apiResult)

        userDao?.getAllUsers()
    }

    suspend fun getUser(id: String): User? = withContext(Dispatchers.IO) { userDao?.getUser(id) }
}