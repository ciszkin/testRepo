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
import kotlinx.android.synthetic.main.fragment_resources.*


class ResourcesFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.title = getString(R.string.resources_menu_item_title)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resourcesText.text = Repository.resources.toString()
        resourcesText.movementMethod = ScrollingMovementMethod()
        resourcesLoadingBar.visibility = View.GONE
//        CoroutineScope(Dispatchers.IO).launch{
//            val resourcesListResponse = NetworkService.getBackendlessApi().getResourcesList(100).await()
//
//            if(resourcesListResponse.isSuccessful) {
//                withContext(Dispatchers.Main){
//                    resourcesText.text = resourcesListResponse.body().toString()
//                    resourcesText.movementMethod = ScrollingMovementMethod()
//                    resourcesLoadingBar.visibility = View.GONE
//                }
//            }
//        }
    }
}
