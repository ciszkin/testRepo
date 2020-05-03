package by.ciszkin.basicapp.data

import android.content.Context
import androidx.preference.PreferenceManager
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.networking.NetworkService
import by.ciszkin.basicapp.data.networking.requests.LoginRequest
import by.ciszkin.basicapp.data.networking.requests.RegistrationRequest
import by.ciszkin.basicapp.data.networking.responses.ErrorResponse
import by.ciszkin.basicapp.data.util.Mappers

object UserService {

    suspend fun register(
        context: Context,
        name: String,
        email: String,
        password: String
    ): ErrorResponse? {
        val userResponse =
            NetworkService.getAnonymousBackendlessApi()
                .registerUserAsync(RegistrationRequest(name, email, password)).await()
        return if (userResponse.isSuccessful) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            with(sharedPref.edit()) {
                putBoolean(context.getString(R.string.pref_auto_login), true)
                putString(context.getString(R.string.user_name), name)
                putString(context.getString(R.string.user_email), email)
                putString(context.getString(R.string.user_password), password)
                commit()
            }
            null
        } else Mappers.mapErrorBodyToErrorResponse(userResponse.errorBody())
    }


    suspend fun loginWithToken(context: Context): ErrorResponse? {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        val autoLogin = sharedPref.getBoolean(context.getString(R.string.pref_auto_login), false)

        if (autoLogin) {
            val userToken = sharedPref.getString(context.getString(R.string.user_token), null)
            if (userToken != null) {
                return if (NetworkService.getAnonymousBackendlessApi()
                        .isValidUserTokenAsync(userToken).await()
                ) {
                    null
                } else checkLogin(context)
            }

            return checkLogin(context)
        } else {
            val email = sharedPref.getString(context.getString(R.string.user_email), "")
            val password = sharedPref.getString(context.getString(R.string.user_password), "")

            return ErrorResponse(100100, email + "\n" + password)
        }
    }

    private suspend fun checkLogin(context: Context): ErrorResponse? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val email = sharedPref.getString(context.getString(R.string.user_email), null)
        val password = sharedPref.getString(context.getString(R.string.user_password), null)

        return login(context, email, password)

    }


    suspend fun login(context: Context, email: String?, password: String?): ErrorResponse? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        if (email != null && password != null) {
            val sessionResponse =
                NetworkService.getAnonymousBackendlessApi()
                    .loginUserAsync(LoginRequest(email, password))
                    .await()

            return if (sessionResponse.isSuccessful) {
                val newUserToken = sessionResponse.body()!!.userToken

                with(sharedPref.edit()) {
                    putString(context.getString(R.string.user_token), newUserToken)
                    putBoolean(context.getString(R.string.pref_auto_login), true)
                    putString(context.getString(R.string.user_name), null)
                    putString(context.getString(R.string.user_email), email)
                    putString(context.getString(R.string.user_password), password)
                    commit()
                }


                null
            } else Mappers.mapErrorBodyToErrorResponse(sessionResponse.errorBody())

        }
        return ErrorResponse(100001, "Register first!")
    }


}