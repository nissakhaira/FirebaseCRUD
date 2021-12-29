package com.example.firebasecrud

class ModelBarang {

    var key: String? = null
    var kode: String? = null
    var nama: String? = null
    var sn: String? = null
    var hb: String? = null
    var hj: String? = null
    var st: String? = null
    var sm: String? = null

    constructor() {}

    constructor(kodeBarang: String?, namaBarang: String?, satuan: String?, hargabeli: String?, hargajual: String?, stok: String?, stokmin: String?) {
        kode = kodeBarang
        nama = namaBarang
        sn = satuan
        hb = hargabeli
        hj = hargajual
        st = stok
        sm = stokmin
    }
}