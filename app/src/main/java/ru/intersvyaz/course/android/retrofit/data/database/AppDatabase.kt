package ru.intersvyaz.course.android.retrofit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.intersvyaz.course.android.retrofit.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "course"

        fun invoke(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                }
            }

            return INSTANCE!!
        }

        fun getDatabase() = INSTANCE!!
    }

}