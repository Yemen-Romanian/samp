package com.application.android.samp.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.application.android.samp.data.sources.IAudioTrackSource
import com.application.android.samp.data.sources.InternalStorageAudioTrackSource
import java.lang.IllegalStateException

class AudioTrackRepository private constructor(
    context: Context,
    private val source: IAudioTrackSource // for this moment, only one data source will be available
    ){

    companion object {
        private var instance: AudioTrackRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = AudioTrackRepository(context, InternalStorageAudioTrackSource(context))
            }
        }

        fun get(): AudioTrackRepository {
            return instance?:
            throw IllegalStateException("AudioTrack Repository must first be initialized")
        }
    }

    fun getAllAudioTracks(): List<AudioTrack> {
        return source.getAllAudioTracks()
    }
}