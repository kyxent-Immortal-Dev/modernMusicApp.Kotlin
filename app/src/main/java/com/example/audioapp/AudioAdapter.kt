package com.example.audioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AudioAdapter(
    private var audioList: List<AudioFile>,
    private val onItemClick: (AudioFile) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFile = audioList[position]
        holder.bind(audioFile)
    }

    override fun getItemCount(): Int = audioList.size

    fun updateList(newList: List<AudioFile>) {
        audioList = newList
        notifyDataSetChanged()
    }

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        private val imgNumber: ImageView = itemView.findViewById(R.id.imgNumber)

        fun bind(audioFile: AudioFile) {
            tvTitle.text = audioFile.title
            tvArtist.text = audioFile.artist

            val resourceName = when (audioFile.language) {
                "english" -> "number_${audioFile.title.toLowerCase()}"
                else -> "numero_${audioFile.id.toIntOrNull() ?: 0 - 10}"
            }

            val resourceId = itemView.context.resources.getIdentifier(
                resourceName, "drawable", itemView.context.packageName
            )

            if (resourceId != 0) {
                imgNumber.setImageResource(resourceId)
            } else {

                imgNumber.setImageResource(R.drawable.ic_play)
            }

            itemView.setOnClickListener {
                onItemClick(audioFile)
            }
        }
    }
}