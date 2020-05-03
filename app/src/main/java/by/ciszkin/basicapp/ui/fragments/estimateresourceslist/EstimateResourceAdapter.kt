package by.ciszkin.basicapp.ui.fragments.estimateresourceslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.Estimate
import by.ciszkin.basicapp.model.EstimateResource
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.ui.Utils
import by.ciszkin.basicapp.ui.Utils.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.list_item_estimate_resource.view.*

class EstimateResourceAdapter(val list: MutableList<EstimateResource>) :
    RecyclerView.Adapter<EstimateResourceAdapter.EstimateResourceViewHolder>() {
    class EstimateResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateResourceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_estimate_resource, parent, false)
        return EstimateResourceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EstimateResourceViewHolder, position: Int) {
        val view = holder.itemView
        val item = list[position]
        view.apply {
            estimateResourceNameLabel.text = item.resource.name
            estimateResourceAmountValue.apply {
                text = item.completedAmount.round(Utils.amountAccuracy).toString()
                append(context.getString(R.string.per))
                append(item.amount.round(Utils.amountAccuracy).toString())
                append(" ")
                append(item.resource.units.title)
            }

            estimateResourceCostValue.apply {
                text = (item.completedAmount * item.estimatePrice).round(Utils.costAccuracy).toString()
                append(context.getString(R.string.per))
                append((item.amount * item.estimatePrice).round(Utils.costAccuracy).toString())
                append(" ")
                append(context.getString(R.string.currency))
            }

            estimateResourceImage.setImageResource(item.resource.type.icon)

            setOnClickListener {
                val editText = EditText(this.context)
                editText.apply{
                    setRawInputType(2002)
                    isSingleLine = true
                    text.append(item.estimatePrice.toString())
                    hint = "Введите цену за 1 " + item.resource.units.title
                }

                MaterialAlertDialogBuilder(context)
                    .setTitle("Новая цена ресурса:")
                    .setMessage(item.resource.name)
                    .setIcon(if(item.resource.type == ResType.MATERIAL){
                        R.drawable.ic_materials_icon
                    } else {
                        R.drawable.ic_tools_icon
                    })
                    .setView(editText)
                    .setNegativeButton(R.string.cancel_button, null)
                    .setPositiveButton(R.string.ok_button) { _, _ ->
                        item.estimatePrice = editText.text.toString().toDouble()
                        Estimate.current?.update()
                        notifyItemChanged(position)
                    }
                    .show()
            }
        }
    }
}


