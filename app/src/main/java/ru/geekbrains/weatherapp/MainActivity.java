package ru.geekbrains.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private User user;
    private boolean submitted = false;

    SharedPreferences sPref;
    private static final String USER_AVATAR = "USER_AVATAR";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_SUBMITTED = "USER_SUBMITTED";
    private static final String LAST_SEARCH = "LAST_SEARCH";

    private static final int AVATAR_FROM_CAMERA = 10,
                             AVATAR_FROM_GALLERY = 20,
                             AVATAR_FROM_WEB = 30;

    private NavigationView navigationView;
    private View navViewHeader;
    private ImageView ivAvatar;
    private EditText etName, etEmail;
    private Button btnSubmit;

    private String lastSearchStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navViewHeader = navigationView.getHeaderView(0);

        ivAvatar = navViewHeader.findViewById(R.id.ivAvatar);
        etName = navViewHeader.findViewById(R.id.etUserName);
        etEmail = navViewHeader.findViewById(R.id.etUserEmail);
        btnSubmit = navViewHeader.findViewById(R.id.btnSubmit);

        //fix, не работает ни AvatarView ни ImageView DrawerLayout avt, imgView is null
//        AvatarView avt = findViewById(R.id.avAvatar);
//        avt.bind("new user", "http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png");
//        ivAvatar = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);.setImageURI(Uri.parse("http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png"));

        loadPrefs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setPrefs();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.matches("^http(s?):\\/\\/(\\w+(.)+)")) {
                    getWeather(query, false);
                } else {
                    lastSearchStr = query;
                    Toast.makeText(getApplication(),"Now search weather...", Toast.LENGTH_SHORT).show();
                    getWeather(query, true);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void getWeather(String query, boolean weatherData) {
        final WebView webView = findViewById(R.id.browse);
        final RequestMaker requestMaker = new RequestMaker(new RequestMaker.OnRequestListener() {
            // Обновим прогресс
            @Override
            public void onStatusProgress(String updateProgress) {
                Toast.makeText(getApplication(), updateProgress, Toast.LENGTH_SHORT).show();
            }
            // По окончании загрузки страницы вызовем этот метод, который и вставит текст в WebView
            @Override
            public void onComplete(String result) {
                webView.loadDataWithBaseURL(null, result, "text/html; charset=utf-8", "utf-8", null);
            }
        });

        if (weatherData) requestMaker.setWeatherRequest();
        requestMaker.make(query);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_sensors:
                Intent intent = new Intent(this, SensorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSubmit:
                submitted = true;

                //TODO: add avatar Uri into constructor params
                if (user == null) {
                    user = new User(etName.getText().toString(), etEmail.getText().toString());
                } else {
                    user.setUserName(etName.getText().toString());
                    user.setUserEmail(etEmail.getText().toString());
                }
                setPrefs();
                updateDrawer();
                break;
            case R.id.ivAvatar:
            case R.id.ivAvatarOptions:
                chooseAvatarPopupShow(view);
                break;
        }

        updateDrawer();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.chooseAvatarFromCamera:
                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File tempFile = File.createTempFile("avatar", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    Uri uri = Uri.fromFile(new File(tempFile.getAbsolutePath()));
                    setUserAvatarUri(uri);

                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    grantUriPermission(
                            "com.google.android.GoogleCamera",
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    startActivityForResult(intent, AVATAR_FROM_CAMERA);
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chooseAvatarFromGalery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, AVATAR_FROM_GALLERY);
                break;
                default: return false;
        }

        return true;
    }

    public void chooseAvatarPopupShow(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.avatar_menu);

        try {
            Field fieldMPopup = PopupMenu.class.getDeclaredField("mPopup");
            fieldMPopup.setAccessible(true);

            Object mPopup = fieldMPopup.get(popupMenu);
            mPopup.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(mPopup, true);

        } catch (Exception e) {
            Log.e("No icons", "Error showing popup menu icons", e);
        }

        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        //String name = data.getStringExtra("name");
        switch(requestCode) {
            case AVATAR_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    //TODO fix camera avatar : open failed: EACCES (Permission denied)
//                    018-12-30 15:04:17.943 22027-22027/? E/CAM_StateSavePic: exception while saving result to URI: Optional.of(file:///data/user/0/ru.geekbrains.weatherapp/cache/avatar3170511085545866042.jpg)
//                    java.io.FileNotFoundException: open failed: EACCES (Permission denied)
//                    at android.os.ParcelFileDescriptor.openInternal(ParcelFileDescriptor.java:313)
//                    at android.os.ParcelFileDescriptor.open(ParcelFileDescriptor.java:211)
//                    at android.content.ContentResolver.openAssetFileDescriptor(ContentResolver.java:1290)
//                    at android.content.ContentResolver.openOutputStream(ContentResolver.java:1055)
//                    at android.content.ContentResolver.openOutputStream(ContentResolver.java:1031)
//                    at com.android.camera.captureintent.state.StateSavingPicture.onEnter(StateSavingPicture.java:85)
//                    at com.android.camera.captureintent.stateful.StateMachineImpl.jumpToState(StateMachineImpl.java:62)
//                    at com.android.camera.captureintent.stateful.StateMachineImpl.processEvent(StateMachineImpl.java:110)
//                    at com.android.camera.captureintent.state.StateOpeningCamera$9.onClick(StateOpeningCamera.java:307)

                    Bundle bndl = data.getExtras();
                    if (bndl != null) {
                        Bitmap photo = (Bitmap) bndl.get("data");
                        ivAvatar.setImageBitmap(photo);

                        setUserAvatarUri((Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT));
                    } else {
                        ivAvatar.setImageURI(user.getUserAvatarUri());
                    }

                }
            case AVATAR_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    //setAvatarFromGallery(ivAvatar, data);
                    ivAvatar.setImageURI(data.getData());

                    setUserAvatarUri(data.getData());
                }
                break;
            case AVATAR_FROM_WEB: //TODO get avatar from web
                break;
        }
    }

    private void setUserAvatarUri(Uri userAvatarUri) {
        if (user == null) {
            user = new User(etName.getText().toString(), etEmail.getText().toString(), userAvatarUri);
        } else user.setUserAvatarUri(userAvatarUri);
    }

