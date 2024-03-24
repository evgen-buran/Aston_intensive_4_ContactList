package com.buranchikov.astoncontacthomework4

interface DeleteModeChange {
    fun showViewGroup(isDeleteMode:Boolean)
    fun  setDeleteMode()
    fun  resetDeleteMode()
    fun isDeleteMode():Boolean
    fun setVisibilityMenuDelete(mode:Boolean)
}