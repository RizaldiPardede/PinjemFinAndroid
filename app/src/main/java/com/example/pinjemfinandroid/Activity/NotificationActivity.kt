package com.example.pinjemfinandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinjemfinandroid.Adapter.NotificationAdapter
import com.example.pinjemfinandroid.Local.NotificationViewModel
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotificationAdapter(emptyList())
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotifications.adapter = adapter
        binding.ivBack.setOnClickListener{
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        observeNotifications()
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            notificationViewModel.markAllAsRead()
            notificationViewModel.notifications.collect { notifications ->
                adapter.updateData(notifications)
            }
        }
    }
}