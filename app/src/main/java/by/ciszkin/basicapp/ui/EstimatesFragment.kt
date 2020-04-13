package by.ciszkin.basicapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.ciszkin.basicapp.R
import kotlinx.android.synthetic.main.fragment_estimates.*

class EstimatesFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.title = getString(R.string.estimates_menu_item_title)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_estimatesFragment_to_jobsFragment)
        }
    }
}
