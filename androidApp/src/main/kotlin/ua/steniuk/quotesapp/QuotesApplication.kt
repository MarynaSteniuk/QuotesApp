package ua.steniuk.quotesapp

import android.app.Application
import ua.steniuk.quotesapp.di.initKoin

class QuotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}