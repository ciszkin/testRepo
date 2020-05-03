package by.ciszkin.basicapp.ui.fragments.resourceslist

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
import kotlinx.android.synthetic.main.fragment_resources.*


class ResourcesListFragment : Fragment(),
    MainActivity.OnFabClickListener, MainActivity.OnSearchListener {

    lateinit var model: ResourcesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(ResourcesListViewModel::class.java)

        sortResourcesBy.adapter = ArrayAdapter(requireContext(),
            R.layout.spinner_text, model.spinnerList)
        sortResourcesBy.onItemSelectedListener = model

        model.list.observe(viewLifecycleOwner, Observer {
            resourcesRecycler.adapter = model.list.value?.let { RawResourceAdapter(it, this) }
            resourcesRecycler.layoutManager = LinearLayoutManager(this.context)
            resourcesRecycler.hasFixedSize()
        })


    }

    override fun onFabClick() {
        findNavController().navigate(R.id.newResourceFragment)
    }

    override fun performSearch(query: String?) {
        model.search(query)
    }
}
