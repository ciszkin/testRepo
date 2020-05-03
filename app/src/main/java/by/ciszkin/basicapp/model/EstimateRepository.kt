package by.ciszkin.basicapp.model

import android.content.Context
import kotlinx.coroutines.Deferred

interface EstimateRepository {
    fun getRawResources() : ArrayList<RawResource>
    fun getRawJobs() : ArrayList<RawJob>
    fun getEstimates() : ArrayList<Estimate>
    fun saveEstimates(context: Context, estimates: List<Estimate>)
    fun loadDataAsync(context: Context) : Deferred<Boolean>
    fun getJobById(id: String) : RawJob
    fun getResourceById(id: String) : RawResource
}