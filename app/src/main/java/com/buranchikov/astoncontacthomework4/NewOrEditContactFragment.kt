package com.buranchikov.astoncontacthomework4

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.buranchikov.astoncontacthomework4.data.Contact
import com.buranchikov.astoncontacthomework4.databinding.FragmentNewContactBinding

class NewOrEditContactFragment : Fragment() {
    private val LAST_ID = "lastId"
    private val NEW_CONTACT = "newContact"
    private val NEW_CONTACT_REQUEST = "newContactRequest"
    private val OLD_CONTACT = "oldContact"

    lateinit var binding: FragmentNewContactBinding
    private var currentId = 0
    private var name = ""
    private var secondName = ""
    private var phone = ""
    private var gender = ""

    private var currentContact: Contact? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rbMan.setOnClickListener { onRadioButtonClick(it) }
        binding.rbWoman.setOnClickListener { onRadioButtonClick(it) }

    }

    override fun onStart() {
        super.onStart()
        //-----------------------------
        var id = arguments?.getInt(LAST_ID)!!
        if (id != null) {
            binding.btnAddNewContact.text = getString(R.string.add_btn_text)
            id++
            currentId = id
        }

        currentContact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(OLD_CONTACT, Contact::class.java)
        } else arguments?.getSerializable(OLD_CONTACT) as? Contact


        currentContact?.run {
            currentId = this.id
            binding.btnAddNewContact.text = getString(R.string.edit_btn_text)
            binding.etNameNew.setText(this.name)
            binding.etSecondNameNew.setText(this.secondName)
            binding.etPhone.setText(this.phone)
            when (this.gender) {
                getString(R.string.man_gender) -> binding.rbMan.isChecked = true
                getString(R.string.woman_gender) -> binding.rbWoman.isChecked = true
            }
        }

        //-----------------------------------
        binding.btnAddNewContact.setOnClickListener {
            name = binding.etNameNew.text.toString()
            secondName = binding.etSecondNameNew.text.toString()
            phone = binding.etPhone.text.toString()
            val newContact = Contact(
                id = currentId,
                name = name,
                secondName = secondName,
                phone = phone,
                gender = gender
            )
            val bundle = Bundle()
            bundle.putSerializable(NEW_CONTACT, newContact)
            setFragmentResult(NEW_CONTACT_REQUEST, bundle)


            val fragmentManager = parentFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun onRadioButtonClick(view: View) {
        val radioButton = view as RadioButton
        if (radioButton.isChecked) {
            when (radioButton.text) {
                getString(R.string.man_gender) -> gender = getString(R.string.man_gender)
                getString(R.string.woman_gender) -> gender = getString(R.string.woman_gender)
            }
        }
    }
}