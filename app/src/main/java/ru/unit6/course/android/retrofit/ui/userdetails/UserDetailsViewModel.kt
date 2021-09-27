package ru.unit6.course.android.retrofit.ui.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.unit6.course.android.retrofit.data.api.ApiHelper
import ru.unit6.course.android.retrofit.data.api.RetrofitBuilder
import ru.unit6.course.android.retrofit.data.database.AppDatabase
import ru.unit6.course.android.retrofit.data.model.User
import ru.unit6.course.android.retrofit.data.repository.MainRepository

class UserDetailsViewModel: ViewModel() {

    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)
    private val appDatabase = AppDatabase.getDatabase()
    private val mainRepository: MainRepository = MainRepository(apiHelper, appDatabase)
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    fun loadUserInfo(id: String) = viewModelScope.launch {
        val result = mainRepository.getUser(id)
        _user.postValue(result)
    }
}