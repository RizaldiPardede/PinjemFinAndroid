package com.example.pinjemfinandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.Model.ListPlafonResponseItem
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.RupiahFormatter

class PlafonAdapter(private val plafonList:  List<ListPlafonResponseItem>) :
    RecyclerView.Adapter<PlafonAdapter.PlafonViewHolder>() {

    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlafonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plafon, parent, false)
        return PlafonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlafonViewHolder, position: Int) {
        val plafon = plafonList[position]
        val isExpanded = position == expandedPosition

        // Set data awal
        holder.tvJenisPlafon.text = plafon.jenis_plafon
        holder.tvJumlahPlafon.text = "Jumlah: "+ plafon.jumlah_plafon?.let {
            RupiahFormatter.format(
                it
            )
        }
        holder.tvBunga.text = "Bunga: ${plafon.bunga}%"
        holder.tvDeskripsi.text = plafon.jenis_plafon?.let { getDeskripsi(it) }

        // Reset skala awal
        val initialScale = if (isExpanded) 1.1f else 1f
        holder.cardView.scaleX = initialScale
        holder.cardView.scaleY = initialScale
        holder.imageIcon.scaleX = initialScale
        holder.imageIcon.scaleY = initialScale

        // Expand/collapse
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        // Gambar berdasarkan jenis plafon
        val imageRes = when (plafon.jenis_plafon?.lowercase()) {
            "bronze" -> R.drawable.card_bronze
            "silver" -> R.drawable.card_silver
            "gold" -> R.drawable.card_gold
            "platinum" -> R.drawable.card_platinum
            else -> R.drawable.card_default
        }
        holder.imageIcon.setImageResource(imageRes)

        // Handle klik
        holder.itemView.setOnClickListener {
            val prev = expandedPosition
            expandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(prev)
            notifyItemChanged(position)
        }

        // Animasi pembesaran
        val scale = if (isExpanded) 1.1f else 1f
        holder.cardView.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(200)
            .start()

        holder.imageIcon.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(200)
            .start()
    }

    override fun getItemCount(): Int = plafonList.size

    private fun getDeskripsi(jenis: String): String = when (jenis.lowercase()) {
        "bronze" -> "Plafon entry-level untuk kebutuhan dasar."
        "silver" -> "Pilihan menengah dengan bunga terjangkau."
        "gold" -> "Plafon besar untuk pengguna terpercaya."
        "platinum" -> "Plafon eksklusif dengan bunga paling rendah."
        else -> "Jenis plafon untuk berbagai kebutuhan."
    }

    inner class PlafonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val tvJenisPlafon: TextView = itemView.findViewById(R.id.tvJenisPlafon)
        val tvJumlahPlafon: TextView = itemView.findViewById(R.id.tvJumlahPlafon)
        val expandedLayout: LinearLayout = itemView.findViewById(R.id.expandedLayout)
        val tvBunga: TextView = itemView.findViewById(R.id.tvBunga)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        val imageIcon: ImageView = itemView.findViewById(R.id.imageIcon)
    }
}