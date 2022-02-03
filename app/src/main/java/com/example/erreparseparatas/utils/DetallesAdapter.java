package com.example.erreparseparatas.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.example.erreparseparatas.MainActivity.MY_PREFS_NAME;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.model.Detalle;

import java.io.File;
import java.util.List;

public class DetallesAdapter extends RecyclerView.Adapter<DetallesAdapter.ProductViewHolder> implements  android.view.View.OnClickListener {

    private Context mCtx;

    private List<Detalle> detalleList;

    private Boolean downloadPermited;

    public DetallesAdapter(Context mCtx, List<Detalle> productList, Integer iduser) {
        this.mCtx = mCtx;
        this.detalleList = productList;
    }

    @Override
    public DetallesAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        android.view.View view = inflater.inflate(R.layout.layout_detalle, null);

        return new DetallesAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetallesAdapter.ProductViewHolder holder, final int position) {
        Detalle detalle = detalleList.get(position);
        if (detalle.getFecha() != null) {
            holder.txtFecha.setText(detalle.getFecha());
        } else {
            holder.txtFecha.setText("Sin fecha");
        }
        SharedPreferences preferences = mCtx.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        downloadPermited = preferences.getBoolean("downloadPermited", false);
        if (detalle.getFileTitle() == null) {
            holder.txtArchivo.setVisibility(View.GONE);
            holder.pdfIco.setVisibility(View.GONE);
        } else {
            holder.txtArchivo.setText(detalle.getFileTitle());
            holder.txtArchivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    String downloadDirectory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    String fileName = (detalle.getFileTitle() + detalle.getId() + ".pdf").replace(" ", "_");
                    File file = new File(downloadDirectory + "/" + fileName);
                    if (isConnected()) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl()));
                        mCtx.startActivity(browserIntent);
                        if (downloadPermited && !file.exists()) {
                            DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse("https://www.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setTitle(fileName);
                            request.setDescription("Descargando libro...");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                            downloadmanager.enqueue(request);
                        }
                    } else {
                        if (file.exists()) {
                            openFile(file);
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl()));
                            mCtx.startActivity(browserIntent);
                        }
                    }
                }
            });
        }
        if (detalle.getVideoTitle() == null || detalle.getVideoTitle().equals("")) {
            holder.txtLinkVimeo.setVisibility(View.GONE);
            holder.vidIco.setVisibility(View.GONE);
        } else  if (detalle.getVideoUrl() != null){
            holder.txtLinkVimeo.setText(detalle.getVideoTitle());
            holder.txtLinkVimeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detalle.getVideoUrl()));
                    mCtx.startActivity(browserIntent);
                }
            });
        }
        if (detalle.getAudioTitle() == null) {
            holder.txtLinkAudio.setVisibility(View.GONE);
            holder.audIco.setVisibility(View.GONE);
        } else  if (detalle.getAudioUrl() != null) {
            holder.txtLinkAudio.setText(detalle.getAudioTitle());
            holder.txtLinkAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detalle.getAudioUrl()));
                    mCtx.startActivity(browserIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return detalleList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtFecha, txtArchivo, txtLinkVimeo, txtLinkAudio;
        ImageView pdfIco, vidIco, audIco;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtArchivo = itemView.findViewById(R.id.txtArchivo);
            txtLinkVimeo = itemView.findViewById(R.id.txtLinkVimeo);
            txtLinkAudio = itemView.findViewById(R.id.txtLinkAudio);
            pdfIco = itemView.findViewById(R.id.favorite2);
            vidIco = itemView.findViewById(R.id.favorite);
            audIco = itemView.findViewById(R.id.favorite3);
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mCtx.getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imageView:
                break;
        }
    }
    private void openFile(File file) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, FileProvider.getUriForFile(mCtx, mCtx.getApplicationContext().getPackageName() + ".provider", file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mCtx.startActivity(intent);
    }
}
