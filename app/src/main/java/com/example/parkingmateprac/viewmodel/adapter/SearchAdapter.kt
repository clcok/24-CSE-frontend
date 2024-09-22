package com.example.parkingmateprac.viewmodel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingmateprac.R
import com.example.parkingmateprac.model.entity.LocationEntity
import com.example.parkingmateprac.viewmodel.OnSearchItemClickListener

class SearchAdapter(
    private val onSearchItemClickListener: OnSearchItemClickListener
) : ListAdapter<LocationEntity, SearchAdapter.SearchViewHolder>(
    object : DiffUtil.ItemCallback<LocationEntity>() {
        override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
            oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindViewHolder(item, onSearchItemClickListener)
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemList: RelativeLayout = view.findViewById(R.id.item_list)
        private val place: TextView = view.findViewById(R.id.place_name)
        private val address: TextView = view.findViewById(R.id.address)
        private val category: TextView = view.findViewById(R.id.category)

        fun bindViewHolder(item: LocationEntity, onSearchItemClickListener: OnSearchItemClickListener) {
            place.text = item.placeName
            address.text = item.addressName
            category.text = item.categoryGroupName

            itemList.setOnClickListener {
                onSearchItemClickListener.onSearchItemClick(item)
            }
        }
    }
}
