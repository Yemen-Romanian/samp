package com.application.android.samp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.android.samp.AudioTrack
import com.application.android.samp.R
import com.application.android.samp.viewmodels.MainViewModel

private const val TAG = "MainAudioTrackListFragment"

class MainAudioTrackListFragment: Fragment() {
    private lateinit var audioRecyclerView: RecyclerView
    private var adapter: AudioTrackAdapter? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    companion object {
        fun newInstance(): MainAudioTrackListFragment {
            return MainAudioTrackListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Overall number of tracks ${mainViewModel.audioTracks.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_main_audio_track_list,
            container,
            false
        );
        audioRecyclerView =
            view.findViewById(R.id.audio_recycler_view) as RecyclerView
        audioRecyclerView.layoutManager = LinearLayoutManager(context)

        updateAudioList()

        return view;
    }

    private fun updateAudioList() {
        val audioTracks = mainViewModel.audioTracks
        adapter = AudioTrackAdapter(audioTracks)
        audioRecyclerView.adapter = adapter
    }

    private inner class AudioTrackHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
            private lateinit var track: AudioTrack
            private val titleTextView: TextView = itemView.findViewById(R.id.track_name)
            private val artistTextView: TextView = itemView.findViewById(R.id.track_artist)
            private val albumCover: ImageView = itemView.findViewById(R.id.track_album_cover)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(track: AudioTrack) {
            this.track = track
            titleTextView.text = track.title
            artistTextView.text = track.artist
            albumCover.setImageResource(R.drawable.ic_baseline_album_24)
        }

        override fun onClick(p0: View?) {
            Toast.makeText(
                context,
                "${track.title} of ${track.artist} is playing!",
                Toast.LENGTH_SHORT
            ).show()
        }

        }

    private inner class AudioTrackAdapter(var audioTracks: List<AudioTrack>)
        : RecyclerView.Adapter<AudioTrackHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioTrackHolder {
            val view = layoutInflater.inflate(R.layout.list_item_audio_track, parent, false)
            return AudioTrackHolder(view)
        }

        override fun onBindViewHolder(holder: AudioTrackHolder, position: Int) {
            holder.bind(audioTracks[position])
        }

        override fun getItemCount() = audioTracks.size
    }
}