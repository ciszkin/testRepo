package by.ciszkin.basicapp.ui.fragments.newjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.ui.fragments.jobdetailed.ConsumptionAdapter
import kotlinx.android.synthetic.main.fragment_new_job.*

class NewJobFragment : Fragment(), View.OnClickListener {

    lateinit var model: NewJobViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_job, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        model = ViewModelProvider(this).get(NewJobViewModel::class.java)

        spinnersSetup()

        model.consumptionList.observe(viewLifecycleOwner, Observer {
            newJobConsumptionList.adapter = ConsumptionAdapter(it)
            newJobConsumptionList.layoutManager = LinearLayoutManager(requireContext())
            newJobConsumptionList.hasFixedSize()
        })

        newJobName.editText?.setText(model.jobName.value)
        newJobName.editText?.doAfterTextChanged {
            model.jobName.value = it.toString()
            if (it.toString().isEmpty()) {
                newJobName.error = getString(R.string.empty_job_name_error)
            } else {
                newJobName.error = ""
            }
        }

        newJobSurface.editText?.doAfterTextChanged {
            model.jobSurface.value = it.toString()
            if (it.toString().isEmpty()) {
                newJobSurface.error = getString(R.string.empty_new_job_surface_error)
            } else {
                newJobSurface.error = ""
            }
        }


        newJobType.editText?.doAfterTextChanged {
            model.jobType.value = it.toString()
            if (it.toString().isEmpty()) {
                newJobType.error = getString(R.string.empty_new_job_type_error)
            } else {
                newJobType.error = ""
            }
        }

        newJobPrice.editText?.setText(model.jobPrice.value.toString())
        newJobPrice.editText?.doAfterTextChanged {
            model.jobPrice.value = it.toString().toDoubleOrNull()
            if (it.toString().isEmpty()) {
                newJobPrice.error = getString(R.string.empty_new_job_price_error)
            } else {
                newJobPrice.error = ""
            }
        }

        newJobUnits.editText?.doAfterTextChanged {
            model.jobUnits.value = it.toString()
            if (it.toString().isEmpty()) {
                newJobUnits.error = getString(R.string.empty_units_error)
            } else {
                newJobUnits.error = ""
            }
        }

        createJobButton.setOnClickListener(this)
        newJobAddResourceButton.setOnClickListener(this)
        newJobWorkflowButton.setOnClickListener(this)


    }

    private fun spinnersSetup() {
        surfaceDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                model.surfaceList
            )
        )
        typeDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                model.typeList
            )
        )
        unitsDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                model.unitsList
            )
        )
    }

    override fun onClick(view: View?) {


        when (view?.id) {

            createJobButton.id -> {
                val jobName = newJobName.editText?.text.toString()
                val jobSurface = surfaceDropdown.text.toString()
                val jobType = typeDropdown.text.toString()
                val jobPrice = newJobPrice.editText?.text.toString()
                val jobUnits = unitsDropdown.text.toString()



                when {
                    jobName == "" -> newJobName.error = getString(R.string.empty_job_name_error)
                    jobSurface == "" -> newJobSurface.error =
                        getString(R.string.empty_new_job_surface_error)
                    jobType == "" -> newJobType.error = getString(R.string.empty_new_job_type_error)
                    jobPrice == "" -> newJobPrice.error =
                        getString(R.string.empty_new_job_price_error)
                    jobUnits == "" -> newJobUnits.error =
                        getString(R.string.empty_units_error)
                    jobName != "" && jobSurface != "" && jobType != "" && jobPrice != "" && jobUnits != "" -> {
                        model.createNewJob()
                        findNavController().navigateUp()
                        Toast.makeText(requireContext(), "New job successfully created!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            newJobAddResourceButton.id -> {
                model.setResourceNeed()
                findNavController().navigate(R.id.resourcesListFragment)
            }
            newJobWorkflowButton.id -> {

            }
        }
    }
}
