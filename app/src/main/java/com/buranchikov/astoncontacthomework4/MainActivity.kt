package com.buranchikov.astoncontacthomework4

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.buranchikov.astoncontacthomework4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), DeleteModeChange {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private val mainFragment = MainFragment.newInstance()
    private var isDeleteMode = false
    private var menuDeleteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        getFragment(mainFragment)
        isDeleteMode = savedInstanceState?.getBoolean(getString(R.string.isdeletemode)) ?: false

        binding.btnCancel.setOnClickListener {
            val mainFragment =
                fragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainFragment
            resetDeleteMode()
            showViewGroup(isDeleteMode)
            mainFragment?.notifyList()
            mainFragment?.clearSelected()
        }
        binding.btnDelete.setOnClickListener {
            val mainFragment =
                fragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainFragment
            mainFragment?.deleteSelectedItems()
        }

    }

    override fun onStart() {
        super.onStart()
        showViewGroup(isDeleteMode())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(getString(R.string.isdeletemode), isDeleteMode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuDeleteItem = menu?.findItem(R.id.deleteItemMenu)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mainFragment =
            fragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainFragment
        setDeleteMode()
        showViewGroup(isDeleteMode)
        mainFragment?.notifyList()
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

    override fun showViewGroup(isDeleteMode: Boolean) {
        if (isDeleteMode) {
            binding.buttonsContainer.visibility = View.VISIBLE
            val mainFragment =
                fragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainFragment
            mainFragment?.setVisibleFab(View.GONE)
        } else {
            binding.buttonsContainer.visibility = View.GONE
            val mainFragment =
                fragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainFragment
            mainFragment?.setVisibleFab(View.VISIBLE)
            mainFragment?.clearSelected()
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
}
