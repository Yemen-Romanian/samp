package com.application.android.samp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.android.samp.R

class FullMusicPlayerFragment: Fragment(){
    companion object {
        fun newInstance(): FullMusicPlayerFragment {
            return FullMusicPlayerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_music_player, container, false)
        return view
    }
}