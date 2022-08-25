package com.application.android.samp.data.sources

import com.application.android.samp.data.AudioTrack

interface IAudioTrackSource {
    fun getAllAudioTracks(): List<AudioTrack>
}