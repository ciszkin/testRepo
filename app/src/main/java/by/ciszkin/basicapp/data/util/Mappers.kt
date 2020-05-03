package by.ciszkin.basicapp.data.util

import by.ciszkin.basicapp.data.Repository
import by.ciszkin.basicapp.data.db.entity.DbEstimate
import by.ciszkin.basicapp.data.db.entity.DbJob
import by.ciszkin.basicapp.data.db.entity.DbResource
import by.ciszkin.basicapp.data.networking.responses.ErrorResponse
import by.ciszkin.basicapp.data.networking.responses.NetJob
import by.ciszkin.basicapp.data.networking.responses.NetResource
import by.ciszkin.basicapp.model.*
import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units
import com.google.gson.Gson
import okhttp3.ResponseBody
import kotlin.text.StringBuilder

object Mappers {

    fun mapEstimateToDbEstimate(estimates: List<Estimate>) : List<DbEstimate>{
        val result = ArrayList<DbEstimate>()

        estimates.forEach {
            result.add(
                DbEstimate(it.getObjectId(),
                it.created,
                it.title,
                it.deadline,
                it.note,
                getEstimateJobsListAsString(it.getJobsList()),
                getPriceListAsString(it.getResourcesList()))
            )
        }

        return result
    }

    private fun getPriceListAsString(resourcesList: List<EstimateResource>): String {
        val result = StringBuilder()

        resourcesList.forEach {
            result.apply{
                append(it.resource.objectId)
                append(" ")
                append(it.estimatePrice)
                append("\n")
            }
        }

        return result.toString()
    }

    private fun getEstimateJobsListAsString(jobsList: List<EstimateJob>): String {
        val result = StringBuilder()

        jobsList.forEach {
            result.apply{
                append(it.job.objectId)
                append(" ")
                append(it.amount)
                append(" ")
                append(it.completedAmount)
                append(" ")
                append(it.note)
                append(" ")
                append(it.estimatePrice)
                append("\n")
            }
        }

        return result.toString()
    }

    fun mapDbEstimateToEstimate(dbEstimates: List<DbEstimate>) : List<Estimate> {
        val result = ArrayList<Estimate>()

        dbEstimates.forEach {

            val currentEstimate = Estimate(
                it.objectId,
                it.title,
                it.deadline
                )

            val estimateJobsList = getStringAsEstimateJobsList(it.estimateJobsRecordsString)
            currentEstimate.apply{
                note = it.note
                created = it.created
                addJobs(estimateJobsList)
                setResourcesPriceList(getStringAsListOfPairs(it.priceString))
            }

            result.add(currentEstimate)
        }

        return result
    }

    private fun getStringAsListOfPairs(priceString: String): ArrayList<Pair<RawResource, Double>> {
        val result = ArrayList<Pair<RawResource, Double>>()
        val priceRecordsList = ArrayList(priceString.split("\n"))
        priceRecordsList.removeAt(priceRecordsList.lastIndex)

        priceRecordsList.forEach {priceRecord ->
            val values = priceRecord.split(" ")

            val resource = Repository.getResourceById(values[0])
            val price = values[1].toDouble()

            result.add(Pair(resource, price))
        }

        return result
    }

    private fun getStringAsEstimateJobsList(estimateJobsRecordsString: String) : List<EstimateJob> {
        val result = ArrayList<EstimateJob>()
        val jobsRecordsList = ArrayList(estimateJobsRecordsString.split("\n"))
        jobsRecordsList.removeAt(jobsRecordsList.lastIndex)

        jobsRecordsList.forEach {jobRecord ->
            val values = jobRecord.split(" ")


            val rawJob = Repository.getJobById(values[0])
            val amount = values[1].toDouble()
            val completedAmount = values[2].toDouble()
            val note = values[3]
            val estimatePrice = values[4].toDouble()

            result.add(EstimateJob(rawJob, amount, completedAmount, note, estimatePrice))
        }

        return result
    }

    fun mapDbToRawResource(dbResources: List<DbResource>?): List<RawResource> {
        val result = ArrayList<RawResource>()

        dbResources?.forEach {
            result.add(
                RawResource(
                    it.objectId,
                    it.name,
                    getResType(it.type),
                    getUnits(it.units),
                    it.price
                )
            )
        }

        return result
    }

