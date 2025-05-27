package com.example.pinjemfinandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter (private var notifications: List<NotificationEntity>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    fun updateData(newList: List<NotificationEntity>) {
        notifications = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.titleTextView.text = notification.title ?: "No Title"
        holder.bodyTextView.text = notification.body ?: "No Body"
        val formattedDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            .format(Date(notification.createdAt))
        holder.dateTextView.text = formattedDate
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvNotificationTitle)
        val bodyTextView: TextView = itemView.findViewById(R.id.tvNotificationBody)
        val dateTextView: TextView = itemView.findViewById(R.id.tvWaktu)
    }
}