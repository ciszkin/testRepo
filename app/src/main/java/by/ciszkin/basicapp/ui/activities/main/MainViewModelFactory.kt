package by.ciszkin.basicapp.ui.activities.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.ciszkin.basicapp.model.EstimateRepository

class MainViewModelFactory(
    private val application: Application,
    private val repository: EstimateRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(
            application,
            repository
        ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}