package com.example.game

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.game.databinding.ActivitySettingsBinding

const val PREF_SOUND = "com.example.game.SOUND"
const val PREF_LEVEL = "com.example.game.LEVEL"
const val PREF_RULES = "com.example.game.RULES"


class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding

    private var currentLevel : Int = 0
    private var currentVolumeSound: Int = 0
    private var currentRules: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)

        val currentSettings = getCurrentSettings()

        currentLevel = currentSettings.level
        currentVolumeSound = currentSettings.sound
        currentRules = currentSettings.rules

        if(currentLevel == 0){
            settingsBinding.prewLvl.visibility = View.INVISIBLE
        } else if (currentLevel == 2) {
            settingsBinding.nextLvl.visibility = View.INVISIBLE
        }

        settingsBinding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
        settingsBinding.seekBar.progress = currentVolumeSound

        when(currentSettings.rules){
            1 -> settingsBinding.checkBoxHerizontal.isChecked = true
            2 -> settingsBinding.checkBoxVertical.isChecked = true
            3 -> {
                settingsBinding.checkBoxHerizontal.isChecked = true
                settingsBinding.checkBoxVertical.isChecked = true
            }
            4 -> settingsBinding.checkBoxDiagonal.isChecked = true
            5 -> {
                settingsBinding.checkBoxDiagonal.isChecked = true
                settingsBinding.checkBoxHerizontal.isChecked = true
            }
            6 -> {
                settingsBinding.checkBoxDiagonal.isChecked = true
                settingsBinding.checkBoxVertical.isChecked = true
            }
            7 -> {
                settingsBinding.checkBoxHerizontal.isChecked = true
                settingsBinding.checkBoxVertical.isChecked = true
                settingsBinding.checkBoxDiagonal.isChecked = true
            }
        }

        settingsBinding.prewLvl.setOnClickListener {
            currentLevel--

            if(currentLevel == 0){
                settingsBinding.prewLvl.visibility = View.INVISIBLE
            } else if (currentLevel == 1) {
                settingsBinding.nextLvl.visibility = View.VISIBLE
            }

            updateLevel(currentLevel)
            settingsBinding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
        }

        settingsBinding.nextLvl.setOnClickListener {
            currentLevel++

            if(currentLevel == 2){
                settingsBinding.nextLvl.visibility = View.INVISIBLE
            } else if (currentLevel == 1) {
                settingsBinding.prewLvl.visibility = View.VISIBLE
            }

            updateLevel(currentLevel)
            settingsBinding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
        }

        settingsBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                currentVolumeSound = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                updateVolumeSound(currentVolumeSound)
            }

        })

        settingsBinding.checkBoxHerizontal.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules += 1
            } else {
                currentRules -= 1
            }

            updateRules(currentRules)
        }

        settingsBinding.checkBoxVertical.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules += 2
            } else {
                currentRules -= 2
            }

            updateRules(currentRules)
        }

        settingsBinding.checkBoxDiagonal.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules += 4
            } else {
                currentRules -= 4
            }

            updateRules(currentRules)
        }

        settingsBinding.toBack.setOnClickListener {
            setResult(RESULT_OK)
            onBackPressed()
        }

        setContentView(settingsBinding.root)
    }

    private fun updateVolumeSound(volume: Int){
        getSharedPreferences("game", MODE_PRIVATE).edit().apply{
            putInt(PREF_SOUND, volume)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun updateLevel(level: Int){
        getSharedPreferences("game", MODE_PRIVATE).edit().apply {
            putInt(PREF_LEVEL, level)
            apply()
        }
        setResult(RESULT_OK)
    }
    
    private fun updateRules(rules: Int){
        getSharedPreferences("game", MODE_PRIVATE).edit().apply {
            putInt(PREF_RULES, rules)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun getCurrentSettings(): SettingsInfo {
        this.getSharedPreferences("game", MODE_PRIVATE).apply {

            val sound = getInt(PREF_SOUND, 100)
            val level = getInt(PREF_LEVEL, 1)
            val rules = getInt(PREF_RULES, 7)

            return SettingsInfo(sound, level, rules)
        }
    }

    data class SettingsInfo(val sound: Int, val level: Int, val rules: Int)
}