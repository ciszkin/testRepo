package by.ciszkin.basicapp.ui.fragments.estimatedetailed

import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.model.Estimate


class EstimateDetailedViewModel : ViewModel() {

    var title = Estimate.current?.title
        set(value) {
            if (value != null) Estimate.current?.title = value
            field = value
        }
    var deadline = Estimate.current?.deadline
        set(value) {
            if (value != null) Estimate.current?.deadline = value
            field = value
        }
    var note = Estimate.current?.note
        set(value) {
            if (value != null) Estimate.current?.note = value
            field = value
        }
    var jobsCount = Estimate.current?.getJobsCount()
    var resourcesCount = Estimate.current?.getResourcesCount()
    var laborCost = Estimate.current?.getLaborCost()
    var completedLaborCost = Estimate.current?.getCompletedLaborCost()
    var resourcesCost = Estimate.current?.getResourcesCost()
    var completedResourcesCost = Estimate.current?.getCompletedResourcesCost()
    var totalCost = Estimate.current?.getTotalCost()
    var completedCost = Estimate.current?.getCompletedCost()
}