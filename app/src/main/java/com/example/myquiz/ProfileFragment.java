package com.example.myquiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myquiz.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         retriveData();
         getRnak();
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogoutConfirmationDialog();
            }
        });
    }

    public void getRnak()
    {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            database.collection("users")
                    .orderBy("coins", Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int rank = 1; // Start with rank 1

                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                User user = snapshot.toObject(User.class);
                                if (user != null && userId.equals(snapshot.getId())) {
                                    // Found the user, display the rank
                                    binding.rankP.setText(String.valueOf(rank)+"\nRank");
                                    break; // No need to continue iterating
                                }
                                rank++; // Increment the rank
                            }
                        }
                    });
        }

    }

    public void retriveData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            long coinsP = user.getCoins();
                            String usernameP = user.getName();
                            String emailP= user.getEmail();

                            binding.userNameP.setText(usernameP);
                            binding.coinsP.setText(String.valueOf(coinsP)+"\nTotal Coins");
                            binding.emailP.setText(emailP);


                        }
                    }
                });
    }

    private void showLogoutConfirmationDialog() {
        // Use the parent activity's context to create the AlertDialog
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Call the logout() method when the user confirms logout
                    logout();
                }
            });
            builder.setNegativeButton("Cancel", null);
            // Show the dialog
            builder.show();
        }
    }
    private void logout() {
        // Implement your logout logic here
        // For example, if you're using Firebase Authentication, you can sign out the user using the following code:

        FirebaseAuth.getInstance().signOut();

        // Navigate to the login page
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();

        // After signing out, you may want to navigate to a login screen or perform any other necessary actions
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
