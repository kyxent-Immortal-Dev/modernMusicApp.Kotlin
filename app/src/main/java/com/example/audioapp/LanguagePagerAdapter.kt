package com.example.audioapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LanguagePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val englishFragment = LanguageFragment()
    private val spanishFragment = LanguageFragment()

    private var englishAudios: List<AudioFile> = emptyList()
    private var spanishAudios: List<AudioFile> = emptyList()

    init {
        // Set listener for both fragments
        val listener = object : LanguageFragment.AudioPlaybackListener {
            override fun onAudioSelected(audioFile: AudioFile) {
                (activity as? MainActivity)?.playAudio(audioFile)
            }
        }

        englishFragment.setListener(listener)
        spanishFragment.setListener(listener)
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> englishFragment
            1 -> spanishFragment
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    fun updateAudioLists(english: List<AudioFile>, spanish: List<AudioFile>) {
        englishAudios = english
        spanishAudios = spanish

        englishFragment.setAudioFiles(englishAudios)
        spanishFragment.setAudioFiles(spanishAudios)
    }

    fun updateEnglishAudios(audios: List<AudioFile>) {
        englishAudios = audios
        englishFragment.setAudioFiles(englishAudios)
    }

    fun updateSpanishAudios(audios: List<AudioFile>) {
        spanishAudios = audios
        spanishFragment.setAudioFiles(spanishAudios)
    }

    fun getEnglishAudios(): List<AudioFile> = englishAudios

    fun getSpanishAudios(): List<AudioFile> = spanishAudios
}