package com.project.group.projectga.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.project.group.projectga.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends CoreActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private Drawer result = null;
    AccountHeader headerResult;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (getUid() != null) {
            String userId = getUid();
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        } else {
            onAuthFailure();
        }

        final PrimaryDrawerItem home = new PrimaryDrawerItem().withName("Home").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_home);
        final PrimaryDrawerItem profile = new PrimaryDrawerItem().withName("Profile").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_account);
        final PrimaryDrawerItem gallery = new PrimaryDrawerItem().withName("Gallery").withIdentifier(3).withIcon(R.drawable.ic_perm_media_black_24dp);
        final PrimaryDrawerItem recognition = new PrimaryDrawerItem().withName("Recognition").withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_face);
        final PrimaryDrawerItem maps = new PrimaryDrawerItem().withName("Maps").withIdentifier(5).withIcon(R.drawable.ic_place_black_24dp);
        final PrimaryDrawerItem tagAndLocate = new PrimaryDrawerItem().withName("Tag & Locate").withIdentifier(6).withIcon(R.drawable.ic_remove_red_eye_black_24dp);
        final PrimaryDrawerItem gamesAndPuzzle = new PrimaryDrawerItem().withName("Games & Puzzles").withIdentifier(7).withIcon(R.drawable.ic_casino_black_24dp);
        final PrimaryDrawerItem backup = new PrimaryDrawerItem().withName("Backup").withIdentifier(8).withIcon(GoogleMaterial.Icon.gmd_save);
        final PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIdentifier(9).withIcon(FontAwesome.Icon.faw_sign_out);

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).fit().centerCrop().into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        String name = "Ramji Seetharaman";
        String email = "ramji.sepak@gmail.com";
        final ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(name).withEmail(email).withIcon(R.drawable.ic_account_circle_white_24dp);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(userProfile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(home)
                .addDrawerItems(profile)
                .addDrawerItems(gallery)
                .addDrawerItems(recognition)
                .addDrawerItems(maps)
                .addDrawerItems(tagAndLocate)
                .addDrawerItems(gamesAndPuzzle)
                .addDrawerItems(backup)
                .addDrawerItems(new DividerDrawerItem())
                .addDrawerItems(logout)
                .buildForFragment();

        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int drawItemId = (int) drawerItem.getIdentifier();
                Intent intent;
                    if (drawItemId == 9) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        intent = new Intent(MainMenuActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                return false;
            }
        });
    }
    private void onAuthFailure() {
        Intent intent = new Intent(MainMenuActivity.this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (firebaseAuth.getCurrentUser() == null) {
            onAuthFailure();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        result.closeDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
