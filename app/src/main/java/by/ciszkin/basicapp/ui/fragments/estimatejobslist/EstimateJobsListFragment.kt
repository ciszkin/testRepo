package by.ciszkin.basicapp.ui.fragments.estimatejobslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.ui.activities.main.MainActivity
import by.ciszkin.basicapp.ui.Utils
import kotlinx.android.synthetic.main.fragment_estimate_jobs_list.*

class EstimateJobsListFragment : Fragment(),
    MainActivity.OnFabClickListener, MainActivity.OnSearchListener, View.OnClickListener {

    private lateinit var model: EstimateJobsListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimate_jobs_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(EstimateJobsListViewModel::class.java)

        detailedEstimateName.text = model.title

//        if(Estimate.current!!.deadline < System.currentTimeMillis()) {
//            detailedEstimateDeadline.colo
//        }
        detailedEstimateDeadline.apply{

            text = getString(R.string.deadline_label)
            append(": ")
            append(model.deadline?.let { Utils.getDateAsString(it) })

        }

        model.list.observe(viewLifecycleOwner, Observer{
            detailedEstimateJobsList.adapter = model.list.value?.let { EstimateJobAdapter(it) }
            detailedEstimateJobsList.layoutManager = LinearLayoutManager(this.context)
            detailedEstimateJobsList.hasFixedSize()
        })


        detailedEstimateInfo.setOnClickListener(this)

    }

    override fun onFabClick() {
        findNavController().navigate(R.id.jobsListFragment)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.detailedEstimateInfo -> {
                findNavController().navigate(R.id.estimateDetailedFragment)
            }
        }
    }

    override fun performSearch(query: String?) {
        model.search(query)
    }
}
