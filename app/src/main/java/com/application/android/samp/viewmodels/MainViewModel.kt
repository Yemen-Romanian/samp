package com.application.android.samp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.android.samp.data.AudioTrack
import com.application.android.samp.data.AudioTrackRepository

private const val TAG = "MainViewModel"

class MainViewModel: ViewModel() {
    val audioTracks: MutableLiveData<List<AudioTrack>> = MutableLiveData()
    private val audioRepository = AudioTrackRepository.get()

    init {
        fetchAudioTracks()
    }

    fun fetchAudioTracks() {
        audioTracks.value = audioRepository.getAllAudioTracks()
    }
}