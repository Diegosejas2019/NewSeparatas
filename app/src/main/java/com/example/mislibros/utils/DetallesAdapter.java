package com.example.mislibros.utils;

import static android.content.ContentValues.TAG;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
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
import com.example.mislibros.model.LogEvento;
import com.example.mislibros.presenter.MainPresenter;
import com.github.barteksc.pdfviewer.PDFView;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import android.database.Cursor;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mislibros.MainActivity.MY_PREFS_NAME;

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
    public Integer midUser = 0;
    public Context context;
    public MainPresenter mPresenter;
    public DetallesAdapter(Context mCtx, List<Detalle> productList, WebView webView, PDFView pdfView, FragmentActivity activity, MainPresenter mPresenter) {
        this.mCtx = mCtx;
        this.detalleList = productList;
        this.webView = webView;
        this.pdfView = pdfView;
        this.act = activity;
        this.mPresenter = mPresenter;
    }

    @Override
    public DetallesAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        android.view.View view = inflater.inflate(R.layout.layout_detalle, null);
        context = inflater.getContext();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer UserId = prefs.getInt("iduser", 0);
        if (UserId != 0) {

            midUser = UserId;
        }

        ActivityCompat.requestPermissions(this.act , new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                if (validDownload){
//                    validDownload = false;
//                    Bundle extras = intent.getExtras();
//                    DownloadManager.Query q = new DownloadManager.Query();
//                    q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
//                    Cursor c = dm.query(q);
//
//                    if (c.moveToFirst()) {
//                        @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                            // process download
//                            @SuppressLint("Range") String TypeFile = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
//                            @SuppressLint("Range") String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
//                            // get other required data by changing the constant passed to getColumnIndex
//                            String[] titulo = title.split("#");
//                            String tituloFinal = titulo[1];
//                            String filepath = titulo[2];
//                            //Uri uri = Uri.parse(titulo[3]);
//                            String namePath = titulo[3];
//                            String downloadDirectory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//                            File file = new File(downloadDirectory + "/" + namePath);
//
//                            if (checkshare)
//                            {
//                                /*File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "abc.txt");
//                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                                sharingIntent.setType("text/*");
//                                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath));
//                                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) ;
//
//                                List<ResolveInfo> resolvedInfoActivities =
//                                        mCtx.getPackageManager().queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY);
//
//                                for (ResolveInfo ri : resolvedInfoActivities) {
//
//                                    mCtx.grantUriPermission(ri.activityInfo.packageName,Uri.parse(filepath), Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                                }
//                                mCtx.startActivity(Intent.createChooser(sharingIntent, "share file with"));*/
//                                /*Intent i = new Intent(Intent.ACTION_SEND);
//                                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
//
//                                List<ResolveInfo> resInfoList = mCtx.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
//
//                                for (ResolveInfo resolveInfo : resInfoList) {
//                                    String packageName = resolveInfo.activityInfo.packageName;
//                                    mCtx.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                }
//
//
//                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"fake@fake.edu"});
//                                i.putExtra(Intent.EXTRA_SUBJECT,"On The Job");
//                                //Log.d("URI@!@#!#!@##!", Uri.fromFile(pic).toString() + "   " + pic.exists());
//                                i.putExtra(Intent.EXTRA_TEXT,"All Detail of Email are here in message");
//                                i.putExtra(Intent.EXTRA_STREAM,uri);
//                                i.setType("application/pdf");
//                                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                mCtx.startActivity(Intent.createChooser(i,"Share you on the jobing"));*/
//
//                                try {
//                                    Intent sharableIntent = new Intent();
//                                    sharableIntent.setAction(Intent.ACTION_SEND);
//                                    sharableIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION );
//
//                                    Uri uri = FileProvider.getUriForFile(mCtx, mCtx.getApplicationContext().getPackageName() + ".provider", file);
//
//                                    File imageFile = new File(String.valueOf(uri));
//                                    String Author = "";
//
//                                    List<ResolveInfo> resInfoList = mCtx.getPackageManager().queryIntentActivities(sharableIntent, PackageManager.MATCH_DEFAULT_ONLY);
//                                    for (ResolveInfo resolveInfo : resInfoList) {
//                                        String packageName = resolveInfo.activityInfo.packageName;
//                                        mCtx.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    }
//                                    sharableIntent.setType("*/*");
//                                    sharableIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                                    sharableIntent.putExtra(Intent.EXTRA_TITLE, title);
//
//
//
//                                    mCtx.startActivity(sharableIntent);
//                                }
//                                catch (Exception e){
//                                    Toast.makeText(mCtx,e.getMessage(),Toast.LENGTH_LONG).show();
//                                }
//
//
//                                //checkshare = false;
//                                /*Toast toast = Toast.makeText(mCtx, "Enviando correo...", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                mHandler = new Handler();
//                                new Thread(new Runnable() {
//
//                                    public void run() {
//
//                                        try {
//
//                                            SharedPreferences prefs = mCtx.getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
//                                            String email = prefs.getString("email", "");
//                                            GMailSender sender = new GMailSender("erreparseparatas@errepar.com",
//                                                    "#gh6b0pU541#hazP");
//                                            sender.addAttachment(filepath, tituloFinal);
//                                            sender.sendMail("App Libros Errepar - " + tituloFinal,
//                                                    "Adjunto \n ",
//                                                    "erreparseparatas@errepar.com",
//                                                    email);
//                                            // Toast.makeText(getApplicationContext(),"envio",Toast.LENGTH_LONG).show();
//                                        } catch (Exception e) {
//                                            Toast.makeText(mCtx,"Hubo un problema al enviar el correo.",Toast.LENGTH_LONG).show();
//                                        }
//                                        mHandler.post(new Runnable() {
//                                            @Override
//                                            public void run () {
//
//                                                //Toast.makeText(mCtx,"Recibiras por email el archivo seleccionado.",Toast.LENGTH_LONG).show();
//                                                Toast toast = Toast.makeText(mCtx, "Recibiras por email el archivo seleccionado.", Toast.LENGTH_LONG);
//                                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                                toast.show();
//
//                                            }
//                                        });
//
//
//                                    }
//                                }).start();*/
//                            }
//                        }
//                    }
//                }
//            }
//        };
//        mCtx.registerReceiver(receiver, new IntentFilter(
//                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        detalle = detalleList.get(position);
        String DownloadURL = "";
        String URL = "";
        String downloadDirectory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        String fileName = "";
        String titulo = "";
        if(detalle.getFileUrl() != null){
            holder.txtArchivo.setVisibility(View.VISIBLE);
            if (detalle.getFileUrl().contains("new-")){
                DownloadURL = "https://portalerrepar.errepar.com/resources/images/appseparatas/" + detalle.getFileUrl().replace("new-", "");
                URL = "http://docs.google.com/gview?embedded=true&url=https://portalerrepar.errepar.com/resources/images/appseparatas/" + detalle.getFileUrl().replace("new-", "");
                if (detalle.getFileUrl().contains(".xls")){
                    fileName = (detalle.getId() + ".xls").replace(" ", "_");
                }
                else{
                    fileName = (detalle.getId() + ".pdf").replace(" ", "_");
                }
            }
            else{
                DownloadURL = "https://portalerrepar.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl();
                URL = "http://docs.google.com/gview?embedded=true&url=https://portalerrepar.errepar.com/resources/images/appseparatas/" + detalle.getId() + detalle.getFileUrl();
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
        SharedPreferences preferences = mCtx.getSharedPreferences("Configs", MODE_PRIVATE);
        downloadPermited = preferences.getBoolean("downloadPermited", false);
        if (detalle.getFileUrl() == null && detalle.getPublicacionUrl() == null) {
            holder.txtArchivo.setVisibility(View.GONE);
            holder.pdfIco.setVisibility(View.GONE);
        }
        else{
            if (detalle.getFileUrl() != null || detalle.getPublicacionTitle() != null) {
                holder.txtArchivo.setText(detalle.getFileTitle());
                if (detalle.getPublicacionTitle() != null){
                    holder.txtArchivo.setText(detalle.getPublicacionTitle());
                    holder.txtArchivo.setVisibility(View.VISIBLE);
                    holder.pdfIco.setVisibility(View.GONE);
                }
                String finalURL = URL;
                String finalDownloadURL = DownloadURL;
                String finalFileName = fileName;
                String finalTitulo = titulo;
                if (detalle.getPublicacionUrl() != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        holder.txtArchivo.setTooltipText(detalle.getPublicacionUrl());
                    }
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        holder.txtArchivo.setTooltipText(null);
                    }
                    holder.pdfIco.setVisibility(View.VISIBLE);
                }
                holder.txtArchivo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                            "Descarga el archivo " + finalTitulo +" haciendo clic a continuación. \n" +
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
                                                LogEvento event = new LogEvento();
                                                event.setOrigenEvento("Android");
                                                event.setDescripcionEvento("Envio Excel :" + finalTitulo);
                                                event.setUsuarioEntidad(midUser.toString());
                                                mPresenter.createLog(event);
                                            }
                                        });


                                    }
                                }).start();
                            }
                            else{
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if (v.getTooltipText() != null){
                                        ;


                                        Intent browserIntent = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTooltipText().toString()));
                                        }
                                        LogEvento event = new LogEvento();
                                        event.setOrigenEvento("Android");
                                        event.setDescripcionEvento("Apertura Errepar.com :" + finalTitulo);
                                        event.setUsuarioEntidad(midUser.toString());
                                        mPresenter.createLog(event);
                                        mCtx.startActivity(browserIntent);
                                    }
                                    else{

                                        LogEvento event = new LogEvento();
                                        event.setOrigenEvento("Android");
                                        event.setDescripcionEvento("Apertura documento :" + finalTitulo);
                                        event.setUsuarioEntidad(midUser.toString());
                                        mPresenter.createLog(event);
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
                                        /*else{
                                            if (file.exists())
                                            {
                                                pdfView.fromFile(file).load();
                                                pdfView.setVisibility(View.VISIBLE);

                                            }
                                            else{
                                                Toast toast = Toast.makeText(mCtx, "Debe permitir la descarga de contenido.", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        }*/
                                    }
                                }
                            }
                        } else {
                            if (file.exists()) {
                                if (!file.getName().contains("xls"))
                                {
                                    LogEvento event = new LogEvento();
                                    event.setOrigenEvento("Android");
                                    event.setDescripcionEvento("Preview PDF :" + finalTitulo);
                                    event.setUsuarioEntidad(midUser.toString());
                                    mPresenter.createLog(event);
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
        }
        if (detalle.getVideoTitle() == null || detalle.getVideoTitle().equals("")) {
            holder.txtLinkVimeo.setVisibility(View.GONE);
            holder.vidIco.setVisibility(View.GONE);
        } else  {
            if (detalle.getVideoUrl() != null){
                holder.txtLinkVimeo.setText(detalle.getVideoTitle());
                holder.txtLinkVimeo.setVisibility(View.VISIBLE);
                holder.vidIco.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.txtLinkVimeo.setTooltipText(detalle.getVideoUrl());
                }
                holder.txtLinkVimeo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTooltipText().toString()));
                        }
                        LogEvento event = new LogEvento();
                        event.setOrigenEvento("Android");
                        event.setDescripcionEvento("Apertura video :" + holder.txtLinkVimeo.getText());
                        event.setUsuarioEntidad(midUser.toString());
                        mPresenter.createLog(event);
                        mCtx.startActivity(browserIntent);
                    }
                });
            }
        }
        if (detalle.getAudioTitle() == null) {
            holder.txtLinkAudio.setVisibility(View.GONE);
            holder.audIco.setVisibility(View.GONE);
        } else{
            if (detalle.getAudioUrl() != null) {
                holder.txtLinkAudio.setText(detalle.getAudioTitle());
                holder.txtLinkAudio.setVisibility(View.VISIBLE);
                holder.audIco.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.txtLinkAudio.setTooltipText(detalle.getAudioUrl());
                }
                holder.txtLinkAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTooltipText().toString()));
                        }
                        LogEvento event = new LogEvento();
                        event.setOrigenEvento("Android");
                        event.setDescripcionEvento("Apertura Audio :" + holder.txtLinkAudio.getText());
                        event.setUsuarioEntidad(midUser.toString());
                        mPresenter.createLog(event);
                        mCtx.startActivity(browserIntent);
                    }
                });
            }
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

                    try {
                        File.createTempFile(finalTitulo + detalle.getFileUrl(), null, mCtx.getCacheDir());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File cacheFile = new File(mCtx.getCacheDir(), finalTitulo + detalle.getFileUrl());
                    if(!cacheFile.exists()){
                        cacheFile.mkdirs();
                    }
                    String name = downloadFile(finalDownloadURL,cacheFile.getAbsolutePath());

                    Uri uri = FileProvider.getUriForFile(mCtx, mCtx.getApplicationContext().getPackageName() + ".provider", cacheFile);
                    Intent sharableIntent = new Intent();
                    List<ResolveInfo> resInfoList = mCtx.getPackageManager().queryIntentActivities(sharableIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        mCtx.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    sharableIntent.setAction(Intent.ACTION_SEND);
                    sharableIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION );
                    sharableIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                    sharableIntent.setType("*/*");
                    sharableIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sharableIntent.putExtra(Intent.EXTRA_SUBJECT, finalTitulo);
                    sharableIntent.putExtra(Intent.EXTRA_TITLE, finalTitulo);

                    LogEvento event = new LogEvento();
                    event.setOrigenEvento("Android");
                    event.setDescripcionEvento("Compartir archivo :" + finalTitulo);
                    event.setUsuarioEntidad(midUser.toString());
                    mPresenter.createLog(event);

                    mCtx.startActivity(sharableIntent);
                    holder.share.setChecked(false);
                }
            }
        });
    }

    public String downloadFile(String fileURL, String fileName) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d(TAG, "Downloading...");
        try {
            int lastDotPosition = fileName.lastIndexOf('/');
            if( lastDotPosition > 0 ) {
                String folder = fileName.substring(0, lastDotPosition);
                File fDir = new File(folder);
                fDir.mkdirs();
            }

            //Log.i(TAG, "URL: " + fileURL);
            //Log.i(TAG, "File: " + fileName);
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(30000);
            c.connect();
            double fileSize  = (double) c.getContentLength();
            int counter = 0;
            while ( (fileSize == -1) && (counter <=30)){
                c.disconnect();
                u = new URL(fileURL);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(30000);
                c.connect();
                fileSize  = (double) c.getContentLength();
                counter++;
            }

            File fOutput = new File(fileName);
            if (fOutput.exists())
                fOutput.delete();

            BufferedOutputStream f = new BufferedOutputStream(new FileOutputStream(fOutput));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[8192];
            int len1 = 0;
            int downloadedData = 0;
            while ((len1 = in.read(buffer)) > 0) {
                downloadedData += len1;
                f.write(buffer, 0, len1);
            }
            Log.d(TAG, "Finished");
            f.close();

            return fileName;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return null;
        }
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
