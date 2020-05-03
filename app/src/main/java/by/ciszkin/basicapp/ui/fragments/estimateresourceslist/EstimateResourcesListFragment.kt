package by.ciszkin.basicapp.ui.fragments.estimateresourceslist

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
import by.ciszkin.basicapp.ui.Utils
import by.ciszkin.basicapp.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.fragment_estimate_resources_list.detailedEstimateDeadline
import kotlinx.android.synthetic.main.fragment_estimate_resources_list.detailedEstimateName
import kotlinx.android.synthetic.main.fragment_estimate_resources_list.*
import kotlinx.android.synthetic.main.fragment_estimate_resources_list.detailedEstimateInfo

class EstimateResourcesListFragment : Fragment(),
    View.OnClickListener, MainActivity.OnSearchListener {

    lateinit var model: EstimateResourcesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimate_resources_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(EstimateResourcesListViewModel::class.java)

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
            detailedEstimateResourcesList.adapter =
                model.list.value?.let {
                    EstimateResourceAdapter(it)
                }
            detailedEstimateResourcesList.layoutManager = LinearLayoutManager(this.context)
            detailedEstimateResourcesList.hasFixedSize()
        })


        detailedEstimateInfo.setOnClickListener(this)
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
