package com.example.audioapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AudioListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AudioAdapter
    private var audioList: List<AudioFile> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_audio_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        adapter = AudioAdapter(audioList) { audioFile ->
            (activity as? MainActivity)?.playAudio(audioFile)
        }

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }

    fun updateAudioList(newList: List<AudioFile>) {
        audioList = newList
        if (::adapter.isInitialized) {
            adapter.updateList(newList)
        }
    }

    companion object {
        fun newInstance(language: String): AudioListFragment {
            val fragment = AudioListFragment()
            val args = Bundle()
            args.putString("language", language)
            fragment.arguments = args
            return fragment
        }
    }
}