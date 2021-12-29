package com.example.firebasecrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasecrud.R;
import com.example.firebasecrud.MainAdapter;
import com.example.firebasecrud.ModelBarang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.FirebaseDataListener {

    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditKode;
    private EditText mEditNama;
    private EditText mEditSatuan;
    private EditText mEditHb;
    private EditText mEditHj;
    private EditText mEditStok;
    private EditText mEditStokmin;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<ModelBarang> daftarBarang;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("barang");
        mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarBarang = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelBarang barang = mDataSnapshot.getValue(ModelBarang.class);
                    barang.setKey(mDataSnapshot.getKey());
                    daftarBarang.add(barang);
                }

                mAdapter = new MainAdapter(MainActivity.this, daftarBarang);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(MainActivity.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        mFloatingActionButton = findViewById(R.id.tambah_barang);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTambahBarang();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDataClick(final ModelBarang barang, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBarang(barang);
            }
        });

        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataBarang(barang);
            }
        });

        builder.setNeutralButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogTambahBarang() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data ModelBarang");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);

        mEditKode = view.findViewById(R.id.kodebarang);
        mEditNama = view.findViewById(R.id.namabarang);
        mEditSatuan = view.findViewById(R.id.satuan);
        mEditHb = view.findViewById(R.id.hargabeli);
        mEditHj = view.findViewById(R.id.hargajual);
        mEditStok = view.findViewById(R.id.stok);
        mEditStokmin = view.findViewById(R.id.stok_min);
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String kodebarang = mEditKode.getText().toString();
                String namabarang = mEditNama.getText().toString();
                String satuan = mEditSatuan.getText().toString();
                String hargabeli = mEditHb.getText().toString();
                String hargajual = mEditHj.getText().toString();
                String stok = mEditStok.getText().toString();
                String stokmin = mEditStokmin.getText().toString();

                if (!kodebarang.isEmpty() && !namabarang.isEmpty() && !satuan.isEmpty() && !hargabeli.isEmpty() && !hargajual.isEmpty() && !stok.isEmpty() && !stokmin.isEmpty()) {
                    submitDataBarang(new ModelBarang(kodebarang, namabarang, satuan, hargabeli, hargajual, stok, stokmin));
                } else {
                    Toast.makeText(MainActivity.this, "Data harus di isi!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateBarang(final ModelBarang barang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data ModelBarang");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);

        mEditKode = view.findViewById(R.id.kodebarang);
        mEditNama = view.findViewById(R.id.namabarang);
        mEditSatuan = view.findViewById(R.id.satuan);
        mEditHb = view.findViewById(R.id.hargabeli);
        mEditHj = view.findViewById(R.id.hargajual);
        mEditStok = view.findViewById(R.id.stok);
        mEditStokmin = view.findViewById(R.id.stok_min);

        mEditKode.setText(barang.getKode());
        mEditNama.setText(barang.getNama());
        mEditSatuan.setText(barang.getSn());
        mEditHb.setText(barang.getHb());
        mEditHj.setText(barang.getHj());
        mEditSatuan.setText(barang.getSt());
        mEditStokmin.setText(barang.getSm());
        builder.setView(view);

        if (barang != null) {
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    barang.setKode(mEditKode.getText().toString());
                    barang.setNama(mEditNama.getText().toString());
                    barang.setSn(mEditSatuan.getText().toString());
                    barang.setHb(mEditHb.getText().toString());
                    barang.setHj(mEditHj.getText().toString());
                    barang.setSt(mEditStok.getText().toString());
                    barang.setSm(mEditStokmin.getText().toString());
                    updateDataBarang(barang);
                }
            });
        }

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }

    private void submitDataBarang(ModelBarang barang) {
        mDatabaseReference.child("data_barang").push()
                .setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Data barang berhasil di simpan !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDataBarang(ModelBarang barang) {
        mDatabaseReference.child("data_barang").child(barang.getKey())
                .setValue(barang).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hapusDataBarang(ModelBarang barang) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("data_barang").child(barang.getKey())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                    Toast.makeText(MainActivity.this, "Data berhasil di hapus !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}