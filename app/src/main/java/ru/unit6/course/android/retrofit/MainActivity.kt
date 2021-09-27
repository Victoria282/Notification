package ru.unit6.course.android.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import ru.unit6.course.android.retrofit.data.database.AppDatabase
import ru.unit6.course.android.retrofit.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        AppDatabase.invoke(applicationContext)
    }
}