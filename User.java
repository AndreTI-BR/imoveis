package com.example.imobiliaria;

import android.widget.Button;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String cep;
    private String numero;
    private String data;
    private String comple;
    private String pago;
    private String vencimento;
    private String endereco;
    private Button btn_botao;
    private String email;

    public User() {
    }

    public User(String numero,String comple,String data,String cep,String age, String pago, String vencimento, String endereco, Button btn_botao, String email) {
        this.comple = comple;
        this.cep = cep;
        this.numero = numero;
        this.data = data;
        this.pago = pago;
        this.vencimento = vencimento;
        this.endereco = endereco;
    //    this.btn_botao = btn_botao;
        this.email = email;
    }
    public String getComple() {
        return comple;

    }public String getData() {
        return data;
    }
    public String getCep() {
        return cep;
    }
    public String getNumero() {
        return numero;
    }

    public String getPago() {
        return pago;
   }

    public String getVencimento() {
       return vencimento;
   }

   public String getEndereco() {
       return endereco;
 }


    public String getEmail() {
        return email;
    }
}
