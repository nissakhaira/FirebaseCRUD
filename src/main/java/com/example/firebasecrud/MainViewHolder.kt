package com.example.firebasecrud

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecrud.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @JvmField
    var kodebarang: TextView

    @JvmField
    var namabarang: TextView

    @JvmField
    var satuan: TextView

    @JvmField
    var hargabeli: TextView

    @JvmField
    var hargajual: TextView

    @JvmField
    var stok: TextView

    @JvmField
    var stokmin: TextView

    @JvmField
    var view: CardView

    init {
        kodebarang = itemView.findViewById(R.id.kodebarang)
        namabarang = itemView.findViewById(R.id.namabarang)
        satuan = itemView.findViewById(R.id.satuan)
        hargabeli = itemView.findViewById(R.id.hargabeli)
        hargajual = itemView.findViewById(R.id.hargajual)
        stok = itemView.findViewById(R.id.stok)
        stokmin = itemView.findViewById(R.id.stok_min)
        view = itemView.findViewById(R.id.cvMain)
    }
}