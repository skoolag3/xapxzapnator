package com.skoolage.xapxzapnator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skoolage.xapxzapnator.adapters.ConversanteAdapter;
import com.skoolage.xapxzapnator.models.Conversante;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lsvConversantes;
    private List<Conversante> lista = new ArrayList<>();
    private ConversanteAdapter adapter;
    private FloatingActionButton btnInserir;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lsvConversantes = findViewById(R.id.lsvConversantes);
        btnInserir = findViewById(R.id.btnInserir);

        db = FirebaseDatabase.getInstance().getReference();

        adapter = new ConversanteAdapter(this, lista);
        lsvConversantes.setAdapter(adapter);

        db.child("conversantes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Conversante c = ds.getValue(Conversante.class);

                    if (c != null) {
                        lista.add(c);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        MainActivity.this,
                        "Erro: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        lsvConversantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversante c = lista.get(i);

                Intent detalhe = new Intent(MainActivity.this, DetalheActivity.class);
                detalhe.putExtra("id", c.getId());
                detalhe.putExtra("nome", c.getNome());
                detalhe.putExtra("celular", c.getCelular());
                detalhe.putExtra("email", c.getEmail());

                startActivity(detalhe);
            }
        });

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detalhe = new Intent(MainActivity.this, DetalheActivity.class);
                startActivity(detalhe);
            }
        });
    }
}