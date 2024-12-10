package com.example.mislibros;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mislibros.R;
import com.example.mislibros.model.Publicaciones;
import com.example.mislibros.views.ActivarLibroFragment;
import com.example.mislibros.views.ContactanosFragment;
import com.example.mislibros.views.DescargarContenidoFragment;
import com.example.mislibros.views.DetalleFragment;
import com.example.mislibros.views.MisPublicacionesFragment;
import com.example.mislibros.views.TerminosYCondicionesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.android.gms.tasks.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Publicaciones> publicaciones;
    Context context;
    private static final String TAG = "info hash";
    private AppBarConfiguration mAppBarConfiguration;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    private InstallStateUpdatedListener mInstallStateUpdatedListener;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final int PERMISSION_REQUEST_CODE =112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getLayoutInflater().getContext();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setOverflowIcon(null);
        setSupportActionBar(toolbar);
        Fragment nextFrag= new Fragment();
        FirebaseApp.initializeApp(this);
        checkUpdate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.POST_NOTIFICATIONS)) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
                } else {
                    //request the permission
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
                }

            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        String publicacionid;
        String linkImg;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                publicacionid= null;
                linkImg = null;
            } else {
                publicacionid= extras.getString("publicacionid");
                linkImg= extras.getString("linkImg");
                nextFrag = new DetalleFragment();
                Bundle args = new Bundle();
                args.putString("linkImg", linkImg);
                args.putString("publicacionid", publicacionid);

                nextFrag.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            publicacionid= (String) savedInstanceState.getSerializable("publicacionid");
            linkImg= (String) savedInstanceState.getSerializable("linkImg");
            Bundle bundle=new Bundle();
            bundle.putString("publicacionid", publicacionid);
            bundle.putString("linkImg", linkImg);
            nextFrag = new MisPublicacionesFragment();
            nextFrag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }

        if (savedInstanceState == null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_activar, R.id.nav_descarga, R.id.nav_terminos, R.id.nav_contactanos)
                    .setDrawerLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment nextFrag = new Fragment();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_cerrar:
                            mReadJsonData("BooksJson");
                            publicaciones.forEach(publicacion -> {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(String.valueOf(publicacion.getCode()));
                            });
                            SharedPreferences spreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spreferencesEditor = spreferences.edit();
                            spreferencesEditor.clear();
                            spreferencesEditor.commit();
                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            spreferencesEditor = prefs.edit();
                            spreferencesEditor.clear();
                            spreferencesEditor.commit();
                            Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(mainIntent);
                                MainActivity.this.finish();
                                overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);
                                try {
                                    String filename = "BooksJson";
                                    FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                                    File file = new File(String.valueOf(outputStream));
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case R.id.nav_activar:
                                if (isConnected()) {
                                    nextFrag = new ActivarLibroFragment();
                                }
                                break;
                            case R.id.nav_descarga:
                                if (isConnected()) {
                                    nextFrag = new DescargarContenidoFragment();
                                }
                                break;
                            case R.id.nav_contactanos:
                                nextFrag = new ContactanosFragment();
                                break;
                            case R.id.nav_mispublicaciones:
                                nextFrag = new MisPublicacionesFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("Ingreso", "Menu");
                                nextFrag.setArguments(bundle);
                                break;
                            case R.id.nav_terminos:
                                nextFrag = new TerminosYCondicionesFragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                        drawer.closeDrawers();
                        return true;
                    }
            });
        }
        if (hasStoragePermission(112)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }
            }

        }


    }


    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }

    private boolean hasStoragePermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // if user denies permission then Dialog would be pop up. That's why I commented out both toast and finish() method below.
            }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.POST_NOTIFICATIONS)) {
            showAlertDialog();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Para poder recibir las notificaciones de la aplicación debe otorgar el permiso en el menu de ajustes.");
        alertDialogBuilder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //makePermissionRequest();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    public void mReadJsonData(String params) {
        try {
            FileInputStream fis = context.openFileInput(params);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            Type listPublicaciones = new TypeToken<ArrayList<Publicaciones>>(){}.getType();
            List offlineProducts = gson.fromJson(json, listPublicaciones);
            publicaciones = offlineProducts;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mInstallStateUpdatedListener = new
                InstallStateUpdatedListener() {
                    @Override
                    public void onStateUpdate(InstallState state) {
                        if (state.installStatus() == InstallStatus.DOWNLOADED){
                            popupSnackbarForCompleteUpdate();
                        } else if (state.installStatus() == InstallStatus.INSTALLED){
                            if (mAppUpdateManager != null){
                                mAppUpdateManager.unregisterListener(mInstallStateUpdatedListener);
                            }

                        } else {
                            Log.i("TAG", "InstallStateUpdatedListener: state: " + state.installStatus());
                        }
                    }
                };

        mAppUpdateManager.registerListener(mInstallStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("TAG2", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.content),
                        "La nueva aplicación está lista!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Instalar", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }


}