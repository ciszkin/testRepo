package by.ciszkin.basicapp.ui.fragments.jobdetailed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.RawResource
import kotlinx.android.synthetic.main.list_item_consumption.view.*

class ConsumptionAdapter(val list: MutableList<Pair<RawResource, Double>>) : RecyclerView.Adapter<ConsumptionAdapter.ConsumptionViewHolder>() {

    class ConsumptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_consumption, parent, false)
        return ConsumptionViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ConsumptionViewHolder, position: Int) {
        val item = holder.itemView
        item.apply{
            resourceNameLabel.text = list[position].first.name
            resourcePriceValue.apply{
                text = list[position].first.price.toString()
                append(context.getString(R.string.currency))
                append(" / 1 ")
                append(list[position].first.units.title)
            }

            resourceImage.setImageResource(list[position].first.type.icon)
            consumptionValue.apply {
                text = list[position].second.toString()
                append(" ")
                append(list[position].first.units.title)
            }

            setOnClickListener { }
        }
    }

}