package com.example.pinjemfinandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.Model.ListPlafonResponseItem
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.RupiahFormatter

class PlafonHomeAdapter(private val list: List<ListPlafonResponseItem>) :
    RecyclerView.Adapter<PlafonHomeAdapter.PlafonViewHolder>() {

    inner class PlafonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jenisPlafon: TextView = itemView.findViewById(R.id.tv_plafontype)
        val jumlahPlafon: TextView = itemView.findViewById(R.id.tv_plafonamount)
        val container: LinearLayout = itemView.findViewById(R.id.cardHome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlafonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plafonhome, parent, false)
        return PlafonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlafonViewHolder, position: Int) {
        val plafon = list[position]

        holder.jumlahPlafon.text = plafon.jumlah_plafon?.let { RupiahFormatter.format(it) }

        // Set background berdasarkan jumlah plafon
        val context = holder.itemView.context
        when (plafon.jenis_plafon?.lowercase()) {
            "bronze" -> holder.container.setBackgroundResource(R.drawable.card_bronze)
            "silver" -> holder.container.setBackgroundResource(R.drawable.card_silver)
            "gold" -> holder.container.setBackgroundResource(R.drawable.card_gold)
            "platinum" -> holder.container.setBackgroundResource(R.drawable.card_platinum)
            else -> {holder.container.setBackgroundResource(R.drawable.card_default)
                holder.jenisPlafon.text = plafon.jenis_plafon} // fallback
        }
    }

    override fun getItemCount(): Int = list.size


}