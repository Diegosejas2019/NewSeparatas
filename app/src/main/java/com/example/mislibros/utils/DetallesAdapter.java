package com.example.mislibros.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mislibros.MainActivity;
import com.example.mislibros.R;
import com.example.mislibros.model.Detalle;
import com.github.barteksc.pdfviewer.PDFView;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import android.database.Cursor;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DetallesAdapter extends RecyclerView.Adapter<DetallesAdapter.ProductViewHolder> implements  android.view.View.OnClickListener {

    private Context mCtx;

    private List<Detalle> detalleList;

    private Boolean downloadPermited;
    public Detalle detalle;
    WebView webView;
    ProgressBar mProgressBar;
    PDFView pdfView;
    private Handler mHandler;
    public View vista;
    private DownloadManager dm;
    private long enqueue;
    public boolean validDownload = false;
    public boolean checkshare = false;
    public int visibilidad = 8;
    public Activity act;
    public DetallesAdapter(Context mCtx, List<Detalle> productList, WebView webView, PDFView pdfView, FragmentActivity activity) {
        this.mCtx = mCtx;
        this.detalleList = productList;
        this.webView = webView;
        this.pdfView = pdfView;
        this.act = activity;
    }

    @Override
    public DetallesAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        android.view.View view = inflater.inflate(R.layout.layout_detalle, null);


        ActivityCompat.requestPermissions(this.act , new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (validDownload){
                    validDownload = false;
                    Bundle extras = intent.getExtras();
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                    Cursor c = dm.query(q);

                    if (c.moveToFirst()) {
                        @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            // process download
                            @SuppressLint("Range") String TypeFile = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                            @SuppressLint("Range") String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                            // get other required data by changing the constant passed to getColumnIndex
                            String[] titulo = title.split("#");
                            String tituloFinal = titulo[1];
                            String filepath = titulo[2];
                            //Uri uri = Uri.parse(titulo[3]);
                            String namePath = titulo[3];
                            String downloadDirectory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                            File file = new File(downloadDirectory + "/" + namePath);
                            if (checkshare)
                            {
                                /*File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "abc.txt");
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/*");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath));
                                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) ;

                                List<ResolveInfo> resolvedInfoActivities =
                                        mCtx.getPackageManager().queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                for (ResolveInfo ri : resolvedInfoActivities) {

                                    mCtx.grantUriPermission(ri.activityInfo.packageName,Uri.parse(filepath), Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                }
                                mCtx.startActivity(Intent.createChooser(sharingIntent, "share file with"));*/
                                /*Intent i = new Intent(Intent.ACTION_SEND);
                                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

                                List<ResolveInfo> resInfoList = mCtx.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);

                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    mCtx.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }


                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"fake@fake.edu"});
                                i.putExtra(Intent.EXTRA_SUBJECT,"On The Job");
                                //Log.d("URI@!@#!#!@##!", Uri.fromFile(pic).toString() + "   " + pic.exists());
                                i.putExtra(Intent.EXTRA_TEXT,"All Detail of Email are here in message");
                                i.putExtra(Intent.EXTRA_STREAM,uri);
                                i.setType("application/pdf");
                                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                mCtx.startActivity(Intent.createChooser(i,"Share you on the jobing"));*/

                                Intent sharableIntent = new Intent();
                                sharableIntent.setAction(Intent.ACTION_SEND);
                                sharableIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION );

                                Uri uri = FileProvider.getUriForFile(mCtx, mCtx.getApplicationContext().getPackageName() + ".provider", file);
                                File imageFile = new File(String.valueOf(uri));
                                String Author = "";

                                List<ResolveInfo> resInfoList = mCtx.getPackageManager().queryIntentActivities(sharableIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    mCtx.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }
                                sharableIntent.setType("application/pdf");
                                sharableIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                sharableIntent.putExtra(Intent.EXTRA_TITLE, title);
                                sharableIntent.putExtra(Intent.EXTRA_TEXT, "a");



                                mCtx.startActivity(sharableIntent);

                                //checkshare = false;
                                /*Toast toast = Toast.makeText(mCtx, "Enviando correo...", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                mHandler = new Handler();
                                new Thread(new Runnable() {

                                    public void run() {

                                        try {

                                            SharedPreferences prefs = mCtx.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
                                            String email = prefs.getString("email", "");
                                            GMailSender sender = new GMailSender("erreparseparatas@errepar.com",
                                                    "#gh6b0pU541#hazP");
                                            sender.addAttachment(filepath, tituloFinal);
                                            sender.sendMail("App Libros Errepar - " + tituloFinal,
                                                    "Adjunto \n ",
                                                    "erreparseparatas@errepar.com",
                                                    email);
                                            // Toast.makeText(getApplicationContext(),"envio",Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            Toast.makeText(mCtx,"Hubo un problema al enviar el correo.",Toast.LENGTH_LONG).show();
                                        }
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run () {

                                                //Toast.makeText(mCtx,"Recibiras por email el archivo seleccionado.",Toast.LENGTH_LONG).show();
                                                Toast toast = Toast.makeText(mCtx, "Recibiras por email el archivo seleccionado.", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();

                                            }
                                        });


                                    }
                                }).start();*/
                            }
                        }
                    }
                }
            }
        };
        mCtx.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetallesAdapter.ProductViewHolder holder, final int position) {
        detalle = detalleList.get(position);
        String DownloadURL = "";
        String URL = "";
        String downloadDirectory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        String fileName = "";
        String titulo = "";
        if(detalle.getFileUrl() != null){
            if (detalle.getFileUrl().contains("new-")){
                DownloadURL = "https://old.errepar.com/resources/images/appseparatas/" + detalle.getFileUrl().replace("new-", "");
                URL = "http://docs.google.com/gview?embedded=true&url=https://old.errepar.com/resources/images/appseparatas/" + detalle.getFileUrl().replace("new-", "");
                if (detalle.getFileUrl().contains(".xls")){
                    fileName = (detalle.getId() + ".xls").replace(" ", "_");
                }
                else{
                    fileName = (detalle.getId() + ".pdf").replace(" ", "_");
                }
            }
            else{
                DownloadURL = "https://old.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl();
                URL = "http://docs.google.com/gview?embedded=true&url=https://old.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl();
                if(detalle.getFileUrl().contains("xls")){
                    fileName = (detalle.getId() + ".xls").replace(" ", "_");
                }
                else{
                    if (detalle.getFileUrl().contains("doc"))
                    {
                        fileName = (detalle.getId() + ".doc").replace(" ", "_");
                    }
                    else{
                        fileName = (detalle.getId() + ".pdf").replace(" ", "_");
                    }
                }
            }
        }
        titulo = detalle.getFileTitle();
        File file = new File(downloadDirectory + "/" + fileName);
//        String URL = "www.google.com"; // TEST URL
//        String URL = "http://docs.google.com/gview?embedded=true&url=https://www.errepar.com/resources/images/appseparatas/91.pdf"; // TEST URL 2


        if (detalle.getFecha() != null) {
            holder.txtFecha.setText(detalle.getFecha().substring(0, 10).replace("-", "/"));
        } else {
            holder.txtFecha.setText("Sin fecha");
        }
        SharedPreferences preferences = mCtx.getSharedPreferences("Configs", Context.MODE_PRIVATE);
        downloadPermited = preferences.getBoolean("downloadPermited", false);
        if (detalle.getFileTitle() == null) {
            holder.txtArchivo.setVisibility(View.GONE);
            holder.pdfIco.setVisibility(View.GONE);
        } else if (detalle.getFileUrl() != null) {
            holder.txtArchivo.setText(detalle.getFileTitle());
            String finalURL = URL;
            String finalDownloadURL = DownloadURL;
            String finalFileName = fileName;
            String finalTitulo = titulo;
            holder.txtArchivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    if (isConnected()) {
                        if (file.getName().contains("xls") || file.getName().contains("doc"))
                        {
                            mHandler = new Handler();
                            Toast toast = Toast.makeText(mCtx, "Enviando correo...", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            new Thread(new Runnable() {

                                public void run() {

                                    try {

                                        SharedPreferences prefs = mCtx.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
                                        String email = prefs.getString("email", "");
                                        GMailSender sender = new GMailSender("appmislibros@errepar.com",
                                                "#gh6b0pU541#hazP");
                                        /*sender.addAttachment(file.getPath(), finalTitulo);*/
                                        sender.sendMail("App Libros Errepar - " + finalTitulo,
                                                "Hola!\n" +
                                                        "\n" +
                                                        "\n" +
                                                        "Descarga el archivo " + finalTitulo +" haciendo clic a continuaci??n. \n" +
                                                        "\n" +
                                                        "\n" +
                                                        "<a href=" + finalDownloadURL.replace(" ","%20") +" target=\"_blank\"> Descargar </a> \n ",
                                                "appmislibros@errepar.com|||||||||||||||||||||||||||",
                                                email);
                                        // Toast.makeText(getApplicationContext(),"envio",Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(mCtx,"Hubo un problema al enviar el correo.",Toast.LENGTH_LONG).show();
                                    }
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run () {

                                            //Toast.makeText(mCtx,"Recibiras por email el archivo seleccionado.",Toast.LENGTH_LONG).show();
                                            Toast toast = Toast.makeText(mCtx, "Recibiras por email el archivo seleccionado.", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                        }
                                    });


                                }
                            }).start();
                        }
                        else{
                            final WebSettings webSettings = webView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setDomStorageEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(finalURL);
                            webView.setVisibility(View.VISIBLE);
                            if (downloadPermited && !file.exists()) {
                                dm = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(finalDownloadURL);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setTitle(finalFileName);
                                request.setDescription("Descargando libro - " + finalTitulo);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, finalFileName);
                                dm.enqueue(request);
                            }
                        }
                    } else {
                        if (file.exists()) {
                            if (!file.getName().contains("xls"))
                            {
                                pdfView.fromFile(file).load();
                                pdfView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(mCtx,"Archivo no descargado previamente.",Toast.LENGTH_LONG).show();
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
        if (visibilidad == 0)
        {
            holder.share.setVisibility(View.VISIBLE);
        }
        else{
            holder.share.setVisibility(View.GONE);
        }

        String finalDownloadURL = DownloadURL;
        String finalFileName = fileName;
        String finalTitulo = titulo;
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.share.isChecked()){
                    /*Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, finalTitulo1);
                    sendIntent.setType("text/plain");
                    mCtx.startActivity(sendIntent);*/
                    Toast toast = Toast.makeText(mCtx, "Descargando archivo..", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    if (downloadPermited) {
                        checkshare = true;
                        validDownload = true;
                        dm = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(finalDownloadURL);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(finalFileName);
                        request.setDescription("Descargando libro #" + finalTitulo + "#" + file.getPath() + "#" + finalFileName);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, finalFileName);
                        dm.enqueue(request);
                    }
                    else{
                        toast = Toast.makeText(mCtx, "Debe permitir la descarga de archivos.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            }
        });
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yyyy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */

        }
        return value;
    }


    @Override
    public int getItemCount() {
        return detalleList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtFecha, txtArchivo, txtLinkVimeo, txtLinkAudio;
        ImageView pdfIco, vidIco, audIco;
        CheckBox share;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFecha); //Fecha bind
            txtArchivo = itemView.findViewById(R.id.txtArchivo); //PDF text bind
            pdfIco = itemView.findViewById(R.id.favorite2); //PDF icon bind
            txtLinkVimeo = itemView.findViewById(R.id.txtLinkVimeo); //Video text bind
            vidIco = itemView.findViewById(R.id.favorite); //Video icon bind
            txtLinkAudio = itemView.findViewById(R.id.txtLinkAudio); //Audio text bind
            audIco = itemView.findViewById(R.id.favorite3); //Audio icon bind
            share = itemView.findViewById(R.id.check); //Audio icon bind
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

}
