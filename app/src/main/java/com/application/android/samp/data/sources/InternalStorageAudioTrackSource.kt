package com.application.android.samp.data.sources

import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.application.android.samp.data.AudioTrack
import java.io.File

private const val TAG = "InternalStorageAudioTrackSource"

class InternalStorageAudioTrackSource (): IAudioTrackSource{
    private var mediaDataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()

    // add more extensions for future (without dot)
    private var musicFileExtensions = arrayListOf(
        "mp3", "wav", "oog"
    )

    override fun getAllAudioTracks(): List<AudioTrack> {
        val tracks = mutableListOf<AudioTrack>()

        // maybe there is a better way for loading this file
        val musicDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
        )

        val musicFiles = musicDirectory.listFiles().filter {
            it.isFile
            it.absolutePath.split(".").last() in musicFileExtensions
        }

        for (file in musicFiles) {
            val track = AudioTrack()
            mediaDataRetriever.setDataSource(file.absolutePath)
            track.apply {
                title =
                    mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
                album =
                    mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
                artist =
                    mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
                filePath = file.absolutePath
            }
            tracks += track
        }

        Log.i(TAG, "Loaded ${tracks.size} tracks overall" )
        return tracks
    }
}