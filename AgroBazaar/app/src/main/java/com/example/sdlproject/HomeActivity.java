package com.example.sdlproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference noteref = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        final FragmentManager fragmentManager;

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
//        View hView = navigationView.inflateHeaderView(R.layout.navigation_header);

        noteref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String user_name = documentSnapshot.getString("name");

                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = (TextView) headerView.findViewById(R.id.user_profile);
                ImageView imageView = (ImageView) headerView.findViewById(R.id.profile_img);
                navUsername.setText(user_name);
                imageView.setImageResource(R.drawable.farmer);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {

                    case R.id.nav_home:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment()).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;

                    case R.id.nav_schemes:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment()).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SchemeFragment()).commit();
                        break;

                    case R.id.nav_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
//                        Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_logout:
                        Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        mAuth.signOut();
                        finishAffinity();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        break;

                    case R.id.nav_added:
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new OrderFragment()).commit();
                        break;

                    case R.id.nav_price:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PriceHelpFragment()).commit();
                        break;

                    case R.id.nav_success:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PastOrderFragment()).commit();
                        break;
                        
                    default:
                        return true;
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
