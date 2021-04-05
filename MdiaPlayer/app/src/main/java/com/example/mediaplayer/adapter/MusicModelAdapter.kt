package com.example.mediaplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.databinding.MusicListItemBinding
import com.example.mediaplayer.viewmodel.MusicModel

class MusicModelAdapter(context: Context, resource: Int, objects: MutableList<MusicModel>) :
    ArrayAdapter<MusicModel>(context, resource, objects) {

    private var resourceId: Int = resource
    private var viewBind: MusicListItemBinding? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        viewBind = if (convertView == null) {
            DataBindingUtil.inflate(LayoutInflater.from(context), resourceId, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView)
        }
        viewBind?.musicModel = getItem(position) as MusicModel
        return viewBind?.root!!
    }
}