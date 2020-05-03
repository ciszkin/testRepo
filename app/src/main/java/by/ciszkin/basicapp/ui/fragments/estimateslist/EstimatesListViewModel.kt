package by.ciszkin.basicapp.ui.fragments.estimateslist

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate
import java.util.*
import kotlin.collections.ArrayList

class EstimatesListViewModel : ViewModel(), AdapterView.OnItemSelectedListener {

    val list = MutableLiveData(Estimate.list)
    val spinnerList = Estimate.SortBy.values()

    fun search(query: String?) {
        val startList = list.value
        val searchList = ArrayList<Estimate>()
        if(query == null || query.isEmpty()){
            list.postValue(startList)
        } else {
            startList?.forEach { item ->
                if(item.title.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    searchList.add(item)
                }
            }
            list.postValue(searchList)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val sortedList = list.value

        when(position) {
            Estimate.SortBy.COMPLETION_HIGH.ordinal -> {
                sortedList?.sortByDescending {
                    val a = it.getCompletedCost()
                    val b = it.getTotalCost()
                    a/b
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.COMPLETION_LOW.ordinal -> {
                sortedList?.sortBy {
                    val a = it.getCompletedCost()
                    val b = it.getTotalCost()
                    a/b
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.COST_HIGH.ordinal -> {
                sortedList?.sortByDescending {
                    it.getTotalCost()
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.COST_LOW.ordinal -> {
                sortedList?.sortBy {
                    it.getTotalCost()
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.DEADLINE_CLOSE.ordinal -> {
                sortedList?.sortBy {
                    it.deadline
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.DEADLINE_FAR.ordinal -> {
                sortedList?.sortByDescending {
                    it.deadline
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.NAME_AZ.ordinal -> {
                sortedList?.sortBy {
                    it.title
                }
                list.postValue(sortedList)
            }
            Estimate.SortBy.NAME_ZA.ordinal -> {
                sortedList?.sortByDescending {
                    it.title
                }
                list.postValue(sortedList)
            }
        }
    }
}