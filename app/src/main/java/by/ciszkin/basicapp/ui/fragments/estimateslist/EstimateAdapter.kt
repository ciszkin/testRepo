package by.ciszkin.basicapp.ui.fragments.estimateslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.ui.Utils
import by.ciszkin.basicapp.ui.Utils.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.list_item_estimate.view.*
import java.util.*

class EstimateAdapter(val list: MutableList<Estimate>) :
    RecyclerView.Adapter<EstimateAdapter.EstimateViewHolder>() {
    class EstimateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_estimate, parent, false)
        return EstimateViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EstimateViewHolder, position: Int) {
        val view = holder.itemView
        val item = list[position]
        view.apply {
            estimateImage.setImageResource(R.drawable.ic_estimate_icon)
            estimateTitleLabel.text = item.title
            estimateCostValue.apply {
                text = item.getCompletedCost().round(Utils.costAccuracy).toString()
                append(" / ")
                append(item.getTotalCost().round(Utils.costAccuracy).toString())
                append(context.getString(R.string.currency))
            }

            progressBar.progress = ((item.getCompletedCost() / item.getTotalCost()) * 100).toInt()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = item.deadline

            estimateDeadlineValue.text = Utils.getDateAsString(item.deadline)

            setOnClickListener {
                Estimate.current = item
                findNavController().navigate(R.id.estimateJobsListFragment)
            }

            setOnLongClickListener {
                MaterialAlertDialogBuilder(this.context)
                    .setTitle(context.getString(R.string.delete_estimate_dialog_label))
                    .setMessage(
                        item.title + item.getCompletedCost()
                            .round(Utils.costAccuracy) + context.getString(R.string.per) + item.getTotalCost()
                            .round(Utils.costAccuracy)
                    )
                    .setIcon(R.drawable.ic_delete_black_24dp)
                    .setPositiveButton(R.string.ok_button) { _, _ ->
                        Estimate.list.remove(item)
                        Estimate.deletionList.add(item)
                        Estimate.current = null
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton(R.string.cancel_button, null)
                    .show()
                true
            }
        }
    }
}