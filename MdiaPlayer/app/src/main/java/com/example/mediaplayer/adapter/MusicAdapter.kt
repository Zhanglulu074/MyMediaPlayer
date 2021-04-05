package com.example.mediaplayer.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.MusicListItemBinding
import com.example.mediaplayer.viewmodel.MusicModel

class MusicAdapter :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private var musicList: ObservableArrayList<MusicModel>? = null
    private var context: Context? = null

    constructor(musicList: ObservableArrayList<MusicModel>?, context: Context?) : this(musicList, context, null)

    constructor(musicList: ObservableArrayList<MusicModel>?, context: Context?, onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
        this.musicList = musicList
        this.context = context
        this.musicList?.addOnListChangedCallback(object :ObservableList.OnListChangedCallback<ObservableArrayList<MusicModel>>() {
            override fun onChanged(sender: ObservableArrayList<MusicModel>?) {
                Log.d("zll", "onChanged: ")
            }

            override fun onItemRangeRemoved(
                sender: ObservableArrayList<MusicModel>?,
                positionStart: Int,
                itemCount: Int
            ) {

            }

            override fun onItemRangeMoved(
                sender: ObservableArrayList<MusicModel>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {

            }

            override fun onItemRangeInserted(
                sender: ObservableArrayList<MusicModel>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemInserted(positionStart)
//                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                sender: ObservableArrayList<MusicModel>?,
                positionStart: Int,
                itemCount: Int
            ) {

            }

        })
    }

    public var onItemClickListener: OnItemClickListener? = null

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
        onItemClickListener?.let {listener ->
            holder.itemBinding.root.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    inner class MusicViewHolder(var itemBinding: MusicListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }
}