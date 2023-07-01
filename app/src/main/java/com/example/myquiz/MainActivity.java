package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myquiz.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

       replaceFrag(new HomeFragment());

//        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public boolean onItemSelect(int i) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                switch (i) {
//                    case 0:
//                        transaction.replace(R.id.content, new HomeFragment());
//                        transaction.commit();
//                        break;
//                    case 1:
//                        transaction.replace(R.id.content, new LeaderboardFragment());
//                        transaction.commit();
//                        break;
//                    case 2:
//                        transaction.replace(R.id.content, new WalletFragment());
//                        transaction.commit();
//                        break;
//                    case 3:
//                        transaction.replace(R.id.content, new ProfileFragment());
//                        transaction.commit();
//                        break;
//                }
//                return false;
//            }
//        });

binding.bottomBar.setOnItemSelectedListener(item -> {
    switch (item.getItemId()){
        case  R.id.home:
            replaceFrag(new HomeFragment());
            break;
        case R.id.rank:

            replaceFrag(new LeaderboardFragment());
            break;
        case R.id.wallet:

            replaceFrag(new WalletFragment());
            break;
        case R.id.profile:

            replaceFrag(new ProfileFragment());
            break;
    }
    return true;
});

    }
    private  void replaceFrag(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.wallet) {
            Toast.makeText(this, "wallet is clicked.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}