package com.example.imobiliaria;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    static Context context;
    ArrayList<User> list;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.vencimento.setText(user.getVencimento());
        holder.pago.setText(user.getPago());
        holder.email.setText(user.getEmail());
        holder.endereco.setText(user.getEndereco());
        holder.cep.setText(user.getCep());
        holder.comple.setText(user.getComple());
        holder.numero.setText(user.getNumero());
        holder.data.setText(user.getData());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView data;
        TextView comple;
        TextView numero;
        TextView cep;
        TextView email;
        TextView vencimento;
        TextView endereco;
        TextView pago;
        TextView age;
        Button btn_botao;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            vencimento = itemView.findViewById(R.id.textvencimento);
            pago = itemView.findViewById(R.id.textpago);
            endereco = itemView.findViewById(R.id.textendereco);

            btn_botao = itemView.findViewById(R.id.btn_botao);

            email = itemView.findViewById(R.id.text_email);
            cep = itemView.findViewById(R.id.text_cep);
            comple = itemView.findViewById(R.id.text_comple);
            data = itemView.findViewById(R.id.text_data);
            numero = itemView.findViewById(R.id.text_numero);

            btn_botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arquivo, arquivo_ext, local, cepx, numerox, complex,datax;
                    arquivo = null;
                    local = null;
                    local = "x" + email.getText().toString();
                    arquivo_ext = ".pdf";
                    cepx = cep.getText().toString();
                    numerox = numero.getText().toString();
                    complex = comple.getText().toString();
                    datax = data.getText().toString();
                    arquivo = cepx + numerox + complex + datax + arquivo_ext;
                    StorageReference storageReference = FirebaseStorage.getInstance( ).getReference(local);
                    StorageReference ref=storageReference.child(arquivo);

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            downloadFile(this, datax,"pdf",DIRECTORY_DOWNLOADS,url);

                        }

                    });



                }
            });


        }
    }



    private static void downloadFile(OnSuccessListener<Uri> uriOnSuccessListener, String doc, String fileExtension, String directoryDownloads, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadmanager.enqueue(request);

    }


}
