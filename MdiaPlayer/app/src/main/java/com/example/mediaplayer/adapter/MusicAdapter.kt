package com.example.mediaplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.MusicListItemBinding
import com.example.mediaplayer.viewmodel.MusicModel

class MusicAdapter(var musicList: List<MusicModel>?, var context: Context?) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val itemBinding: MusicListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.music_list_item, parent, false
        )
        return MusicViewHolder(itemBinding)

    }

    override fun getItemCount(): Int {
        return musicList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        musicList?.let {
            holder.itemBinding.musicModel = it[position]
        }
    }


    inner class MusicViewHolder(var itemBinding: MusicListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }
}