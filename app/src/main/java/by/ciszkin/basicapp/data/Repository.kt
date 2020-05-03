package by.ciszkin.basicapp.data

import android.content.Context
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.db.AppDatabase
import by.ciszkin.basicapp.data.db.entity.DbEstimate
import by.ciszkin.basicapp.data.db.entity.DbJob
import by.ciszkin.basicapp.data.db.entity.DbResource
import by.ciszkin.basicapp.data.networking.NetworkService
import by.ciszkin.basicapp.data.util.Mappers
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateRepository
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource
import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units
import kotlinx.coroutines.*

object Repository : EstimateRepository{

    val EMPTY_RESOURCE =
        RawResource(
            "000000-resource",
            "Empty resource",
            ResType.MATERIAL,
            Units.PIECE,
            0.0
        )
    val EMPTY_JOB = RawJob(
        "000000-job",
        "Empty job",
        listOf(Pair(EMPTY_RESOURCE, 0.0)),
        JobSurface.ALL,
        JobType.OTHER,
        Units.PIECE,
        listOf("Empty"),
        0.0
    )

    private var resources = ArrayList<RawResource>()
    private var jobs = ArrayList<RawJob>()
    private var estimates = ArrayList<Estimate>()

    private var dBResources = ArrayList<DbResource>()
    private var dBJobs = ArrayList<DbJob>()
    private var dBEstimates = ArrayList<DbEstimate>()

    private var resourcesLoading: Job? = null
    private var jobsLoading: Job? = null
    private var estimatesLoading: Job? = null

    override fun getRawResources() = resources

    override fun getRawJobs() = jobs

    override fun getEstimates() = estimates

    override fun saveEstimates(context: Context, estimates: List<Estimate>) {
        AppDatabase(context).getEstimateDao()
            .addEstimates(Mappers.mapEstimateToDbEstimate(estimates))
    }

    override fun loadDataAsync(context: Context) = CoroutineScope(Dispatchers.IO).async {

        if (resources.isEmpty()) {
            val dbResCount = AppDatabase(context).getResourceDao().getResourcesCount()
            val netResCount =
                NetworkService.getAnonymousBackendlessApi().getResourcesCountAsync().await()

            if (dbResCount < netResCount) {
                downloadResources(context)
            } else {
                resourcesLoading = loadResourcesFromDb(context)
            }

            resourcesLoading?.join()
            resources = Mappers.mapDbToRawResource(dBResources) as ArrayList<RawResource>
        }

        if (jobs.isEmpty()) {
            val dbJobCount = AppDatabase(context).getJobDao().getJobsCount()
            val netJobCount = NetworkService.getAnonymousBackendlessApi().getJobsCountAsync().await()

            if (dbJobCount < netJobCount) {
                downloadJobs(context)
            } else {
                jobsLoading = loadJobsFromDb(context)
            }

            jobsLoading?.join()
            jobs = Mappers.mapDbToRawJob(dBJobs) as ArrayList<RawJob>
        }

        estimatesLoading = loadEstimatesFromDb(context)
        estimatesLoading?.join()
        estimates = Mappers.mapDbEstimateToEstimate(dBEstimates) as ArrayList<Estimate>

        return@async jobs.isNotEmpty() && resources.isNotEmpty()
    }

    private suspend fun downloadJobs(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val userToken = sharedPref.getString(context.getString(R.string.user_token), "")
        val jobsListResponse = NetworkService.getBackendlessApi(userToken!!).getJobsListAsync(100).await()
        if (jobsListResponse.isSuccessful) {
            AppDatabase(context).getJobDao()
                .addJobs(Mappers.mapNetToDbJob(jobsListResponse.body()))
            jobsLoading = loadJobsFromDb(context)
        }
    }

    private suspend fun downloadResources(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val userToken = sharedPref.getString(context.getString(R.string.user_token), "")
        val resourcesListResponse =
            NetworkService.getBackendlessApi(userToken!!).getResourcesListAsync(100).await()
        if (resourcesListResponse.isSuccessful) {
            AppDatabase(context).getResourceDao()
                .addResources(Mappers.mapNetToDbResource(resourcesListResponse.body()))
            resourcesLoading = loadResourcesFromDb(context)
        }
    }

    override fun getJobById(id: String): RawJob {
        var result: RawJob = EMPTY_JOB
        jobs.forEach {
            if (id == it.objectId) {
                result = it
                return@forEach
            }
        }

        return result
    }

    override fun getResourceById(id: String): RawResource {
        var result: RawResource = EMPTY_RESOURCE
        resources.forEach {
            if (id == it.objectId) {
                result = it
                return@forEach
            }
        }
        return result
    }

    private fun loadJobsFromDb(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        dBJobs = AppDatabase(context).getJobDao().getJobs() as ArrayList<DbJob>
    }

    private fun loadResourcesFromDb(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        dBResources =
            AppDatabase(context).getResourceDao().getResources() as ArrayList<DbResource>
    }

    private fun loadEstimatesFromDb(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        dBEstimates = AppDatabase(context).getEstimateDao().getEstimates() as ArrayList<DbEstimate>
    }
}