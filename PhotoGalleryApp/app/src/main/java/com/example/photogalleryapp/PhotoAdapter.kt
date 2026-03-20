package com.example.photogalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(
    private val context: Context,
    private var photoList: List<Photo>,
    private var selectionMode: Boolean = false
) : BaseAdapter() {

    override fun getCount(): Int = photoList.size

    override fun getItem(position: Int): Any = photoList[position]

    override fun getItemId(position: Int): Long = photoList[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        val photo = photoList[position]

        val imgPhoto: ImageView = view.findViewById(R.id.imgPhoto)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val chkSelected: CheckBox = view.findViewById(R.id.chkSelected)

        imgPhoto.setImageResource(photo.resourceId)
        txtTitle.text = photo.title
        
        if (selectionMode) {
            chkSelected.visibility = View.VISIBLE
            chkSelected.isChecked = photo.isSelected
            chkSelected.isClickable = false
            chkSelected.isFocusable = false
        } else {
            chkSelected.visibility = View.GONE
        }

        return view
    }

    fun updateData(newList: List<Photo>) {
        photoList = newList
        notifyDataSetChanged()
    }

    fun setSelectionMode(enabled: Boolean) {
        selectionMode = enabled
        notifyDataSetChanged()
    }

    fun getSelectionMode(): Boolean = selectionMode
}
