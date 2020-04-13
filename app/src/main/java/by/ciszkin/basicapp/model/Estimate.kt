package by.ciszkin.basicapp.model

data class Estimate (
    val name: String,
    val creationDate: Long,
    val location: String,
    val jobsList: List<Pair<EstimateJob, Double>>
)