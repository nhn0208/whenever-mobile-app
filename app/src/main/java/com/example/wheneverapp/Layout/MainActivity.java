package com.example.wheneverapp.Layout;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wheneverapp.Fragment.CartFragment;
import com.example.wheneverapp.Fragment.HomeFragment;
import com.example.wheneverapp.Fragment.NotificationFragment;
import com.example.wheneverapp.Fragment.OrderFragment;
import com.example.wheneverapp.Fragment.ProfileFragment;
import com.example.wheneverapp.R;
import com.example.wheneverapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_cart) {
                selectedFragment = new CartFragment();
            } else if (item.getItemId() == R.id.navigation_order) {
                selectedFragment = new OrderFragment();
            }else if (item.getItemId() == R.id.navigation_notifications) {
                selectedFragment = new NotificationFragment();
            }else if (item.getItemId()==R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }
    private void loadFragment(Fragment fragment) {
        // Thay thế fragment hiện tại bằng fragment mới
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}