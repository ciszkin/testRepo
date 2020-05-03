package by.ciszkin.basicapp.ui.fragments.resourceslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.RawJob
import by.ciszkin.basicapp.model.RawResource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.list_item_resources.view.*

class RawResourceAdapter(private val list: MutableList<RawResource>, private val listFragment: ResourcesListFragment) : RecyclerView.Adapter<RawResourceAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_resources, parent, false)
        return ResourceViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val view = holder.itemView
        val item = list[position]
        view.apply{
            resourceNameLabel.text = item.name
            resourcePriceValue.apply{
                text = item.price.toString()
                append(context.getString(R.string.currency))
                append(" / 1 ")
                append(item.units.title)
            }

            resourceImage.setImageResource(item.type.icon)

            setOnClickListener {
                if (RawJob.current.value != null) {
                    val editConsumption = EditText(this.context)
                    editConsumption.apply {
                        setRawInputType(2002)
                        isSingleLine = true
                        hint = context.getString(R.string.input_job_amount_label)
                    }

                    MaterialAlertDialogBuilder(this.context)
                        .setTitle(context.getString(R.string.add_resource_to_new_job_label))
                        .setIcon(R.drawable.ic_add_resource_icon)
                        .setMessage(item.name)
                        .setView(editConsumption)
                        .setNegativeButton(R.string.cancel_button) { _, _ ->
                            listFragment.findNavController().navigateUp()
                        }
                        .setPositiveButton(R.string.ok_button) { _, _ ->
                            RawResource.current.postValue(Pair(item, editConsumption.text.toString().toDouble()))
                            listFragment.findNavController().navigateUp()
                        }
                        .show()
                }
            }
        }
    }

}