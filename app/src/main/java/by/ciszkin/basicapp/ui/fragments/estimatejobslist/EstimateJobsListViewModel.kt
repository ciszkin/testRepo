package by.ciszkin.basicapp.ui.fragments.estimatejobslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import java.util.*
import kotlin.collections.ArrayList

class EstimateJobsListViewModel : ViewModel() {
    val title = Estimate.current?.title
    val deadline =  Estimate.current?.deadline
    val list = MutableLiveData(Estimate.current?.getJobsList() as MutableList<EstimateJob>)

    fun search(query: String?) {
        val startList = ArrayList(Estimate.current?.getJobsList() as ArrayList)
        val searchList = ArrayList<EstimateJob>()
        if(query == null || query.isEmpty()){
            list.postValue(startList)
        } else {
            startList.forEach { item ->
                if(item.job.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    searchList.add(item)
                }
            }
            list.postValue(searchList)
        }
    }
}