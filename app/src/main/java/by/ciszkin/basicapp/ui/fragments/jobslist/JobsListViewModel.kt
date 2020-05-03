package by.ciszkin.basicapp.ui.fragments.jobslist

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.RawJob
import java.util.*

class JobsListViewModel : ViewModel(), AdapterView.OnItemSelectedListener {

    val list = MutableLiveData(RawJob.list)
    val spinnerList = RawJob.SortBy.values()

    init {

    }

    fun search(query: String?) {
        val startList = ArrayList(RawJob.list)
        val searchList = ArrayList<RawJob>()
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
        val sortedList = RawJob.list

        when(position) {

            RawJob.SortBy.COST_HIGH.ordinal -> {
                sortedList.sortByDescending {
                    it.price
                }
                list.postValue(sortedList)
            }
            RawJob.SortBy.COST_LOW.ordinal -> {
                sortedList.sortBy {
                    it.price
                }
                list.postValue(sortedList)
            }
            RawJob.SortBy.NAME_AZ.ordinal -> {
                sortedList.sortBy {
                    it.name
                }
                list.postValue(sortedList)
            }
            RawJob.SortBy.NAME_ZA.ordinal -> {
                sortedList.sortByDescending {
                    it.name
                }
                list.postValue(sortedList)
            }
            RawJob.SortBy.TYPE.ordinal -> {
                sortedList.sortBy {
                    it.type
                }
                list.postValue(sortedList)
            }
            RawJob.SortBy.SURFACE.ordinal -> {
                sortedList.sortBy {
                    it.surface
                }
                list.postValue(sortedList)
            }
        }
    }
}