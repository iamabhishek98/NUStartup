package orbital.gns.nustartup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import orbital.gns.nustartup.login.LoginActivity;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) goToLogin();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(FeedFragment.newInstance("", ""));
    }

        public void openFragment (Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    checkLoggedOut();
                    switch (item.getItemId()) {
                        case R.id.navigation_feed:
                            openFragment(FeedFragment.newInstance("", ""));
                            checkLoggedOut();
                            return true;
                        case R.id.navigation_groups:
                            openFragment(GroupsFragment.newInstance("", ""));
                            checkLoggedOut();
                            return true;
                        case R.id.navigation_notifications:
                            openFragment(NotificationFragment.newInstance("", ""));
                            checkLoggedOut();
                            return true;
                        case R.id.navigation_startup:
                            openFragment(CreateStartUp.newInstance("", ""));
                            checkLoggedOut();
                            return true;
                        case R.id.navigation_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            checkLoggedOut();
                            return true;
                    }
                    return false;
                }
            };

        @Override
        public void onStart () {
            super.onStart();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                goToLogin();
            } else if (!currentUser.isEmailVerified()) {
                Toast.makeText(MainActivity.this, "You are not verified yet.", Toast.LENGTH_SHORT).show();
            }
        }

        public void goToLogin () {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        public void checkLoggedOut() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                goToLogin();
            }
        }

}
