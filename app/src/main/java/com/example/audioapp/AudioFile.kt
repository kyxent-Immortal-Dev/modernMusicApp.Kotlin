package com.example.audioapp

/**
 * Model class to represent an audio file
 */
data class AudioFile(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int, // in seconds
    val language: String // "english" or "spanish"
) {
    fun getDurationString(): String {
        val minutes = duration / 60
        val seconds = duration % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}