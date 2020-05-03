package by.ciszkin.basicapp.model

import androidx.lifecycle.MutableLiveData
import by.ciszkin.basicapp.model.enums.ResType

class Estimate() {

//    interface OnAmountChangeListener {
//        fun onAmountChange()
//    }

    constructor(
        objectId: String,
        title: String,
        deadline: Long
    ) : this() {
        this.objectId = objectId
        this.title = title
        this.deadline = deadline
    }


    private lateinit var objectId: String
    lateinit var title: String
    var deadline = 0L
    var note = ""
    var created = 0L

    private var totalCost = 0.0
    private var completedTotalCost = 0.0
    private var resourcesCost = 0.0
    private var completedResourcesCost = 0.0
    private var laborCost = 0.0
    private var completedLaborCost = 0.0
    private val estimateJobsList = ArrayList<EstimateJob>()
    private val estimateResourcesList = ArrayList<EstimateResource>()

    fun addJob(job: EstimateJob) {
        estimateJobsList.add(job)
        addResources(job)
        update()
    }

    fun addJobs(jobsList: List<EstimateJob>) {
        estimateJobsList.addAll(jobsList)
        jobsList.forEach {job ->
            addResources(job)
        }
        update()
    }

    fun setJobPrice(job: RawJob, newPrice: Double) {
        val jobToEdit = estimateJobsList.find{
            it.job == job
        }
        jobToEdit?.estimatePrice = newPrice
        update()
    }

//    fun setJobsPriceList(priceList: ArrayList<Pair<RawJob, Double>>) {
//        jobsPriceList.clear()
//        jobsPriceList.addAll(priceList)
//    }

    fun setResourcePrice(resource: RawResource, newPrice: Double) {
        val resourceToEdit = estimateResourcesList.find{
            it.resource == resource
        }
        resourceToEdit?.estimatePrice = newPrice
        update()
    }

    fun setResourcesPriceList(priceList: ArrayList<Pair<RawResource, Double>>) {
        priceList.forEach {
            setResourcePrice(it.first, it.second)
        }
    }

    private fun addResources(estimateJob: EstimateJob) {
        estimateJob.job.resourcesConsumptionList.forEach { consumption ->
            val selectedEstimateResource = estimateResourcesList.find {
                it.resource == consumption.first
            }
            if (selectedEstimateResource != null) {
                selectedEstimateResource.jobsList.add(estimateJob)
                selectedEstimateResource.amount += consumption.second * estimateJob.amount
                selectedEstimateResource.completedAmount += consumption.second * estimateJob.completedAmount
            } else {
                estimateResourcesList.add(
                    EstimateResource(
                        consumption.first,
                        mutableListOf(estimateJob),
                        consumption.second * estimateJob.amount,
                        consumption.second * estimateJob.completedAmount,
                        consumption.first.price
                    )
                )
            }
        }
    }


    fun removeJob(job: EstimateJob) {
        if (estimateJobsList.contains(job)) {
            estimateJobsList.remove(job)
            removeResources(job)
            update()
        }
    }

    private fun removeResources(estimateJob: EstimateJob) {
        estimateJob.job.resourcesConsumptionList.forEach { consumption ->
            val selectedEstimateResource = estimateResourcesList.find {
                it.resource == consumption.first
            }
            if (selectedEstimateResource != null) {
                selectedEstimateResource.jobsList.remove(estimateJob)
                selectedEstimateResource.amount -= consumption.second * estimateJob.amount
                selectedEstimateResource.completedAmount -= consumption.second * estimateJob.completedAmount

                if(selectedEstimateResource.amount == 0.0) estimateResourcesList.remove(selectedEstimateResource)
            }
        }
    }

    fun getObjectId() = objectId

    fun getJobsCount() = estimateJobsList.size

    fun getResourcesCount() = estimateResourcesList.size


    fun getJobsList(): List<EstimateJob> {
        return estimateJobsList
    }

    fun getResourcesList() : List<EstimateResource> {
        return estimateResourcesList
    }

    fun getTotalCost(): Double {
        return totalCost
    }

    fun getCompletedCost(): Double {
        return completedTotalCost
    }

    fun getLaborCost(): Double {
        return laborCost
    }

    fun getCompletedLaborCost(): Double {
        return completedLaborCost
    }

    fun getResourcesCost(): Double {
        return resourcesCost
    }

    fun getCompletedResourcesCost(): Double {
        return completedResourcesCost
    }

    fun update() {
        totalCost = 0.0
        completedTotalCost = 0.0
        resourcesCost = 0.0
        completedResourcesCost = 0.0
        laborCost = 0.0
        completedLaborCost = 0.0

        estimateJobsList.forEach { estimateJob ->
            laborCost += estimateJob.amount * estimateJob.estimatePrice
            completedLaborCost += estimateJob.completedAmount * estimateJob.estimatePrice
        }

        estimateResourcesList.forEach { estimateResource ->
            estimateResource.completedAmount = 0.0
            estimateResource.jobsList.forEach {
                estimateResource.completedAmount += it.completedAmount * it.job.resourcesConsumptionList.find { consumption ->
                    consumption.first == estimateResource.resource
                }?.second!!
            }

            resourcesCost += estimateResource.amount * estimateResource.estimatePrice
            completedResourcesCost += estimateResource.completedAmount * estimateResource.estimatePrice
        }

        totalCost = laborCost + resourcesCost
        completedTotalCost = completedLaborCost + completedResourcesCost

    }

    companion object {

        val list = ArrayList<Estimate>()
        var current: Estimate? = null

        fun getTotalJobCostPerUnit(job: EstimateJob): Double {

            return job.estimatePrice + getJobsResourcesCostPerUnit(job)
        }

        private fun getJobsResourcesCostPerUnit(job: EstimateJob): Double {
            var result = 0.0
            job.job.resourcesConsumptionList.forEach { consumption ->

                val estimateResource = current?.getResourcesList()?.find{estimateResource ->
                    estimateResource.resource == consumption.first
                }

                result += if(estimateResource?.resource?.type == ResType.MATERIAL){
                    estimateResource.estimatePrice * consumption.second
                } else {
                    estimateResource?.estimatePrice ?: 0.0
                }
            }
            return result
        }
    }

    enum class SortBy (private val title: String) {
        COST_LOW("Сначала дешевые"),
        COST_HIGH("Сначала дорогие"),
        NAME_AZ("По названию А-Я"),
        NAME_ZA("По названию Я-А"),
        COMPLETION_LOW("Сначала незавершенные"),
        COMPLETION_HIGH("Сначала завершенные"),
        DEADLINE_CLOSE("Сначала срочные"),
        DEADLINE_FAR("Сначала несрочные");

        override fun toString(): String {
            return title
        }
    }

//    override fun onAmountChange() {
//        Log.e("Estimate", "I'm here and i start to update! ")
//        update()
//    }
}