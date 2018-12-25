package ru.geekbrains.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.net.URI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private User user;
    private boolean submited = false;

    SharedPreferences sPref;
    private static final String USER_AVATAR = "USER_AVATAR";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";

    private static final int AVATAR_FROM_CAMERA = 10,
                             AVATAR_FROM_GALLERY = 20,
                             AVATAR_FROM_WEB = 30;

    private NavigationView navigationView;
    ImageView ivAvatar;

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

        ivAvatar = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);

        //fix, не работает ни AvatarView ни ImageView DrawerLayout avt, imgView is null
//        AvatarView avt = findViewById(R.id.avAvatar);
//        avt.bind("new user", "http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png");
//        ivAvatar = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);.setImageURI(Uri.parse("http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png"));

        loadPrefs();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
                submited = true;
                EditText etName = findViewById(R.id.etUserName),
                        etEmail = findViewById(R.id.etUserEmail);

                //TO DO
                // add avatar Uri into constructor params
                user = new User(etName.getText().toString(), etEmail.getText().toString());
                setPrefs();
                updateDrawer();
                break;
            case R.id.ivAvatar:
            case R.id.ivAvatarOptions:
                chooseAvatar(view);
                break;
        }

        updateDrawer();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.chooseAvatarFromCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, AVATAR_FROM_CAMERA);
                break;
            case R.id.chooseAvatarFromGalery:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, AVATAR_FROM_GALLERY);
                break;
            case R.id.chooseAvatarFromWeb:
                intent = new Intent(Intent.ACTION_PICK, Uri.parse("http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png"));
                startActivityForResult(intent, AVATAR_FROM_WEB);
                break;
                default: return false;
        }

        return true;
    }

    public void chooseAvatar(View view) {
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
            case AVATAR_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    setAvatarFromGallery(ivAvatar, data);
                    if (user == null) {
                        EditText etName = findViewById(R.id.etUserName),
                                etEmail = findViewById(R.id.etUserEmail);
                        user = new User(etName.getText().toString(), etEmail.getText().toString(), data.getData());
                    } else user.setUserAvatarUri(data.getData());
                }
                break;
            case AVATAR_FROM_WEB:
                break;
        }
    }

    public void setAvatarFromGallery(ImageView v, Intent data) {
//        Uri uri = data.getData();
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null);
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(projection[0]);
//        String filePath = cursor.getString(columnIndex);
//        cursor.close();

        v.setImageURI(data.getData());
    }

    public void loadPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        user = new User(sPref.getString(USER_NAME, ""), sPref.getString(USER_EMAIL, ""), Uri.parse(sPref.getString(USER_AVATAR, "")));

        updateDrawer();
    }

    public void setPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(USER_NAME, user.getUserName());
        ed.putString(USER_EMAIL, user.getUserEmail());
        ed.putString(USER_AVATAR, user.getUserAvatarUri().toString());
        ed.apply();
    }

    public void updateDrawer() {
        if (submited && user == null) submited = false;

        if (user != null) {
            Uri uri = user.getUserAvatarUri();
            if (!uri.toString().equals("")) ivAvatar.setImageURI(user.getUserAvatarUri());
        }

        if (submited) {
//            TextView tvUserName = findViewById(R.id.tvUserName),
//                     tvUserEmail = findViewById(R.id.tvUserEmail);

//            tvUserName.setText(user.getUserName());
//            tvUserEmail.setText(user.getUserEmail());
        }
    }
}
