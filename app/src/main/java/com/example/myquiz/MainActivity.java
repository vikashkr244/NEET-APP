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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myquiz.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
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


//        setSupportActionBar(binding.toolbar);
//        getSupportActionBar().setTitle(""); // Set an empty string as the title
////        binding.totalCoins.setText("123");
//  retrieveCoinsFromFirebase();
       replaceFrag(new HomeFragment());



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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.wallet) {
            Toast.makeText(this, "wallet is clicked.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

//    public void retrieveCoinsFromFirebase() {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//
//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        User user = documentSnapshot.toObject(User.class);
//                        if (user != null) {
//                            long coins = user.getCoins();
//                            //totalCoins = binding.totalCoinsTextView;
//                           binding.totalCoins.setText(String.valueOf(coins));
//                        }
//                    }
//                });
//    }
}