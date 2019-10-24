package com.yonatan.asusx541u.pacPrayerTime.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.databinding.ItemSynagogueGridViewBinding

class SynagogueGridViewAdapter(private val listSynagogue : ArrayList<String>) : RecyclerView.Adapter<SynagogueGridViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
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

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind(listSynagogue[position])
    }

    inner class ViewHolder(private val binding: ItemSynagogueGridViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(synagogue: String) {
            binding.synagogue = synagogue
        }

    }
}