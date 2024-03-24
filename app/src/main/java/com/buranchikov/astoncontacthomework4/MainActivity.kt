package com.buranchikov.astoncontacthomework4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.buranchikov.astoncontacthomework4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), DeleteModeChange {
    private val TAG = "myLog"
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private val mainFragment = MainFragment.newInstance()
    private var isDeleteMode = false
    private var menuDeleteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)


        getFragment(mainFragment)

        binding.btnCancel.setOnClickListener {
            resetDeleteMode()
            showViewGroup(isDeleteMode)
            mainFragment.notifyList()
            mainFragment.clearSelected()

        }
        binding.btnDelete.setOnClickListener {
            mainFragment.deleteSelectedItems()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuDeleteItem = menu?.findItem(R.id.deleteItemMenu)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setDeleteMode()
        showViewGroup(isDeleteMode)
        mainFragment.notifyList()
        return true
    }

    private fun getFragment(fragment: Fragment) {
        val currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (currentFragment == null) {
            fragmentManager.beginTransaction().replace(
                R.id.fragmentContainerView,
                fragment
            ).commit()
        }
    }

    override fun showViewGroup(deleteMode: Boolean) {
        if (deleteMode) {
            binding.buttonsContainer.visibility = View.VISIBLE
            mainFragment.setVisibleFab(View.INVISIBLE)
        } else {
            binding.buttonsContainer.visibility = View.GONE
            mainFragment.setVisibleFab(View.VISIBLE)
            mainFragment.clearSelected()
        }
    }

    override fun setDeleteMode() {
        isDeleteMode = true
    }

    override fun resetDeleteMode() {
        isDeleteMode = false
    }

    override fun isDeleteMode() = isDeleteMode
    override fun setVisibilityMenuDelete(mode: Boolean) {
        menuDeleteItem?.isVisible = mode

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy: Activity")
    }
}
