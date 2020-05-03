package by.ciszkin.basicapp.ui.fragments.newestimate

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.ui.Utils
import kotlinx.android.synthetic.main.fragment_new_estimate.*
import java.util.*

class NewEstimateFragment : Fragment(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private var selectedDate = System.currentTimeMillis()
    private lateinit var model: NewEstimateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_estimate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model = ViewModelProvider(this).get(NewEstimateViewModel::class.java)

        showSelectedDeadlineDate()

        newEstimateName.editText?.setText(model.estimateName.value)
        newEstimateName.editText?.doAfterTextChanged {
            model.estimateName.value = it.toString()
            if (it.toString().isEmpty()) {
                newEstimateName.error = getString(R.string.empty_estimate_title_error)
            } else {
                newEstimateName.error = ""
            }
        }

        selectedDate = model.estimateDeadline.value!!
        newEstimateDeadlineTextInput.setOnClickListener(this)
        newEstimateDeadline.editText?.doAfterTextChanged {
            if (it.toString().isEmpty()) {
                newEstimateDeadline.error = getString(R.string.empty_estimate_deadline_error)
            } else {
                newEstimateDeadline.error = ""
            }
        }
        newEstimateDeadline.setStartIconOnClickListener {
            selectDate()
        }

        newEstimateNote.editText?.doAfterTextChanged {
            model.estimateNote.value = it.toString()
        }

        createEstimateButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.createEstimateButton -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate

                if (newEstimateDeadline.editText?.text.toString() != "") {
                    if (newEstimateName.editText?.text.toString() != "") {

                        model.createNewEstimate()

                        findNavController().navigateUp()
                    } else {
                        newEstimateName.error = getString(R.string.empty_estimate_title_error)
                    }
                } else {
                    newEstimateDeadline.error = getString(R.string.empty_estimate_deadline_error)
                }
            }

            R.id.newEstimateDeadlineTextInput -> {
                selectDate()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        selectedDate = calendar.timeInMillis
        showSelectedDeadlineDate()
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this.requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showSelectedDeadlineDate() {
        newEstimateDeadline.editText?.apply {
            text.clear()
            append(Utils.getDateAsString(selectedDate))
        }
        model.estimateDeadline.value = selectedDate
    }
}
