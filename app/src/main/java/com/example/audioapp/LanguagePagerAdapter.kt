package com.example.audioapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LanguagePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val englishFragment = AudioListFragment.newInstance("english")
    private val spanishFragment = AudioListFragment.newInstance("spanish")

    private var englishAudios: List<AudioFile> = emptyList()
    private var spanishAudios: List<AudioFile> = emptyList()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) englishFragment else spanishFragment
    }

    fun updateAudioLists(englishList: List<AudioFile>, spanishList: List<AudioFile>) {
        englishAudios = englishList
        spanishAudios = spanishList

        englishFragment.updateAudioList(englishList)
        spanishFragment.updateAudioList(spanishList)
    }

    fun updateEnglishAudios(audios: List<AudioFile>) {
        englishAudios = audios
        englishFragment.updateAudioList(audios)
    }

    fun updateSpanishAudios(audios: List<AudioFile>) {
        spanishAudios = audios
        spanishFragment.updateAudioList(audios)
    }

    fun getEnglishAudios(): List<AudioFile> = englishAudios

    fun getSpanishAudios(): List<AudioFile> = spanishAudios
}