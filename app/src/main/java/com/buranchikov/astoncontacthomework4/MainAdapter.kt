package com.buranchikov.astoncontacthomework4

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.buranchikov.astoncontacthomework4.data.Contact
import com.buranchikov.astoncontacthomework4.databinding.ContactItemBinding

private const val NAME_KEY = "name_key"
private const val SECOND_NAME_KEY = "second_name_key"
private const val PHONE_KEY = "phone_key"
private const val PHOTO_KEY = "photo_key"
private const val GENDER_KEY = "gender_key"

class MainAdapter(val mainActivity: MainActivity, private val onClickAction: (Contact) -> Unit) :
    ListAdapter<Contact, MainAdapter.ContactViewHolder>(DiffUtilContact()) {

    inner class ContactViewHolder(val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.checkBoxItem.visibility =
                if (mainActivity.isDeleteMode()!!) View.VISIBLE else View.INVISIBLE
            binding.checkBoxItem.isChecked = contact.isSelected
            if (contact.isSelected) binding.checkBoxItem.isChecked

            binding.tvIdContactItem.text = "#${contact.id}"
            binding.tvNameItem.text = contact.name
            binding.tvSecondNameItem.text = contact.secondName
            binding.tvPhoneItem.text = contact.phone
            binding.ivPhotoItem.load(contact.photoURL) {
                scale(Scale.FILL).size(
                    binding.root.resources.getDimension(R.dimen.avatar_size_big).toInt()
                )
            }
            binding.checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
                contact.isSelected = isChecked
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        val holder = ContactViewHolder(binding)
        binding.root.setOnClickListener {
            val model = getItem(holder.adapterPosition)
            onClickAction(model)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.first() is Bundle) {
            val newContact = payloads.first() as Bundle
            holder.binding.tvNameItem.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    holder.binding.tvNameItem.text = newContact.getString(NAME_KEY)
                    holder.binding.tvNameItem.animate()
                        .alpha(1f)
                        .setDuration(300)
                }

        } else super.onBindViewHolder(holder, position, payloads)
    }
    class DiffUtilContact : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Contact, newItem: Contact): Any? {
            val diffBundle = Bundle()

            if (oldItem.name != newItem.name) {
                diffBundle.putString(NAME_KEY, newItem.name)
            }
            if (oldItem.secondName != newItem.secondName) {
                diffBundle.putString(SECOND_NAME_KEY, newItem.secondName)
            }
            if (oldItem.phone != newItem.phone) {
                diffBundle.putString(PHONE_KEY, newItem.phone)
            }
            if (oldItem.photoURL != newItem.photoURL) {
                diffBundle.putString(PHOTO_KEY, newItem.photoURL)
            }
            if (oldItem.gender != newItem.gender) {
                diffBundle.putString(GENDER_KEY, newItem.gender)
            }
            return if (diffBundle.isEmpty) null else diffBundle
        }
    }
}