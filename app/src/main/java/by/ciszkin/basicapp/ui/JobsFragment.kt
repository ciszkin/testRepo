package by.ciszkin.basicapp.ui

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.Repository
import by.ciszkin.basicapp.data.networking.NetworkService
import kotlinx.android.synthetic.main.fragment_jobs.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobsFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.title = getString(R.string.jobs_menu_item_title)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobsText.text = Repository.jobs.toString()
        jobsText.movementMethod = ScrollingMovementMethod()
        jobsLoadingBar.visibility = View.GONE
//        CoroutineScope(Dispatchers.IO).launch{
//            val jobsListResponse = NetworkService.getBackendlessApi().getJobsList(100).await()
//
//            if(jobsListResponse.isSuccessful) {
//                withContext(Dispatchers.Main){
//                    jobsText.text = jobsListResponse.body().toString()
//                    jobsText.movementMethod = ScrollingMovementMethod()
//                    jobsLoadingBar.visibility = View.GONE
//                }
//            }
//        }
    }
}
