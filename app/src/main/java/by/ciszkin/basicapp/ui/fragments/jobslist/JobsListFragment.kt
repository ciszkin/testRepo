package by.ciszkin.basicapp.ui.fragments.jobslist

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
import kotlinx.android.synthetic.main.fragment_jobs.*

class JobsListFragment : Fragment(),
    MainActivity.OnFabClickListener, MainActivity.OnSearchListener {

    lateinit var model: JobsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(JobsListViewModel::class.java)

        sortJobsBy.adapter = ArrayAdapter(requireContext(),
            R.layout.spinner_text, model.spinnerList)
        sortJobsBy.onItemSelectedListener = model

        model.list.observe(viewLifecycleOwner, Observer {
            jobsRecycler.adapter = model.list.value?.let { RawJobAdapter(it, this) }
            jobsRecycler.layoutManager = LinearLayoutManager(this.context)
            jobsRecycler.hasFixedSize()
        })

    }

    override fun onFabClick() {
        findNavController().navigate(R.id.newJobFragment)
    }

    override fun performSearch(query: String?) {
        model.search(query)
    }
}