    private fun getUnits(units: Int): Units = when (units) {
        Units.KG.ordinal -> Units.KG
        Units.L.ordinal -> Units.L
        Units.M.ordinal -> Units.M
        Units.M2.ordinal -> Units.M2
        else -> Units.PIECE
    }

    private fun getResType(type: Int): ResType = when (type) {
        ResType.MATERIAL.ordinal -> ResType.MATERIAL
        else -> ResType.TOOL
    }

    fun mapDbToRawJob(dbJobs: List<DbJob>?): List<RawJob> {
        val result = ArrayList<RawJob>()

        dbJobs?.forEach {
            result.add(
                RawJob(
                    it.objectId,
                    it.name,
                    getResourceConsumptionAsListOfPairs(it.resources, it.resourcesRates),
                    getSurface(it.surface),
                    getJobType(it.type),
                    getUnits(it.units),
                    it.workflow.split("\n"),
                    it.price
                )
            )
        }

        return result
    }

    private fun getJobType(type: Int): JobType = when (type) {
        JobType.PAINTING.ordinal -> JobType.PAINTING
        JobType.TILING.ordinal -> JobType.TILING
        JobType.PASTING.ordinal -> JobType.PASTING
        JobType.FLATTENING.ordinal -> JobType.FLATTENING
        JobType.MOUNTING.ordinal -> JobType.MOUNTING
        else -> JobType.OTHER
    }

    private fun getSurface(surface: Int): JobSurface = when (surface) {
        JobSurface.FLOOR.ordinal -> JobSurface.FLOOR
        JobSurface.WALLS.ordinal -> JobSurface.WALLS
        JobSurface.CEILING.ordinal -> JobSurface.CEILING
        JobSurface.OPENING.ordinal -> JobSurface.OPENING
        else -> JobSurface.ALL
    }

    private fun getResourceConsumptionAsListOfPairs(res: String, rate: String): List<Pair<RawResource, Double>> {
        val result = ArrayList<Pair<RawResource, Double>>()

        val resources = res.split("\n")
        val resRate = rate.split("\n")

        for (index: Int in resources.indices) {
            result.add(
                Pair(
                    Repository.getResourceById(resources[index]),
                    resRate[index].toDouble()
                )
            )
        }

        return result
    }

    private fun getResourceConsumptionAsPairOfLists(res: String, rate: String): Pair<List<RawResource>, List<Double>> {
        val result = Pair(ArrayList<RawResource>(), ArrayList<Double>())

        val resources = res.split("\n")
        val resRate = rate.split("\n")

        for (index: Int in resources.indices) {
            result.first.add(index,Repository.getResourceById(resources[index]))
            result.second.add(resRate[index].toDouble())
        }

        return result
    }

    fun mapNetToDbResource(resourceResponse: List<NetResource>?): List<DbResource> {
        val result = ArrayList<DbResource>()

        resourceResponse?.forEach {
            result.add(
                DbResource(
                    it.name,
                    it.objectId,
                    it.type,
                    it.units,
                    0.0,
                    if (it.updated == 0L) it.created else it.updated
                )
            )
        }

        return result
    }

    fun mapNetToDbJob(jobResponse: List<NetJob>?): List<DbJob> {
        val result = ArrayList<DbJob>()

        jobResponse?.forEach {

            val resourceConsumptionAsStrings = getResourceConsumptionListAsPairOfStrings(it.resources)

            result.add(
                DbJob(
                    it.created,
                    it.name,
                    it.objectId,
                    resourceConsumptionAsStrings.first,
                    resourceConsumptionAsStrings.second,
                    it.surface,
                    it.type,
                    it.units,
                    it.updated,
                    it.workflow,
                    0.0
                )
            )
        }

        return result
    }

    private fun getResourceConsumptionListAsPairOfStrings(resourceConsumptions: List<NetJob.ResourceConsumption>): Pair<String, String> {
        val result1 = StringBuilder()
        val result2 = StringBuilder()

        resourceConsumptions.forEach {
            result1.append(it.resource).append("\n")
            result2.append(it.rate).append("\n")
        }

        result1.deleteCharAt(result1.lastIndex)
        result2.deleteCharAt(result2.lastIndex)

        return (result1.toString()) to (result2.toString())
    }

    fun mapErrorBodyToErrorResponse(errorBody: ResponseBody?) : ErrorResponse {
        val gson = Gson()

        return gson.fromJson(errorBody?.string(), ErrorResponse::class.java)
    }
}