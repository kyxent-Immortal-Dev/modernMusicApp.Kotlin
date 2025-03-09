package com.example.audioapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: LanguagePagerAdapter

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var seekBar: SeekBar
    private lateinit var fabPlay: FloatingActionButton
    private lateinit var btnPause: ImageButton
    private lateinit var tvCurrentSong: TextView
    private lateinit var tvAlbumTitle: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var miniPlayerCard: View
    private var currentSongPosition = -1
    private var currentPlaylist: List<AudioFile> = emptyList()
    private var isPlaying = false

    private val seekBarUpdateRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    seekBar.progress = it.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for permissions
        if (!checkPermissions()) {
            requestPermissions()
        }

        // Initialize UI components
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        seekBar = findViewById(R.id.seekBar)
        fabPlay = findViewById(R.id.fabPlay)
        btnPause = findViewById(R.id.btnPause)
        tvCurrentSong = findViewById(R.id.tvCurrentSong)
        tvAlbumTitle = findViewById(R.id.tvAlbumTitle)
        tvArtistName = findViewById(R.id.tvArtistName)
        miniPlayerCard = findViewById(R.id.miniPlayerCard)

        // Hide mini player initially
        miniPlayerCard.visibility = View.GONE

        // Setup view pager
        pagerAdapter = LanguagePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "English" else "Español"
        }.attach()

        // Setup UI listeners
        setupListeners()

        // Load audio files
        loadAudioFiles()
    }

    private fun setupListeners() {
        fabPlay.setOnClickListener {
            if (currentSongPosition >= 0 && currentPlaylist.isNotEmpty()) {
                playAudio(currentPlaylist[currentSongPosition])
            }
        }

        btnPause.setOnClickListener {
            togglePlayPause()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<ImageButton>(R.id.btnShuffle).setOnClickListener {
            shufflePlaylist()
        }

        findViewById<ImageButton>(R.id.btnExpandPlayer).setOnClickListener {
            // Toggle seekbar visibility
            seekBar.visibility = if (seekBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun loadAudioFiles() {
        // Carga los números en inglés (1-10)
        val englishNumbers = listOf(
            AudioFile("1", "One", "Numbers", "English Numbers", 10, "english", R.raw.ingles1),
            AudioFile("2", "Two", "Numbers", "English Numbers", 10, "english", R.raw.ingles2),
            AudioFile("3", "Three", "Numbers", "English Numbers", 10, "english", R.raw.ingles3),
            AudioFile("4", "Four", "Numbers", "English Numbers", 10, "english", R.raw.ingles4),
            AudioFile("5", "Five", "Numbers", "English Numbers", 10, "english", R.raw.ingles5),
            AudioFile("6", "Six", "Numbers", "English Numbers", 10, "english", R.raw.ingles6),
            AudioFile("7", "Seven", "Numbers", "English Numbers", 10, "english", R.raw.ingles7),
            AudioFile("8", "Eight", "Numbers", "English Numbers", 10, "english", R.raw.ingles8),
            AudioFile("9", "Nine", "Numbers", "English Numbers", 10, "english", R.raw.ingles9),
            AudioFile("10", "Ten", "Numbers", "English Numbers", 10, "english", R.raw.ingles10)
        )

        // Carga los números en español (1-10)
        val spanishNumbers = listOf(
            AudioFile("11", "Uno", "Números", "Números en Español", 10, "spanish", R.raw.espanol1),
            AudioFile("12", "Dos", "Números", "Números en Español", 10, "spanish", R.raw.espanol2),
            AudioFile("13", "Tres", "Números", "Números en Español", 10, "spanish", R.raw.espanol3),
            AudioFile("14", "Cuatro", "Números", "Números en Español", 10, "spanish", R.raw.espanol4),
            AudioFile("15", "Cinco", "Números", "Números en Español", 10, "spanish", R.raw.espanol5),
            AudioFile("16", "Seis", "Números", "Números en Español", 10, "spanish", R.raw.espanol6),
            AudioFile("17", "Siete", "Números", "Números en Español", 10, "spanish", R.raw.espanol7),
            AudioFile("18", "Ocho", "Números", "Números en Español", 10, "spanish", R.raw.espanol8),
            AudioFile("19", "Nueve", "Números", "Números en Español", 10, "spanish", R.raw.espanol9),
            AudioFile("20", "Diez", "Números", "Números en Español", 10, "spanish", R.raw.espanol10)
        )

        // Update fragments with audio lists
        pagerAdapter.updateAudioLists(englishNumbers, spanishNumbers)
    }

    fun playAudio(audioFile: AudioFile) {
        // Update UI
        tvCurrentSong.text = audioFile.title
        tvAlbumTitle.text = audioFile.album
        tvArtistName.text = audioFile.artist

        // Show mini player
        miniPlayerCard.visibility = View.VISIBLE

        // Release previous media player if exists
        mediaPlayer?.release()

        // Create new media player with the specific audio resource
        mediaPlayer = MediaPlayer.create(this, audioFile.resourceId)
        mediaPlayer?.apply {
            seekBar.max = duration
            seekBar.progress = 0

            setOnCompletionListener {
                playNextSong()
            }

            start()

            updatePlayPauseButton()
            handler.post(seekBarUpdateRunnable)
        }

        // Update current position in playlist
        currentPlaylist = if (audioFile.language == "english") {
            pagerAdapter.getEnglishAudios()
        } else {
            pagerAdapter.getSpanishAudios()
        }

        currentSongPosition = currentPlaylist.indexOfFirst { it.id == audioFile.id }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                handler.removeCallbacks(seekBarUpdateRunnable)
                isPlaying = false
            } else {
                it.start()
                handler.post(seekBarUpdateRunnable)
                isPlaying = true
            }
            updatePlayPauseButton()
        }
    }

    private fun updatePlayPauseButton() {
        btnPause.setImageResource(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
    }

    private fun playNextSong() {
        if (currentPlaylist.isEmpty() || currentSongPosition < 0) return

        currentSongPosition = (currentSongPosition + 1) % currentPlaylist.size
        playAudio(currentPlaylist[currentSongPosition])
    }

    private fun shufflePlaylist() {
        if (currentPlaylist.isEmpty()) return

        val currentSong = if (currentSongPosition >= 0) currentPlaylist[currentSongPosition] else null
        val shuffledList = currentPlaylist.shuffled()

        // Update current song position in the shuffled list
        if (currentSong != null) {
            val newPosition = shuffledList.indexOfFirst { it.id == currentSong.id }
            if (newPosition >= 0) {
                currentSongPosition = newPosition
            }
        }

        // Update the adapter with shuffled list
        if (currentSong?.language == "english") {
            pagerAdapter.updateEnglishAudios(shuffledList)
        } else {
            pagerAdapter.updateSpanishAudios(shuffledList)
        }

        // Update current playlist reference with the shuffled list
        currentPlaylist = shuffledList
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacks(seekBarUpdateRunnable)
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
    }
}