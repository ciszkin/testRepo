package by.ciszkin.basicapp.model

data class EstimateJob(
    val job: RawJob,
    var amount: Double,
    var completedAmount: Double,
    var note: String,
    var estimatePrice: Double
) {


//    companion object {
//        var current: EstimateJob? = null
//    }
}