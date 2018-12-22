package ru.geekbrains.weatherapp;

import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import me.fahmisdk6.avatarview.AvatarView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    private boolean submited = false;

    SharedPreferences sPref;
    private static final String USER_AVATAR = "USER_AVATAR";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PHONE = "USER_PHONE";

    private NavigationView navigationView;

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

        //fix, не работает ни AvatarView ни ImageView DrawerLayout avt, imgView is null
//        AvatarView avt = findViewById(R.id.avAvatar);
//        avt.bind("new user", "http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png");
//        ImageView imgView = findViewById(R.id.ivAvatar);
//        imgView.setImageURI(Uri.parse("http://img3.wikia.nocookie.net/__cb20131019015927/marvelheroes/images/b/b1/Spiderman_Superior.png"));

        loadPrefs();
        updateDrawer();
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

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnLogin:
                submited = true;
                EditText etName = findViewById(R.id.etName),
                        etEmail = findViewById(R.id.etEmail),
                        etPhone = findViewById(R.id.etPhone);
                user = new User(etName.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString());
                setPrefs();
                break;
            case R.id.ivAvatar:
                break;
        }

        updateDrawer();
    }

    public void loadPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        user = new User(sPref.getString(USER_NAME, ""), sPref.getString(USER_EMAIL, ""), sPref.getString(USER_PHONE, ""));
    }

    public void setPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(USER_NAME, user.getUserName());
        ed.putString(USER_EMAIL, user.getUserEmail());
        ed.putString(USER_PHONE, user.getUserPhone());
        ed.putString(USER_AVATAR, user.getUserAvatar().toString());
        ed.apply();
    }

    public void updateDrawer() {
        if (submited && user == null) submited = false;

        if (submited) {
            TextView tvUserName = findViewById(R.id.tvUserName),
                     tvUserEmail = findViewById(R.id.tvUserEmail);

            tvUserName.setText(user.getUserName());
            tvUserEmail.setText(user.getUserEmail());
        }
    }
}
