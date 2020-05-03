package by.ciszkin.basicapp.ui.fragments.estimateresourceslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateResource
import java.util.*
import kotlin.collections.ArrayList

class EstimateResourcesListViewModel : ViewModel() {

    val title = Estimate.current?.title
    val deadline = Estimate.current?.deadline
    val list =
        MutableLiveData(Estimate.current?.getResourcesList() as MutableList<EstimateResource>)

    fun search(query: String?) {
        val startList = ArrayList(Estimate.current?.getResourcesList() as ArrayList)
        val searchList = ArrayList<EstimateResource>()
        if(query == null || query.isEmpty()){
            list.postValue(startList)
        } else {
            startList.forEach { item ->
                if(item.resource.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    searchList.add(item)
                }
            }
            list.postValue(searchList)
        }
    }
}