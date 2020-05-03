package by.ciszkin.basicapp.ui.fragments.resourceslist

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.RawResource
import java.util.*

class ResourcesListViewModel : ViewModel(), AdapterView.OnItemSelectedListener {

    val list = MutableLiveData(RawResource.list)
    val spinnerList = RawResource.SortBy.values()

    fun search(query: String?) {
        val startList = ArrayList(RawResource.list)
        val searchList = ArrayList<RawResource>()
        if(query == null || query.isEmpty()){
            list.postValue(startList)
        } else {
            startList.forEach { item ->
                if(item.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    searchList.add(item)
                }
            }
            list.postValue(searchList)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {  }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val sortedList = ArrayList(RawResource.list)

        when(position) {

            RawResource.SortBy.COST_HIGH.ordinal -> {
                sortedList.sortByDescending {
                    it.price
                }
                list.postValue(sortedList)
            }
            RawResource.SortBy.COST_LOW.ordinal -> {
                sortedList.sortBy {
                    it.price
                }
                list.postValue(sortedList)
            }
            RawResource.SortBy.NAME_AZ.ordinal -> {
                sortedList.sortBy {
                    it.name
                }
                list.postValue(sortedList)
            }
            RawResource.SortBy.NAME_ZA.ordinal -> {
                sortedList.sortByDescending {
                    it.name
                }
                list.postValue(sortedList)
            }
            RawResource.SortBy.TYPE.ordinal -> {
                sortedList.sortBy {
                    it.type
                }
                list.postValue(sortedList)
            }
        }
    }
}