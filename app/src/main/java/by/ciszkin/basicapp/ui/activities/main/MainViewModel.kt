package by.ciszkin.basicapp.ui.activities.main

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.Repository
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.model.EstimateRepository
import by.ciszkin.basicapp.model.RawJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application, private val repository: EstimateRepository) : AndroidViewModel(application) {

//    val currentRawJob = MutableLiveData<RawJob>()
//    val currentEstimateJob = MutableLiveData<EstimateJob>()
//    val currentEstimate = MutableLiveData<Estimate>()

    val currentFragmentName = MutableLiveData(R.string.estimates_list_menu_item_title)
    val currentFabVisibility = MutableLiveData(View.VISIBLE)
    val currentFabIcon = MutableLiveData(R.drawable.ic_new_estimate_icon)


    fun saveDataToDb() = CoroutineScope(Dispatchers.IO).launch {
        val context = getApplication<Application>().applicationContext
        repository.saveEstimates(context, Estimate.list)
    }
}