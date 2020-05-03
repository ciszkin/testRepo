package by.ciszkin.basicapp.ui.fragments.estimateslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.fragment_estimates_list.*

class EstimatesListFragment : Fragment(),
    MainActivity.OnFabClickListener, MainActivity.OnSearchListener {

    private lateinit var model: EstimatesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimates_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(EstimatesListViewModel::class.java)

        sortEstimatesBy.adapter = ArrayAdapter(requireContext(),
            R.layout.spinner_text, model.spinnerList)
        sortEstimatesBy.onItemSelectedListener = model

        model.list.observe(viewLifecycleOwner, Observer {
            estimatesList.adapter =
                model.list.value?.let { EstimateAdapter(it) }
            estimatesList.layoutManager = LinearLayoutManager(this.context)
            estimatesList.hasFixedSize()
        })

    }

    override fun onFabClick() {
        findNavController().navigate(R.id.newEstimateFragment)
    }

    override fun performSearch(query: String?) {
        model.search(query)
    }

}
