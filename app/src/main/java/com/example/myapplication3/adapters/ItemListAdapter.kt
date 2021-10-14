package com.example.myapplication3.adapters

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3.R
import com.example.myapplication3.models.LogItem
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.android.synthetic.main.item_location.view.tv_latitude
import kotlinx.android.synthetic.main.item_location.view.tv_longitude

class ItemListAdapter internal constructor() :
    RecyclerView.Adapter<ItemListAdapter.ViewHolderRecycleView>() {
    private var items = emptyList<LogItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListAdapter.ViewHolderRecycleView {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolderRecycleView(itemView)
    }

    override fun onBindViewHolder(holder: ItemListAdapter.ViewHolderRecycleView, position: Int) {
        val currentItem = items[position]

        if (currentItem.latitude != ""){
            holder.tv_latitude.text = currentItem.latitude
        }
        if (currentItem.longitude !=""){
            holder.tv_longitude.text = currentItem.longitude
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(_items:List<LogItem>) {
        items = _items
        notifyDataSetChanged()
    }

    inner class ViewHolderRecycleView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_latitude: TextView = itemView.tv_latitude
        val tv_longitude: TextView = itemView.tv_longitude

    }
}