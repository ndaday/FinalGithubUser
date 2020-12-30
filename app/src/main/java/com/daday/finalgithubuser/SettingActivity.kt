package com.daday.finalgithubuser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.daday.finalgithubuser.databinding.ActivitySettingBinding
import com.daday.finalgithubuser.utils.AlarmReceiver

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFS_NAME = "SettingPref"
        private const val REMINDER = "reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = getString(R.string.settings)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSwitch()
        binding.swReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setDailyReminder(
                    this,
                    AlarmReceiver.TYPE_DAILY,
                    getString(R.string.reminder)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            saveChange(isChecked)
        }

        binding.btnLocalization.setOnClickListener(this)
    }

    private fun setSwitch() {
        binding.swReminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_localization -> {
                val localIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(localIntent)
            }
        }
    }

    private fun saveChange(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(REMINDER, value)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}