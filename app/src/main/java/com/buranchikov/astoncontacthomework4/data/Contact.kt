package com.buranchikov.astoncontacthomework4.data

import java.io.Serializable

data class Contact(
    val id: Int,
    val name: String,
    val secondName: String,
    val phone: String,
    val photoURL: String = "https://placekitten.com/100/100",
    val gender: String,
    var isSelected: Boolean = false
):Serializable
