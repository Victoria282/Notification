package ru.unit6.course.android.retrofit.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.unit6.course.android.retrofit.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>?

    @Query("SELECT * FROM users WHERE id =:id")
    suspend fun getUser(id: String) : User?
}