package com.example.firebasecrud

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.firebasecrud.R
import com.example.firebasecrud.MainViewHolder
import com.example.firebasecrud.ModelBarang
import java.util.*

class MainAdapter(private val context: Context,
                  private val daftarBarang: ArrayList<ModelBarang?>?) : RecyclerView.Adapter<MainViewHolder>() {
    private val listener: FirebaseDataListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val generator = ColorGenerator.MATERIAL //custom color
        val color = generator.randomColor
        holder.view.setCardBackgroundColor(color)
        holder.kodebarang.text = "Kode : " + (daftarBarang?.get(position)?.kode)
        holder.namabarang.text = "Nama   : " + (daftarBarang?.get(position)?.nama)
        holder.satuan.text = "Satuan     : " + (daftarBarang?.get(position)?.sn)
        holder.hargabeli.text = "Harga Beli   : " + daftarBarang?.get(position)?.hb
        holder.hargajual.text = "Harga Jual   : " + daftarBarang?.get(position)?.hj
        holder.stok.text = "Harga   : " + daftarBarang?.get(position)?.st
        holder.stokmin.text = "Harga   : " + daftarBarang?.get(position)?.sm
        holder.view.setOnClickListener { listener.onDataClick(daftarBarang?.get(position), position) }
    }

    override fun getItemCount(): Int {

        return daftarBarang?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
        fun onDataClick(barang: ModelBarang?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener
    }
}