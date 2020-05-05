package by.ciszkin.basicapp.ui.fragments.estimatejobslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateJob
import by.ciszkin.basicapp.ui.Utils
import by.ciszkin.basicapp.ui.Utils.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.list_item_estimate_job.view.*

class EstimateJobAdapter(val list:MutableList<EstimateJob>) : RecyclerView.Adapter<EstimateJobAdapter.EstimateJobViewHolder>() {
    class EstimateJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateJobViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_estimate_job, parent, false)
        return EstimateJobViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EstimateJobViewHolder, position: Int) {
        val view = holder.itemView
        val item = list[position]
        view.apply {
            estimateJobResourceImage.setImageResource(item.job.type.icon)
            estimateJobSurfaceImage.setImageResource(item.job.surface.icon)
            estimateJobTitleLabel.text = item.job.name

            estimateJobAmountValue.apply{
                text = context.getString(R.string.completed_label)
                append(" ")
                append(item.completedAmount.round(Utils.amountAccuracy).toString())
                append(context.getString(R.string.per))
                append(item.amount.round(Utils.amountAccuracy).toString())
                append(item.job.units.title)
            }
            estimateJobCostValue.apply{
                val costPerUnit = Estimate.getTotalJobCostPerUnit(item)
                text = (costPerUnit*item.completedAmount).round(Utils.costAccuracy).toString()
                append(context.getString(R.string.per))
                append((costPerUnit*item.amount).round(Utils.costAccuracy).toString())
                append(context.getString(R.string.currency))
            }

            jobProgressBar.progress = (item.completedAmount/item.amount*100).toInt()

            setOnClickListener {

                val editCompletedAmount = EditText(this.context)
                editCompletedAmount.apply{
                    setRawInputType(2002)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_build_black_24dp, 0, 0, 0)
                    isSingleLine = true
                    text.append(item.completedAmount.round(Utils.amountAccuracy).toString())
                    hint = "Введите объем работ в " + item.job.units.title
                }

                val editPrice = EditText(this.context)
                editPrice.apply{
                    setRawInputType(2002)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_monetization_on_black_24dp, 0, 0, 0)
                    isSingleLine = true
                    text.append(item.estimatePrice.round(Utils.amountAccuracy).toString())
                    hint = "Введите новую цену в " + context.getString(R.string.currency)
                }

                val dialogViewGroup = LinearLayout(context)
                dialogViewGroup.apply {
                    addView(editCompletedAmount)
                    addView(editPrice)
                    orientation = LinearLayout.VERTICAL
                }


                MaterialAlertDialogBuilder(this.context)
                    .setTitle("Выполнение работы: ")
                    .setMessage(item.job.name + ": " + item.completedAmount.round(Utils.amountAccuracy) + "/" + item.amount.round(Utils.amountAccuracy) + " " + item.job.units.title)
                    .setIcon(R.drawable.ic_edit_icon)

                    .setView(dialogViewGroup)
                    .setPositiveButton(R.string.ok_button) { _, _ ->

                        item.completedAmount = editCompletedAmount.text.toString().toDouble()
                        item.estimatePrice = editPrice.text.toString().toDouble()

                        notifyItemChanged(position)
                        Estimate.current?.update()

                    }
                    .setNegativeButton(R.string.cancel_button, null)
                    .show()
            }

            setOnLongClickListener {
                MaterialAlertDialogBuilder(this.context)
                    .setTitle("Удалить работу?")
                    .setMessage(item.job.name + ": " + item.completedAmount.round(Utils.amountAccuracy) + "/" + item.amount.round(Utils.amountAccuracy) + " " + item.job.units.title)
                    .setIcon(R.drawable.ic_delete_black_24dp)
                    .setPositiveButton(R.string.ok_button) { _, _ ->
                        Estimate.current?.removeJob(item)
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton(R.string.cancel_button, null)
                    .show()
                true
            }
        }
    }
}