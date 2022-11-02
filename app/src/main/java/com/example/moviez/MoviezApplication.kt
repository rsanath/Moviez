package com.example.moviez

import android.app.Application
import com.example.moviez.data.AppDatabase

class MoviezApplication : Application() {
    val db: AppDatabase by lazy { AppDatabase.createDb(this) }
}