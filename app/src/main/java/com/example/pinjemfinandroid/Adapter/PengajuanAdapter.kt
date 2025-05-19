package com.example.pinjemfinandroid.Adapter

import PengajuanResponseItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjemfinandroid.R
import java.text.NumberFormat
import java.util.Locale


class PengajuanAdapter(private var list: List<PengajuanResponseItem?>) :
    RecyclerView.Adapter<PengajuanAdapter.PengajuanViewHolder>() {

    fun updateData(newData: List<PengajuanResponseItem>) {
        this.list = newData
        notifyDataSetChanged()
    }

    class PengajuanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_pinjaman: TextView = itemView.findViewById(R.id.tv_pinjaman)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tv_tenor: TextView = itemView.findViewById(R.id.tv_tenor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengajuanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengajuan, parent, false)
        return PengajuanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PengajuanViewHolder, position: Int) {
        val item = list[position]
        val amount = item?.amount?.toString()?.toDoubleOrNull() ?: 0.0

        val amountFormatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(amount)

        if (item != null) {
            holder.tv_pinjaman.text = "Rp. ${item.amount}"
        }
        when(item?.status ?: "-"){
            "bckt_marketing"->{holder.tvStatus.text = "Status: in Review"}
            "bckt_BranchManager"->{holder.tvStatus.text = "Status: in Review"}
            "bckt_Operation"->{holder.tvStatus.text = "Status: Approve"}
            "Disbursment"->{holder.tvStatus.text = "Status: Disburse"}
            "tolak"->{holder.tvStatus.text = "Status: Rejected"}
        }

        holder.tv_tenor.text = "Jumlah: $amountFormatted"
    }

    override fun getItemCount(): Int = list.size
}