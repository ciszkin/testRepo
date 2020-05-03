package by.ciszkin.basicapp.model

import androidx.lifecycle.MutableLiveData
import by.ciszkin.basicapp.model.enums.JobSurface
import by.ciszkin.basicapp.model.enums.JobType
import by.ciszkin.basicapp.model.enums.Units

data class RawJob(
    val objectId: String,
    val name: String,
    val resourcesConsumptionList: List<Pair<RawResource, Double>>,
    val surface: JobSurface,
    val type: JobType,
    val units: Units,
    val workflow: List<String>,
    val price: Double
) {
    companion object {
        val list = ArrayList<RawJob>()
        var current = MutableLiveData<RawJob>()
    }

    enum class SortBy (private val title: String) {
        COST_LOW("Сначала дешевые"),
        COST_HIGH("Сначала дорогие"),
        NAME_AZ("По названию А-Я"),
        NAME_ZA("По названию Я-А"),
        TYPE("По типу"),
        SURFACE("По месту");

        override fun toString(): String {
            return title
        }
    }
}