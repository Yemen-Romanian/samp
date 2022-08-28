package com.application.android.samp

import android.app.Application
import com.application.android.samp.data.AudioTrackRepository

class SAMPApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        AudioTrackRepository.initialize(this)
    }
}