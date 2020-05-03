package by.ciszkin.basicapp.data.networking

import by.ciszkin.basicapp.data.networking.requests.LoginRequest
import by.ciszkin.basicapp.data.networking.requests.RegistrationRequest
import by.ciszkin.basicapp.data.networking.responses.NetJob
import by.ciszkin.basicapp.data.networking.responses.NetResource
import by.ciszkin.basicapp.data.networking.responses.UserResponse
import by.ciszkin.basicapp.data.networking.responses.Session
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface BackendlessApi {
    @GET("data/Resource")
    fun getResourcesListAsync(
        @Query("pageSize") pageSize: Int
    ) : Deferred<Response<List<NetResource>>>

    @GET("data/Job")
    fun getJobsListAsync(
        @Query("pageSize") pageSize: Int
    ) : Deferred<Response<List<NetJob>>>

    @GET("data/Resource/count")
    fun getResourcesCountAsync() : Deferred<Int>

    @GET("data/Job/count")
    fun getJobsCountAsync() : Deferred<Int>

    @GET("users/isvalidusertoken/{userToken}")
    fun isValidUserTokenAsync(
        @Path("userToken") userToken: String
    ) : Deferred<Boolean>

    @POST("users/register")
    fun registerUserAsync(@Body registrationRequest: RegistrationRequest) : Deferred<Response<UserResponse>>

    @POST("users/login")
    fun loginUserAsync(@Body loginRequest: LoginRequest) : Deferred<Response<Session>>


}