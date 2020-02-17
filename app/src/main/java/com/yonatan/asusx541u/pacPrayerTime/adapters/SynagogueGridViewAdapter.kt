package com.yonatan.asusx541u.pacPrayerTime.adapters

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.databinding.ItemSynagogueGridViewBinding

class SynagogueGridViewAdapter(private val listSynagogue : ArrayList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<SynagogueGridViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = DataBindingUtil.inflate<ItemSynagogueGridViewBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.item_synagogue_grid_view,
                parent,
                false
        )
        binding.synagogueGridViewAdapter = this
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSynagogue.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.onBind(listSynagogue[position], position)
    }

    inner class ViewHolder(private val binding: ItemSynagogueGridViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun onBind(synagogue: String, position: Int) {
            binding.synagogue = synagogue
            if (position == listSynagogue.size-1){
                binding.viewDiv.visibility = View.INVISIBLE
            }
        }

    }
}