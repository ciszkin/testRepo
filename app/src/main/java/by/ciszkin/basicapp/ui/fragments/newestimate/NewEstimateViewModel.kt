package by.ciszkin.basicapp.ui.fragments.newestimate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate

class NewEstimateViewModel : ViewModel() {

    val estimateName = MutableLiveData("")
    val estimateDeadline = MutableLiveData(System.currentTimeMillis())
    val estimateNote = MutableLiveData("")

    fun createNewEstimate() {
        val estimate = Estimate(
            "estimate-${System.currentTimeMillis()}",
            estimateName.value!!,
            estimateDeadline.value!!
        )
        estimate.note = estimateNote.value!!
        Estimate.list.add(estimate)
    }
}