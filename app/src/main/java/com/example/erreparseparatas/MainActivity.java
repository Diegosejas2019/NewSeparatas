package com.example.erreparseparatas;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.erreparseparatas.views.ActivarLibroFragment;
import com.example.erreparseparatas.views.ContactanosFragment;
import com.example.erreparseparatas.views.DescargarContenidoFragment;
import com.example.erreparseparatas.views.DetalleFragment;
import com.example.erreparseparatas.views.MisPublicacionesFragment;
import com.example.erreparseparatas.views.TerminosYCondicionesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    Context context;
    private static final String TAG = "info hash";
    private AppBarConfiguration mAppBarConfiguration;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    private InstallStateUpdatedListener mInstallStateUpdatedListener;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setOverflowIcon(null);
        setSupportActionBar(toolbar);
        Fragment nextFrag= new Fragment();

        checkUpdate();

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
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment nextFrag = new Fragment();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_cerrar:
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


//    @Override
//    public void onBackPressed() {
//        if(getFragmentManager().getBackStackEntryCount() > 0){
//            getFragmentManager().popBackStackImmediate();
//        }
//        else{
//            super.onBackPressed();
//        }
//    }

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