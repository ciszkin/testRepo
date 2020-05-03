package by.ciszkin.basicapp.data.networking.requests

data class RegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)