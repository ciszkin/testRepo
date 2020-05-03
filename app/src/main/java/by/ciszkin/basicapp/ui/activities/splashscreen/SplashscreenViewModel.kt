package by.ciszkin.basicapp.ui.activities.splashscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import by.ciszkin.basicapp.data.UserService
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateRepository
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashscreenViewModel(application: Application, private val repository: EstimateRepository) : AndroidViewModel(application) {

    val isLoading = MutableLiveData(true)
    val isDataLoaded = MutableLiveData(false)
    val errorMessage = MutableLiveData<String>()
    var autoFill = false

    fun loginUserWithToken() = CoroutineScope(Dispatchers.IO).launch {
        val context = getApplication<Application>().applicationContext
        val needToRegister = UserService.loginWithToken(context)
        if (needToRegister == null) {
            loadData()
        } else {
           if(needToRegister.code == 100100) {
               autoFill = true
           }
            errorMessage.postValue(needToRegister.message)
            isLoading.postValue(false)
        }
    }

    fun loginUser(email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        val context = getApplication<Application>().applicationContext
        val needToRegister = UserService.login(context, email, password)
        if (needToRegister == null) {
            loadData()
        } else {
            errorMessage.postValue(needToRegister.message)
            isLoading.postValue(false)
        }
    }

    private fun loadData() {
        val context = getApplication<Application>().applicationContext

        isLoading.postValue(true)

        val loading = repository.loadDataAsync(context)
        CoroutineScope(Dispatchers.IO).launch {
            loading.await()

            isDataLoaded.postValue(true)

            if (RawResource.list.isEmpty()) RawResource.list.addAll(repository.getRawResources())
            if (RawJob.list.isEmpty()) RawJob.list.addAll(repository.getRawJobs())
            Log.e("MyDebug", "estimates list is empty: ${Estimate.list.isEmpty()}")
            if (Estimate.list.isEmpty()) Estimate.list.addAll(repository.getEstimates())
            Log.e("MyDebug", "Now estimates list is empty: ${Estimate.list.isEmpty()}")
        }
    }

    fun registerNewUser(name: String, email: String, password: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val context = getApplication<Application>().applicationContext
            val registrationError = UserService.register(
                context,
                name,
                email,
                password
            )

            if (registrationError == null) {
                loadData()
            } else {
                errorMessage.postValue(registrationError.message)
                isLoading.postValue(false)
            }
        }


}