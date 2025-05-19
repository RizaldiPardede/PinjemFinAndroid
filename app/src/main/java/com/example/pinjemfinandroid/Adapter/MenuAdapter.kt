package com.example.pinjemfinandroid.Adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.Model.MenuModel
import com.example.pinjemfinandroid.R

class MenuAdapter(private val dataList: List<MenuModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>()  {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_judulmenu)
        val tvDescription: TextView = view.findViewById(R.id.tv_deskripsimenu)
        val imgItem: ImageView = view.findViewById(R.id.iv_menu)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description
        holder.imgItem.setImageResource(item.imageResId)
        val color = ContextCompat.getColor(holder.itemView.context, R.color.black)
        holder.imgItem.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }


    override fun getItemCount() = dataList.size
}