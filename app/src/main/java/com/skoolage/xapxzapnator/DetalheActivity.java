package com.skoolage.xapxzapnator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skoolage.xapxzapnator.models.Conversante;

import java.util.UUID;

public class DetalheActivity extends AppCompatActivity {

    private Button btnConfirmar, btnAtualizar, btnExcluir;
    private EditText edtNome, edtCelular, edtEmail;

    private DatabaseReference db;

    // guardar id, nulo se for insert
    String idAtual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_detalhe);

        edtNome = findViewById(R.id.edtNome);
        edtCelular = findViewById(R.id.edtCelular);
        edtEmail = findViewById(R.id.edtEmail);

        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnExcluir = findViewById(R.id.btnExcluir);

        db = FirebaseDatabase.getInstance().getReference();

        //verificar se foi recebido um id via intent

        Bundle dados = getIntent().getExtras();
        if (dados != null) {
            idAtual = dados.getString("id");
            edtNome.setText(dados.getString("nome"));
            edtCelular.setText(dados.getString("celular"));
            edtEmail.setText(dados.getString("email"));
            btnConfirmar.setVisibility(View.GONE);
            btnAtualizar.setVisibility(View.VISIBLE);
            btnExcluir.setVisibility(View.VISIBLE);
        } else {
            boolean temId = idAtual != null;
            btnAtualizar.setVisibility(temId ?  View.GONE: View.VISIBLE);
            btnExcluir.setVisibility(temId ?  View.GONE: View.VISIBLE);
            btnConfirmar.setVisibility(temId ?  View.VISIBLE: View.GONE);
        }

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conversante c = new Conversante();

                c.setId(idAtual);
                c.setNome(edtNome.getText().toString());
                c.setCelular(edtCelular.getText().toString());
                c.setEmail(edtEmail.getText().toString());

                db.child("conversantes").child(idAtual).setValue(c)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(
                                    DetalheActivity.this,
                                    "Conversante atualizado com sucesso!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    DetalheActivity.this,
                                    "Erro: " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        });
            }
        });


        btnExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.child("conversantes").child(idAtual).removeValue()
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(
                                        DetalheActivity.this,
                                        "Conversante excluído com sucesso!",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(
                                        DetalheActivity.this,
                                        "Erro: " + e.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                }
            });

        btnConfirmar.setOnClickListener(view -> {
                try {
                    String nome = edtNome.getText().toString().trim();
                    String celular = edtCelular.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();

                    if (nome.isEmpty() || celular.isEmpty() || email.isEmpty()) {
                        Snackbar.make(view, "Preencha todos os campos!", 3000).show();
                    } else {
                        Conversante c = new Conversante();
                        c.setId(UUID.randomUUID().toString());
                        c.setNome(nome);
                        c.setCelular(celular);
                        c.setEmail(email);

                        db.child("conversantes").child(c.getId()).setValue(c);
                        Snackbar.make(view, "Conversante cadastrado com sucesso!", 3000).show();
                        finish();
                    }

                } catch (Exception e) {
                    Snackbar.make(view, "Erro ao cadastrar o conversante!", 3000).show();
                }
            });
        }
    }
