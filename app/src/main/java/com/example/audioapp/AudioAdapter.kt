package com.example.audioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AudioAdapter(
    private var audioList: List<AudioFile>,
    private val onItemClick: (AudioFile) -> Unit,
    private val onMoreButtonClick: (AudioFile, View) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        val btnMore: ImageButton = view.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFile = audioList[position]

        holder.tvNumber.text = (position + 1).toString()
        holder.tvTitle.text = audioFile.title
        holder.tvArtist.text = audioFile.artist
        holder.tvDuration.text = audioFile.getDurationString()

        holder.itemView.setOnClickListener {
            onItemClick(audioFile)
        }

        holder.btnMore.setOnClickListener {
            onMoreButtonClick(audioFile, holder.btnMore)
        }
    }

    override fun getItemCount() = audioList.size

    fun updateData(newList: List<AudioFile>) {
        audioList = newList
        notifyDataSetChanged()
    }
}