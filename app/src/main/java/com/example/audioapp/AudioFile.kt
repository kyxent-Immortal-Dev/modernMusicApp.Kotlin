package com.example.audioapp

// Clase AudioFile modificada para incluir el resourceId del archivo de audio
data class AudioFile(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int,
    val language: String,
    val resourceId: Int  // Nuevo campo para el ID del recurso del archivo de audio
)