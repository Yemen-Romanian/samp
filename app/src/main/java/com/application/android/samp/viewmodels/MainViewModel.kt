package com.application.android.samp.viewmodels

import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.application.android.samp.AudioTrack

class MainViewModel: ViewModel() {
    val audioTracks = mutableListOf<AudioTrack>()

    init {
        for (i in 0 until 10) {
            val track = AudioTrack()
            track.title = "Track ${i + 1}"
            track.artist = "The Beatles"
            audioTracks += track
        }
    }
}