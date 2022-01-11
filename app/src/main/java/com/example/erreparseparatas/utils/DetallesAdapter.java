package com.example.erreparseparatas.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.model.Detalle;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.views.DetalleFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DetallesAdapter extends RecyclerView.Adapter<DetallesAdapter.ProductViewHolder> implements  android.view.View.OnClickListener {

    private Context mCtx;

    private List<Detalle> detalleList;

    private Integer mIdUser;

    private String View;

    //the recyclerview
    private ProgressDialog pDialog;

    //getting the context and product list with constructor
    public DetallesAdapter(Context mCtx, List<Detalle> productList,Integer iduser) {
        this.mCtx = mCtx;
        this.detalleList = productList;
        this.mIdUser = iduser;

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
        holder.txtArchivo.setText(detalle.getFileTitle());
        holder.txtArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl()));
                mCtx.startActivity(browserIntent);
            }
        });
        holder.txtLinkVimeo.setText(detalle.getVideoTitle());
        holder.txtLinkVimeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detalle.getVideoUrl()));
                mCtx.startActivity(browserIntent);
            }
        });
        holder.txtLinkAudio.setText(detalle.getAudioTitle());
        holder.txtLinkAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detalle.getAudioUrl()));
                mCtx.startActivity(browserIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return detalleList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtArchivo, txtLinkVimeo, txtLinkAudio, txtPdf;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtArchivo = itemView.findViewById(R.id.txtArchivo);
            txtLinkVimeo = itemView.findViewById(R.id.txtLinkVimeo);
            txtLinkAudio = itemView.findViewById(R.id.txtLinkAudio);
            //txtPdf = itemView.findViewById(R.id.txtPdf);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id) {
            case R.id.imageView:

                break;
        }
    }

}
