package by.ciszkin.basicapp.model

import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units

data class EstimateResource(
    val objectId: String,
    val name: String,
    val type: ResType,
    val units: Units,
    val price: Double
)