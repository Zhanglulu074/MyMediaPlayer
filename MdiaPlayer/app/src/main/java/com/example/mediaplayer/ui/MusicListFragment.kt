package com.example.mediaplayer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.MusicAdapter
import com.example.mediaplayer.databinding.LayoutMusicListFragmentBinding
import com.example.mediaplayer.service.MusicCrtlCenter
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import com.example.mediaplayer.viewmodel.MainPageViewModel
import com.example.mediaplayer.viewmodel.MusicModel

class MusicListFragment : Fragment {

    private var logTag: String = ""
    private var isLoaded = false
    private var isVisibleToUser = false
    private var isCallResume = false
    private var binding: LayoutMusicListFragmentBinding? = null
    private var musicListType: Int = 0


    constructor() : super()
    constructor(listType: Int) {
        musicListType = listType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate: ")
    }

    override fun onStart() {
        super.onStart()
        isCallResume = true
        Log.d(logTag, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        loadData()
        Log.d(logTag, "onResume: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(logTag, "onDestroyView: ")
        isVisibleToUser = false
        isLoaded = false
        isCallResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.layout_music_list_fragment, container, false)
        return binding?.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        Log.d(logTag, "setUserVisibleHint: $isVisibleToUser")
        loadData()
    }


    private fun loadData() {
        if (!isLoaded) {
            binding?.apply {
                val musicList =
                    if (musicListType == 0) MainPageViewModel.instance.localMusicList else MainPageViewModel.instance.localMusicList
                val musicModelAdapter = MusicAdapter(musicList, this@MusicListFragment.context, object : MusicAdapter.OnItemClickListener {
                    override fun onClick(position: Int) {
                        MusicCrtlCenter.instance.switchTo(position)
                    }
                })
                musicListView.layoutManager = LinearLayoutManager(context)
                musicListView.adapter = musicModelAdapter
                musicListView.isNestedScrollingEnabled = true
//                musicListView.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
//                object : View.OnTouchListener {
//                    @SuppressLint("ClickableViewAccessibility")
//                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                        v?.let {
//                            var parent = v.parent
//                            while ((parent !is CoordinatorLayout)) {
//                                parent = parent.parent
//                            }
//                            parent.requestDisallowInterceptTouchEvent(true)
//                        }
////                        musicListView.onTouchEvent(event)
//                        return true
//                    }
//
//                })
            }
            isLoaded = true
        }
    }
}