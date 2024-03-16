package com.buranchikov.astoncontacthomework4

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

class MainAdapter(private val onClickAction: (Contact) -> Unit) :
    ListAdapter<Contact, MainAdapter.ContactViewHolder>(DiffUtilContact()) {

    class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.checkBoxItem.visibility =
                if (MainActivity.isDeleteMode) View.VISIBLE else View.INVISIBLE
            binding.checkBoxItem.isChecked = contact.isSelected

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
                diffBundle.putString("name", newItem.name)
            }
            if (oldItem.secondName != newItem.secondName) {
                diffBundle.putString("secondName", newItem.secondName)
            }
            if (oldItem.phone != newItem.phone) {
                diffBundle.putString("phone", newItem.phone)
            }
            if (oldItem.photoURL != newItem.photoURL) {
                diffBundle.putString("photoURL", newItem.photoURL)
            }
            if (oldItem.gender != newItem.gender) {
                diffBundle.putString("gender", newItem.gender)
            }
            return if (diffBundle.isEmpty) null else diffBundle
        }
    }
}