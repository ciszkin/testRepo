package by.ciszkin.basicapp.model

data class EstimateResource(
    val resource: RawResource,
    val jobsList: MutableList<EstimateJob>,
    var amount: Double,
    var completedAmount: Double,
    var estimatePrice: Double
)