package by.ciszkin.basicapp.model

import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType

data class EstimateJob(
    val objectId: String,
    val name: String,
    val resources: List<Pair<EstimateResource, Double>>,
    val surface: JobSurface,
    val type: JobType,
    val workflow: List<String>,
    val price: Double
)