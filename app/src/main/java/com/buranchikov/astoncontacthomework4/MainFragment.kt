package com.buranchikov.astoncontacthomework4

import android.content.Context
import android.content.res.XmlResourceParser
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.buranchikov.astoncontacthomework4.data.Contact
import com.buranchikov.astoncontacthomework4.databinding.FragmentMainBinding

class MainFragment() : Fragment() {
    private val LAST_ID = "lastId"
    private val NEW_CONTACT = "newContact"
    private val NEW_CONTACT_REQUEST = "newContactRequest"
    private val OLD_CONTACT = "oldContact"
    private lateinit var binding: FragmentMainBinding
    private var contactsList = mutableListOf<Contact>()
    lateinit var instanceActivity: DeleteModeChange

    private lateinit var adapter: MainAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        instanceActivity = context as MainActivity
        adapter = MainAdapter (instanceActivity as MainActivity){ contact ->
            editContact(contact)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        if (contactsList.isEmpty()) contactsList = setContactsListFromXML()
        instanceActivity.setVisibilityMenuDelete(true)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        setFragmentResultListener(NEW_CONTACT_REQUEST) { _, bundle ->
            val newContact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(NEW_CONTACT, Contact::class.java)
            } else bundle.getSerializable(NEW_CONTACT) as Contact

            var isReplace = false
            for (i in 0 until contactsList.size) {
                if (contactsList[i].id == newContact?.id) {
                    contactsList[i] = newContact
                    isReplace = true
                }
            }
            if (!isReplace) contactsList.add(newContact!!)
            submitList()
        }
        binding.mainRecyclerView.adapter = adapter
        submitList()

        binding.floatingActionButton.setOnClickListener {
            createNewContact()
        }
    }

    private fun submitList() {
        adapter.submitList(contactsList)
    }

    private fun createNewContact() {
        instanceActivity.resetDeleteMode()
        instanceActivity.showViewGroup(instanceActivity.isDeleteMode())
        val bundle = Bundle().apply {
            val lastId = contactsList.maxByOrNull { it.id }?.id
            if (lastId != null) {
                putInt(LAST_ID, lastId)
            }
        }
        navigateFragment(bundle)
    }

    private fun editContact(contact: Contact) {
        instanceActivity.resetDeleteMode()
        instanceActivity.showViewGroup(instanceActivity.isDeleteMode())
        val bundle = Bundle().apply {
            putSerializable(OLD_CONTACT, contact)
        }
        navigateFragment(bundle)
    }

    private fun navigateFragment(bundle: Bundle) {
        val newOrEditContactFragment = NewOrEditContactFragment()
        newOrEditContactFragment.arguments = bundle
        val fragmentManager = parentFragmentManager
        instanceActivity.setVisibilityMenuDelete(false)
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newOrEditContactFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setContactsListFromXML(): MutableList<Contact> {
        val dataXml = resources.getXml(R.xml.contacts_xml)
        val listContact = mutableListOf<Contact>()

        var id = 0
        var photoUrl = ""
        var name = ""
        var secondName = ""
        var phone = ""
        var gender = ""

        while (dataXml.eventType != XmlResourceParser.END_DOCUMENT) {
            when (dataXml.eventType) {
                XmlResourceParser.START_TAG -> {
                    when (dataXml.name) {
                        "item" -> {
                            id = 0
                            photoUrl = ""
                            name = ""
                            secondName = ""
                            phone = ""
                            gender = ""
                        }

                        "id" -> id = dataXml.nextText().toInt()
                        "photo_url" -> photoUrl = dataXml.nextText()
                        "first_name" -> name = dataXml.nextText()
                        "last_name" -> secondName = dataXml.nextText()
                        "phone" -> phone = dataXml.nextText()
                        "gender" -> gender = dataXml.nextText()
                    }
                }

                XmlResourceParser.END_TAG -> {
                    if (dataXml.name == "item") {
                        listContact.add(
                            Contact(
                                id = id,
                                name = name,
                                secondName = secondName,
                                phone = phone,
                                photoURL = photoUrl,
                                gender = gender
                            )
                        )
                    }
                }
            }
            dataXml.next()
        }
        return listContact
    }

    fun setVisibleFab(visible: Int) {
        binding.floatingActionButton.visibility = visible
    }

    fun notifyList() {
        adapter.notifyDataSetChanged()
    }

    fun deleteSelectedItems() {
        val selectedItems = adapter.currentList.filter { it.isSelected }
        contactsList.removeAll(selectedItems)
        notifyList()
    }

    fun clearSelected() {
        contactsList.forEach { it.isSelected = false }
    }


}