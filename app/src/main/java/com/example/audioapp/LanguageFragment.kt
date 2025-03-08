package com.example.audioapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LanguageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    private var audioList: List<AudioFile> = emptyList()
    private var listener: AudioPlaybackListener? = null

    interface AudioPlaybackListener {
        fun onAudioSelected(audioFile: AudioFile)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        audioAdapter = AudioAdapter(
            audioList,
            { audioFile -> onAudioItemClick(audioFile) },
            { audioFile, view -> showPopupMenu(audioFile, view) }
        )

        recyclerView.adapter = audioAdapter
    }

    fun setAudioFiles(audios: List<AudioFile>) {
        audioList = audios
        if (::audioAdapter.isInitialized) {
            audioAdapter.updateData(audioList)
        }
    }

    fun setListener(listener: AudioPlaybackListener) {
        this.listener = listener
    }

    private fun onAudioItemClick(audioFile: AudioFile) {
        listener?.onAudioSelected(audioFile)
    }

    private fun showPopupMenu(audioFile: AudioFile, view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.menu_audio_item)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_play -> {
                    listener?.onAudioSelected(audioFile)
                    true
                }
                R.id.action_add_to_playlist -> {
                    // Implement add to playlist functionality
                    true
                }
                R.id.action_share -> {
                    // Implement share functionality
                    true
                }
                else -> false
            }
        }

        popup.show()
    }
}