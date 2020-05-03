package by.ciszkin.basicapp.model

import androidx.lifecycle.MutableLiveData
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units

data class RawResource(
    val objectId: String,
    val name: String,
    val type: ResType,
    val units: Units,
    val price: Double
) {
    companion object {
        val list = ArrayList<RawResource>()
        val current = MutableLiveData<Pair<RawResource, Double>>()
    }

    enum class SortBy (private val title: String) {
        COST_LOW("Сначала дешевые"),
        COST_HIGH("Сначала дорогие"),
        NAME_AZ("По названию А-Я"),
        NAME_ZA("По названию Я-А"),
        TYPE("По типу");

        override fun toString(): String {
            return title
        }
    }
}