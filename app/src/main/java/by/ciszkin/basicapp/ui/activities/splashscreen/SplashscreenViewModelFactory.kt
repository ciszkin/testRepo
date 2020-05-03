package by.ciszkin.basicapp.ui.activities.splashscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.ciszkin.basicapp.model.EstimateRepository

class SplashscreenViewModelFactory(
    private val application: Application,
    private val repository: EstimateRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) return SplashscreenViewModel(
            application,
            repository
        ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}