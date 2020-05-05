package by.ciszkin.basicapp.ui.fragments.newjob

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource
import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType
import by.ciszkin.basicapp.model.enums.Units
import kotlin.collections.ArrayList

class NewJobViewModel : ViewModel() {

    val jobName = MutableLiveData("")
    val jobPrice = MutableLiveData(0.0)
    val jobSurface = MutableLiveData("")
    val jobType = MutableLiveData("")
    val jobUnits = MutableLiveData("")
    val surfaceList = JobSurface.values()
    val typeList = JobType.values()
    val unitsList = Units.values()
    val consumptionList = MutableLiveData(ArrayList<Pair<RawResource, Double>>())

    private val resourceObserver = Observer<Pair<RawResource, Double>> {
        val resList = consumptionList.value

        if (it != null) {
            resList?.add(it)
            consumptionList.postValue(resList)
            clearCurrentRawResource()
        }
    }

    init {
        RawResource.current.observeForever(resourceObserver)
    }

    private fun clearCurrentRawResource() {
        RawResource.current.value = null
    }

    fun createNewJob() {
        val objectId = "user_defined_job_" + System.currentTimeMillis()
        RawJob.list.add(RawJob(
            objectId,
            jobName.value!!,
            consumptionList.value as List<Pair<RawResource, Double>>,
            JobSurface.getInstanceByTitle(jobSurface.value!!)!!,
            JobType.getInstanceByTitle(jobType.value!!)!!,
            Units.getInstanceByTitle(jobUnits.value!!)!!,
            listOf(""),
            jobPrice.value!!
            ))
        RawResource.needToAddResourceToNewJob = false
    }

    override fun onCleared() {
        RawResource.current.removeObserver(resourceObserver)
        super.onCleared()
    }

    fun setResourceNeed() {
        RawResource.needToAddResourceToNewJob = true
    }
}
