package by.ciszkin.basicapp.ui.fragments.jobdetailed

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource

class JobDetailedViewModel : ViewModel() {

    lateinit var jobName: String
    var jobTypeIcon = 0
    lateinit var jobTypeTitle: String
    var jobSurfaceIcon = 0
    lateinit var jobSurfaceTitle: String
    var jobPrice = 0.0
    lateinit var unitsTitle: String
    lateinit var consumptionList: MutableList<Pair<RawResource, Double>>
    lateinit var workflow: List<String>

    var currentEstimateTitle = Estimate.current?.title

    private val rawJobObserver = Observer<RawJob> {
        jobName = it.name
        jobTypeIcon = it.type.icon
        jobTypeTitle = it.type.title
        jobSurfaceIcon = it.surface.icon
        jobSurfaceTitle = it.surface.title
        jobPrice = it.price
        unitsTitle = it.units.title
        consumptionList = it.resourcesConsumptionList as MutableList<Pair<RawResource, Double>>
        workflow = it.workflow
    }

//    private val estimateObserver = Observer<Estimate>{
//        currentEstimateTitle = it.title
//    }

    init {
        RawJob.current.observeForever(rawJobObserver)
    }

    override fun onCleared() {
        super.onCleared()
        RawJob.current.removeObserver(rawJobObserver)
    }

    fun isEstimateSelected() = Estimate.current != null

    fun addJobToEstimate(editAmount: String) {
        RawJob.current.value?.let {
            Estimate.current?.addJob(
                EstimateJob(
                    it,
                    editAmount.toDouble(),
                    0.0,
                    "",
                    it.price
                )
            )
        }
    }

}