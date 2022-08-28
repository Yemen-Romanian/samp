package com.application.android.samp.data.sources

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.application.android.samp.data.AudioTrack
import java.io.File

private const val TAG = "InternalStorageAudioTrackSource"

class InternalStorageAudioTrackSource (
    private val context: Context,
    private var directoriesToSearch: List<String>? = null) : IAudioTrackSource{

    private val externalStoragePath =
        Environment.getExternalStorageDirectory().absolutePath + File.separator;

    init {
        directoriesToSearch = directoriesToSearch ?: arrayListOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath + File.separator
        )
    }

    private var mediaDataRetriever: MediaMetadataRetriever = MediaMetadataRetriever()

    // add more extensions for future (without dot)
    private var musicFileExtensions = arrayListOf(
        "mp3", "wav", "oog"
    )

    override fun getAllAudioTracks(): List<AudioTrack> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            getAllAudioTracksForNewerVersions()
        } else {
            getAllAudioTrackForOlderVersions()
        }
    }

    /**
     * Function for API version < Q (Android 10)
     */
    private fun getAllAudioTrackForOlderVersions(): List<AudioTrack> {
        val tracks = mutableListOf<AudioTrack>()

        // maybe there is a better way for loading this file
        directoriesToSearch?.forEach {
            val musicDirectory = File(it)
            val musicFiles = musicDirectory.listFiles()?.filter {
                    file ->
                    file.isFile
                    file.absolutePath.split(".").last() in musicFileExtensions
            } ?: emptyList()

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
        }

        Log.i(TAG, "Loaded ${tracks.size} tracks overall" )

        return tracks
    }

    /**
     * Function for API version >= Q (Android 10)
     */
    private fun getAllAudioTracksForNewerVersions(): List<AudioTrack> {
        val tracks = mutableListOf<AudioTrack>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " =? "
        val selectionArg = arrayOf("1")

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DISPLAY_NAME
            ),
            selection,
            selectionArg,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER) ?: return tracks

        while (cursor.moveToNext()) {
            val mname = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH))
            val misMusic = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC))
            val mtitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val martist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val malbum = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
            val dispName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
            Log.i(TAG, "Uploaded name ${mname}, is music = ${misMusic}, title ${mtitle}, artist ${martist}, dispName ${dispName}")

            val track = AudioTrack()
            track.apply {
                title = mtitle
                artist = martist
                album = malbum
                filePath = externalStoragePath + mname
            }
            tracks += track
        }

        cursor.close()

        return tracks
    }
}