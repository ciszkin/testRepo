package by.ciszkin.basicapp.data

import android.content.Context
import by.ciszkin.basicapp.data.db.AppDatabase
import by.ciszkin.basicapp.data.db.entity.Job
import by.ciszkin.basicapp.data.db.entity.Resource
//import by.ciszkin.basicapp.data.mappers.Mappers
import by.ciszkin.basicapp.data.networking.NetworkService
import by.ciszkin.basicapp.data.networking.responses.JobResponse
import by.ciszkin.basicapp.data.networking.responses.ResourceResponse
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.model.EstimateResource
import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units
import kotlinx.coroutines.*
import java.lang.StringBuilder

object Repository {

    val EMPTY_RESOURCE = EstimateResource("000000-resource", "Empty resource", ResType.MATERIAL, Units.PIECE, 0.0)
    val EMPTY_JOB = EstimateJob("000000-job", "Empty job", listOf(Pair(EMPTY_RESOURCE, 0.0)), JobSurface.ALL, JobType.OTHER, listOf("Empty"), 0.0)

    var jobs = ArrayList<EstimateJob>()
    var resources = ArrayList<EstimateResource>()

    private var dBJobs = ArrayList<Job>()
    private var dBResources = ArrayList<Resource>()

    private var resourcesLoading: kotlinx.coroutines.Job? = null
    private var jobsLoading: kotlinx.coroutines.Job? = null

    fun loadData(context: Context) = CoroutineScope(Dispatchers.IO).async {

        resourcesLoading = loadResourcesFromDb(context)
        jobsLoading = loadJobsFromDb(context)

        resourcesLoading?.join()
        jobsLoading?.join()

        if (dBResources.isEmpty()) downloadResources(context)
        if (dBJobs.isEmpty()) downloadJobs(context)

        if (resourcesLoading?.isActive == true) resourcesLoading?.join()
        if (jobsLoading?.isActive == true) jobsLoading?.join()

        resources = Mappers.mapDbToEstimateResource(dBResources) as ArrayList<EstimateResource>
        jobs = Mappers.mapDbToEstimateJob(dBJobs) as ArrayList<EstimateJob>

        return@async jobs.isNotEmpty() && resources.isNotEmpty()
    }

    private suspend fun downloadJobs(context: Context) {
        val jobsListResponse = NetworkService.getBackendlessApi().getJobsListAsync(100).await()
        if (jobsListResponse.isSuccessful) {
            AppDatabase(context).getJobDao()
                .addJobs(Mappers.mapNetToDbJob(jobsListResponse.body()))
            jobsLoading = loadJobsFromDb(context)
        }
    }

    private suspend fun downloadResources(context: Context) {
        val resourcesListResponse =
            NetworkService.getBackendlessApi().getResourcesListAsync(100).await()
        if (resourcesListResponse.isSuccessful) {
            AppDatabase(context).getResourceDao()
                .addResources(Mappers.mapNetToDbResource(resourcesListResponse.body()))
            resourcesLoading = loadResourcesFromDb(context)
        }
    }

    fun getJobByID(id: String): EstimateJob {
        var result: EstimateJob = EMPTY_JOB
        jobs.forEach {
            if (id == it.objectId) {
                result = it
                return@forEach
            }
        }
        return result
    }

    fun getResourceByID(id: String): EstimateResource {
        var result: EstimateResource = EMPTY_RESOURCE
        resources.forEach {
            if (id == it.objectId) {
                result = it
                return@forEach
            }
        }
        return result
    }

    private fun loadJobsFromDb(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        dBJobs = AppDatabase(context).getJobDao().getJobs() as ArrayList<Job>
    }

    private fun loadResourcesFromDb(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        dBResources =
            AppDatabase(context).getResourceDao().getResources() as ArrayList<Resource>
    }


    object Mappers {

        fun mapDbToEstimateResource(dbResources: List<Resource>?): List<EstimateResource> {
            val result = ArrayList<EstimateResource>()

            dbResources?.forEach {
                result.add(
                    EstimateResource(
                        it.objectId,
                        it.name,
                        when (it.type) {
                            ResType.MATERIAL.ordinal -> ResType.MATERIAL
                            else -> ResType.TOOL
                        },
                        when (it.units) {
                            Units.KG.ordinal -> Units.KG
                            Units.L.ordinal -> Units.L
                            Units.M.ordinal -> Units.M
                            Units.M2.ordinal -> Units.M2
                            else -> Units.PIECE
                        },
                        it.price
                    )
                )
            }

            return result
        }

        fun mapDbToEstimateJob(dbJobs: List<Job>?): List<EstimateJob> {
            val result = ArrayList<EstimateJob>()

            dbJobs?.forEach {
                result.add(
                    EstimateJob(
                        it.objectId,
                        it.name,
                        getResourceConsumptionAsPairList(it.resources, it.resourcesRates),
                        when (it.surface) {
                            JobSurface.FLOOR.ordinal -> JobSurface.FLOOR
                            JobSurface.WALLS.ordinal -> JobSurface.WALLS
                            JobSurface.CEILING.ordinal -> JobSurface.CEILING
                            JobSurface.OPENING.ordinal -> JobSurface.OPENING
                            else -> JobSurface.ALL
                        },
                        when (it.type) {
                            JobType.PAINTING.ordinal -> JobType.PAINTING
                            JobType.TILING.ordinal -> JobType.TILING
                            JobType.PASTING.ordinal -> JobType.PASTING
                            JobType.FLATTENING.ordinal -> JobType.FLATTENING
                            JobType.MOUNTING.ordinal -> JobType.MOUNTING
                            else -> JobType.OTHER
                        },
                        it.workflow.split("\n"),
                        it.price
                    )
                )
            }

            return result
        }

        private fun getResourceConsumptionAsPairList(res: String, rate: String): List<Pair<EstimateResource, Double>> {
            val result = ArrayList<Pair<EstimateResource, Double>>()

            val resources = res.split("\n")
            val resRate = rate.split("\n")

            for (index: Int in resources.indices) {
                result.add(
                    Pair(
                        Repository.getResourceByID(resources[index]),
                        resRate[index].toDouble()
                    )
                )
            }

            return result
        }

        fun mapNetToDbResource(resourceResponse: List<ResourceResponse>?): List<Resource> {
            val result = ArrayList<Resource>()

            resourceResponse?.forEach {
                result.add(
                    Resource(
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

        fun mapNetToDbJob(jobResponse: List<JobResponse>?): List<Job> {
            val result = ArrayList<Job>()

            jobResponse?.forEach {

                val resourceConsumptionAsStrings = getResourceConsumptionAsStrings(it.resources)

                result.add(
                    Job(
                        it.created,
                        it.name,
                        it.objectId,
                        resourceConsumptionAsStrings.first,
                        resourceConsumptionAsStrings.second,
                        it.surface,
                        it.type,
                        it.updated,
                        it.workflow,
                        0.0
                    )
                )
            }

            return result
        }

        private fun getResourceConsumptionAsStrings(resourceConsumptions: List<JobResponse.ResourceConsumption>): Pair<String, String> {
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
    }
}