//    public void setAvatarFromGallery(ImageView v, Intent data) {
//        Uri uri = data.getData();
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null);
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(projection[0]);
//        String filePath = cursor.getString(columnIndex);
//        cursor.close();
//    }

    public void loadPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        user = new User(sPref.getString(USER_NAME, ""), sPref.getString(USER_EMAIL, ""), Uri.parse(sPref.getString(USER_AVATAR, "@mipmap/ic_launcher_round")));

        submitted = sPref.getBoolean(USER_SUBMITTED, false);
        lastSearchStr = sPref.getString(LAST_SEARCH, "");
        if (!lastSearchStr.isEmpty()) {
            setTitle(lastSearchStr);
            getWeather(lastSearchStr, true);
        }

        updateDrawer();
    }

    public void setPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(USER_NAME, user.getUserName());
        ed.putString(USER_EMAIL, user.getUserEmail());
        ed.putBoolean(USER_SUBMITTED, submitted);
        ed.putString(USER_AVATAR, user.getUserAvatarUri().toString());

        ed.putString(LAST_SEARCH, lastSearchStr);
        ed.apply();
    }

    public void updateDrawer() {
        if (submitted && user == null) submitted = false;

        if (user != null) {
            //TODO fix "Permission Denial: opening provider com.google.android.apps.photos.contentprovider.impl.MediaContentProvider"
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
            }

            Uri uri = user.getUserAvatarUri();

//            if (!uri.toString().equals("")) ivAvatar.setImageURI(Uri.parse(uri.toString()));
            ImageLoader imageLoader = ImageLoader.getInstance(); // Получили экземпляр
            imageLoader.init(ImageLoaderConfiguration.createDefault(this)); // Проинициализировали конфигом по умолчанию
            imageLoader.displayImage(uri.getPath(), ivAvatar); // Запустили асинхронный показ картинки

            if (etName.getText().toString().equals("")) etName.setText(user.getUserName());
            if (etEmail.getText().toString().equals("")) etEmail.setText(user.getUserEmail());
        }

        if (submitted) {
            etName.setEnabled(false);
            etEmail.setEnabled(false);
            btnSubmit.setVisibility(View.GONE);
        } else {
            etName.setEnabled(true);
            etEmail.setEnabled(true);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }
}
