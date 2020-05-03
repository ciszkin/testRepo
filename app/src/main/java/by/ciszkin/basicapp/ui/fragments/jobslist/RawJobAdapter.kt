package by.ciszkin.basicapp.ui.fragments.jobslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.RawJob
import kotlinx.android.synthetic.main.list_item_jobs.view.*

class RawJobAdapter(
    private val list: MutableList<RawJob>, private val listFragment: JobsListFragment
) : RecyclerView.Adapter<RawJobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_jobs, parent, false)
        return JobViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val view = holder.itemView
        val item = list[position]
        view.apply {
            jobNameLabel.text = item.name
            jobPriceLabel.apply {
                text = item.price.toString()
                append(context.getString(R.string.currency))
                append(" / 1")
                append(item.units.title)
            }

            resourceImage.setImageResource(item.type.icon)
            surfaceImage.setImageResource(item.surface.icon)

            setOnClickListener{
                RawJob.current.postValue(item)
                listFragment.findNavController().navigate(R.id.jobDetailedFragment)
            }
        }
    }
}