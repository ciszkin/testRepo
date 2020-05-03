package by.ciszkin.basicapp.ui.fragments.newresource

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.ciszkin.basicapp.R
import kotlinx.android.synthetic.main.fragment_new_resource.*
import kotlinx.android.synthetic.main.fragment_new_resource.unitsDropdown

class NewResourceFragment : Fragment(), View.OnClickListener {

    private lateinit var model: NewResourceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model = ViewModelProvider(this).get(NewResourceViewModel::class.java)

        model.resourceTypeIcon.observe(viewLifecycleOwner, Observer {
            newResourceTypeIcon.setImageResource(it)
        })
        model.resourceTypeName.observe(viewLifecycleOwner, Observer {
            newResourceTypeLabel.text = getString(it)
        })

        newResourceName.editText?.setText(model.resourceName.value)
        newResourceName.editText?.doAfterTextChanged {
            model.resourceName.value = it.toString()
            if (it.toString().isEmpty()) {
                newResourceName.error = getString(R.string.empty_resource_name_error)
            } else {
                newResourceName.error = ""
            }
        }

        newResourcePrice.editText?.setText(model.resourcePrice.value.toString())
        newResourcePrice.editText?.doAfterTextChanged {
            model.resourcePrice.value = it.toString().toDoubleOrNull()
            if (it.toString().isEmpty()) {
                newResourcePrice.error = getString(R.string.empty_new_resource_price_error)
            } else {
                newResourcePrice.error = ""
            }
        }

        newResourceUnits.editText?.doAfterTextChanged {
            model.resourceUnits.value = it.toString()
            if (it.toString().isEmpty()) {
                newResourceUnits.error = getString(R.string.empty_units_error)
            } else {
                newResourceUnits.error = ""
            }
        }

        newResourceTypeSwitch.setOnCheckedChangeListener { _, b ->
            model.changeResourceType(b)

        }

        unitsDropdown.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                model.unitsList
            )
        )

        createResourceButton.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.createResourceButton ->
                when {
                    model.resourceName.value == "" -> newResourceName.error =
                        getString(R.string.empty_resource_name_error)
                    model.resourcePrice.value.toString() == "" -> newResourcePrice.error =
                        getString(R.string.empty_new_resource_price_error)
                    model.resourceUnits.value == "" -> newResourceUnits.error =
                        getString(R.string.empty_units_error)
                    model.resourceName.value != "" && model.resourcePrice.value.toString() != "" && model.resourceUnits.value != "" -> {
                        model.createResource()
                        Toast.makeText(
                            requireContext(),
                            "New resource successfully created!",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }
                }
        }
    }


}
