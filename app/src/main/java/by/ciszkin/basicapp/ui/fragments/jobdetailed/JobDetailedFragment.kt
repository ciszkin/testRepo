package by.ciszkin.basicapp.ui.fragments.jobdetailed


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource
import by.ciszkin.basicapp.ui.activities.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_job_detailed.*

class JobDetailedFragment : Fragment(), View.OnClickListener,
    MainActivity.OnFabClickListener {

    private lateinit var model: JobDetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(JobDetailedViewModel::class.java)

        model.let {
            detailedJobNameLabel.text = it.jobName
            detailedJobTypeImage.setImageResource(it.jobTypeIcon)
            detailedJobTypeLabel.text = it.jobTypeTitle
            detailedSurfaceImage.setImageResource(it.jobSurfaceIcon)
            detailedSurfaceLabel.text = it.jobSurfaceTitle
            detailedJobPrice.text = it.jobPrice.toString()
            detailedJobPrice.append(" ")
            detailedJobPrice.append(getString(R.string.currency))
        }

        detailedResourcesList.adapter =
            ConsumptionAdapter(model.consumptionList)
        detailedResourcesList.layoutManager = LinearLayoutManager(detailedResourcesList.context)

        workflowButton.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
       when(view?.id) {
           R.id.workflowButton -> {
               MaterialAlertDialogBuilder(this.context)
                   .setTitle(R.string.workflow_label)
                   .setItems(model.workflow.toTypedArray(), null)
                   .setNeutralButton(getString(R.string.close_button), null)
                   .show()
           }
       }
    }

    override fun onFabClick() {

        if (model.isEstimateSelected()) {
            val editAmount = EditText(this.context)
            editAmount.apply {
                setRawInputType(2002)
                isSingleLine = true
                hint = getString(R.string.input_job_amount_label) + model.unitsTitle
            }

            MaterialAlertDialogBuilder(this.context)
                .setTitle(getString(R.string.add_job_to_estimate_label))
                .setIcon(R.drawable.ic_add_job_icon)
                .setMessage(model.currentEstimateTitle)
                .setView(editAmount)
                .setNegativeButton(R.string.cancel_button) { _, _ ->
                    findNavController().navigateUp()
                }
                .setPositiveButton(R.string.ok_button) { _, _ ->
                    model.addJobToEstimate(editAmount.text.toString())
                    findNavController().navigateUp()
                }
                .show()
        } else {
            Toast.makeText(requireContext(), "Select estimate first!", Toast.LENGTH_SHORT).show()
        }
    }

}
