package by.ciszkin.basicapp.data.networking

import by.ciszkin.basicapp.data.networking.responses.JobResponse
import by.ciszkin.basicapp.data.networking.responses.ResourceResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendlessApi {
    @GET("data/Resource")
    fun getResourcesListAsync(
        @Query("pageSize") pageSize: Int
    ) : Deferred<Response<List<ResourceResponse>>>

    @GET("data/Job")
    fun getJobsListAsync(
        @Query("pageSize") pageSize: Int
    ) : Deferred<Response<List<JobResponse>>>
}