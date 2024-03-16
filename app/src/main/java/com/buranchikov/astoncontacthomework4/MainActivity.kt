package com.buranchikov.astoncontacthomework4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.buranchikov.astoncontacthomework4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding
private lateinit var fragmentManager: FragmentManager
private val mainFragment = MainFragment()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val toolbar = binding.toolbar
    setSupportActionBar(toolbar)

    fragmentManager = supportFragmentManager
    getFragment(mainFragment)

    binding.btnCancel.setOnClickListener {
        binding.btnDelete.visibility = View.GONE
        binding.btnCancel.visibility = View.GONE
        mainFragment.toggleDeleteMode()
        mainFragment.setVisibleFab(View.VISIBLE)
        mainFragment.clearSelected()

    }
    binding.btnDelete.setOnClickListener {
        mainFragment.deleteSelectedItems()

    }
}
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
}
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    mainFragment.toggleDeleteMode()
    if (isDeleteMode) {
        binding.btnCancel.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        mainFragment.setVisibleFab(View.INVISIBLE)
    } else {
        binding.btnCancel.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE
        mainFragment.setVisibleFab(View.VISIBLE)
        mainFragment.clearSelected()
    }
    return true
}

private fun getFragment(fragment: Fragment) {
    fragmentManager.beginTransaction().replace(
        R.id.fragmentContainerView,
        fragment
    ).addToBackStack(null).commit()
}