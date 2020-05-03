package by.ciszkin.basicapp.ui.fragments.estimatedetailed

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.ui.Utils
import by.ciszkin.basicapp.ui.Utils.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_estimate_detailed.*
import java.util.*

class EstimateDetailedFragment : Fragment(),
    View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var model: EstimateDetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimate_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(EstimateDetailedViewModel::class.java)

        model.let{
            estimateName.text = it.title
            estimateName.setOnClickListener(this)
            estimateDeadline.apply{
                text = getString(R.string.deadline_label)
                append(": ")
                append(it.deadline?.let { _deadline -> Utils.getDateAsString(_deadline) })
            }
            estimateDeadline.setOnClickListener(this)

            checkTerm()

            estimateNote.text = it.note
            estimateNote.setOnClickListener(this)

            jobsCountValue.text = it.jobsCount.toString()
            resourcesCountValue.text = it.resourcesCount.toString()
            laborCostValue.text = it.laborCost?.round(Utils.costAccuracy).toString()
            completedLaborCostValue.text = it.completedLaborCost?.round(Utils.costAccuracy).toString()
            resourcesCostValue.text = it.resourcesCost?.round(Utils.costAccuracy).toString()
            completedResourcesCostValue.text = it.completedResourcesCost?.round(Utils.costAccuracy).toString()
            totalCostValue.text = it.totalCost?.round(Utils.costAccuracy).toString()
            completedCostValue.text = it.completedCost?.round(Utils.costAccuracy).toString()
        }

        showJobsButton.setOnClickListener(this)
        showResourcesButton.setOnClickListener(this)


    }

    private fun checkTerm() {
        val currentDeadline = model.deadline ?: System.currentTimeMillis()
        attentionSign.visibility = if (currentDeadline <= System.currentTimeMillis()) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.showJobsButton -> {
                findNavController().navigate(R.id.estimateJobsListFragment)
            }
            R.id.showResourcesButton -> {
                findNavController().navigate(R.id.estimateResourcesListFragment)
            }
            R.id.estimateName -> {
                val editText = EditText(this.context)
                editText.apply{
                    text.append(model.title)
                }
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.new_estimate_name_label)
                    .setIcon(R.drawable.ic_edit_icon)
                    .setView(editText)
                    .setNegativeButton(R.string.cancel_button, null)
                    .setPositiveButton(R.string.ok_button) { _, _ ->
                        estimateName.text = editText.text
                        model.title = editText.text.toString()
                    }
                    .show()
            }
            R.id.estimateDeadline -> {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    this.requireContext(),
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.estimateNote -> {
                val editText = EditText(this.context)
                editText.apply{
                    text.append(model.note)
                }
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.note_label)
                    .setIcon(R.drawable.ic_edit_icon)
                    .setView(editText)
                    .setNegativeButton(R.string.cancel_button, null)
                    .setPositiveButton(R.string.ok_button) { _, _ ->
                        estimateNote.text = editText.text
                        model.note = editText.text.toString()
                    }
                    .show()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        model.deadline = calendar.timeInMillis

        estimateDeadline.apply{
            text = getString(R.string.deadline_label)
            append(": ")
            append(Utils.getDateAsString(calendar.timeInMillis))
        }

        checkTerm()
    }
}
