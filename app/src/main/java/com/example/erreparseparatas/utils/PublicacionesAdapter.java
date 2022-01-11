package com.example.erreparseparatas.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erreparseparatas.R;
import com.example.erreparseparatas.model.Publicaciones;
import com.example.erreparseparatas.views.DetalleFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class PublicacionesAdapter extends RecyclerView.Adapter<PublicacionesAdapter.ProductViewHolder> implements  android.view.View.OnClickListener {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Publicaciones> productList;
    private ImageView favo;
    private Integer mIdUser;
    private String idPost;
    private Double Latitude;
    private Double Longuitude;
    private String View;
    private static final String TAG_SUCCESS = "StatusCode";
    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    private ProgressDialog pDialog;

    //getting the context and product list with constructor
    public PublicacionesAdapter(Context mCtx, List<Publicaciones> productList,Integer iduser) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.mIdUser = iduser;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_publicaciones, null);

        CardView card_view =  (CardView) view.findViewById(R.id.card_view);
        ImageView imageView =  (ImageView) view.findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        TextView textViewTitle =  (TextView) view.findViewById(R.id.textViewTitle);
        //TextView textViewShortDesc =  (TextView) view.findViewById(R.id.textViewShortDesc);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the product of the specified position
        Publicaciones product = productList.get(position);
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Fragment fragment = new DetalleFragment();
                Bundle args = new Bundle();
                args.putString("linkImg", product.getImageUrl());
                args.putString("publicacionid", String.valueOf(product.getId()));

                fragment.setArguments(args);
                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());



        Url url = new Url();


        Picasso.with(mCtx)
                //.load(url.getDireccion() + "/Imagenes/" + product.getImageUrl().substring((product.getImageUrl().length()-6)).replaceAll("\\\\", ""))
                .load(product.getImageUrl())
                .resize(850, 1000)
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewPdf, textViewPdf2, textViewVimeo, textViewAudio;
        ImageView imageView;
        ImageView imgFavorite;
        CardView cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            //textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v)
    {

        //int position = (int) v.getTag();
        int id = v.getId();

        switch (id) {
            case R.id.imageView:

         /*       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);

                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View dialogView = inflater.inflate(R.layout.service_detail, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setNegativeButton("Cerrar",null );

                ImageView imagen = v.findViewById(R.id.imageView);
                //editText.setImageResource(R.drawable.comidas);
                editText.setImageDrawable(imagen.getDrawable());
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();*/
                break;
        }
    }

}
