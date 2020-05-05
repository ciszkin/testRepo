package by.ciszkin.basicapp.ui.activities.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.Repository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener,
    FragmentManager.OnBackStackChangedListener, SearchView.OnQueryTextListener {

    private lateinit var model: MainViewModel
    private lateinit var searchView: SearchView

    interface OnFabClickListener {
        fun onFabClick()
    }

    interface OnSearchListener {
        fun performSearch(query: String?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(this)

        navHostFragment.childFragmentManager.addOnBackStackChangedListener(this)

        bottomNav.setupWithNavController(navHostFragment.findNavController())

        model = ViewModelProvider(this, MainViewModelFactory(application, Repository)).get(MainViewModel::class.java)

        model.currentFragmentName.observe(this, Observer{
            this.title = getString(it)
        })

        model.currentFabIcon.observe(this, Observer{
            fab.setImageResource(it)
        })

        model.currentFabVisibility.observe(this, Observer{
            fab.visibility = it
        })



//                    Snackbar.make(
//                        view,
//                         Snackbar.LENGTH_LONG
//                    )
//                       "Replace with your own action",
////                        .setAction("Action", null).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setOnQueryTextListener(this)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    override fun onStop() {
        super.onStop()
        model.updateDbData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.estimatesListFragment -> {
                navHostFragment.findNavController().navigate(R.id.estimatesListFragment)
                true
            }
            R.id.jobsListFragment -> {
                navHostFragment.findNavController().navigate(R.id.jobsListFragment)
                true
            }
            R.id.resourcesListFragment -> {
                navHostFragment.findNavController().navigate(R.id.resourcesListFragment)
                true
            }
            R.id.actionSettings -> {
                navHostFragment.findNavController().navigate(R.id.mySettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {

        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        when (view?.id) {
            fab.id -> {
                if (currentFragment is OnFabClickListener) {
                    currentFragment.onFabClick()
                }
            }

        }
    }

    override fun onBackStackChanged() {

        when (navHostFragment.findNavController().currentDestination?.id) {
            R.id.estimatesListFragment -> {
                model.currentFragmentName.value = R.string.estimates_list_menu_item_title
                model.currentFabIcon.value = R.drawable.ic_new_estimate_icon
                model.currentFabVisibility.value = View.VISIBLE

            }
            R.id.jobsListFragment -> {
                model.currentFragmentName.value = R.string.jobs_list_menu_item_title
                model.currentFabIcon.value = R.drawable.ic_new_job_icon
                model.currentFabVisibility.value = View.VISIBLE

            }
            R.id.resourcesListFragment -> {
                model.currentFragmentName.value = R.string.resources_list_menu_item_title
                model.currentFabIcon.value = R.drawable.ic_new_resource_icon
                model.currentFabVisibility.value = View.VISIBLE

            }
            R.id.estimateJobsListFragment -> {
                model.currentFragmentName.value = R.string.jobs_list_title
                model.currentFabIcon.value = R.drawable.ic_add_job_icon
                model.currentFabVisibility.value = View.VISIBLE

            }
            R.id.jobDetailedFragment -> {
                model.currentFragmentName.value = R.string.job_card_title
                model.currentFabIcon.value = R.drawable.ic_add_job_icon
                model.currentFabVisibility.value = View.VISIBLE

            }
            R.id.newEstimateFragment -> {
                model.currentFragmentName.value = R.string.new_estimate_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
            R.id.estimateDetailedFragment -> {
                model.currentFragmentName.value = R.string.estimate_card_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
            R.id.estimateResourcesListFragment -> {
                model.currentFragmentName.value = R.string.resources_list_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
            R.id.newJobFragment -> {
                model.currentFragmentName.value = R.string.new_job_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
            R.id.newResourceFragment -> {
                model.currentFragmentName.value = R.string.new_resource_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
            R.id.mySettingsFragment -> {
                model.currentFragmentName.value = R.string.settings_menu_item_title
                model.currentFabVisibility.value = View.INVISIBLE

            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        search(query)
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        search(query)
        return true
    }

    private fun search(query: String?) {
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        if (currentFragment is OnSearchListener) {
            currentFragment.performSearch(query)
        }
    }
}