package com.cmpe277.onlinemilktea;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {


    private static int APP_REQUEST_CODE = 7172;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    //private ICloudFunctions cloudFunctions;

    private DatabaseReference userRef;

    private List<AuthUI.IdpConfig> providers;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener != null) {
            firebaseAuth.removeAuthStateListener(listener);
        }
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        // new AuthUI.IdpConfig.PhoneBuilder().build()
        providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();


        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // already log in
                Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();


                checkUserFromFirebase(user);
            } else {

                Login();
            }
        };
    }

    private void checkUserFromFirebase(FirebaseUser user) {
       dialog.show();

        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //Toast.makeText(MainActivity.this, "You already registered!", Toast.LENGTH_SHORT).show();

                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            goToHomeActivity(userModel);
                        }
                        else {
                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showRegisterDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("One more step");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        // EditText edt_name = (EditText) itemView.findViewById(R.id.edt_name);
        EditText edt_address = (EditText) itemView.findViewById(R.id.edt_address);
        EditText edt_phone = (EditText) itemView.findViewById(R.id.edt_phone);

        // set
        edt_phone.setText(user.getPhoneNumber());
        builder.setView(itemView);
//        builder.setNegativeButton("CANCEL", (dialog, which) -> {
//            dialog.dismiss();
//        });
        builder.setPositiveButton("Sign In", (dialog, which) -> {
            if (TextUtils.isEmpty(edt_address.getText().toString())) {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
                showRegisterDialog(user);
                return;
            } else if (TextUtils.isEmpty(edt_phone.getText().toString())) {
                Toast.makeText(this, "Please enter your phone", Toast.LENGTH_SHORT).show();
                showRegisterDialog(user);
                return;
            }


            UserModel userModel = new UserModel();
            userModel.setUid(user.getUid());
            userModel.setEmail(user.getEmail());
            userModel.setName(user.getDisplayName());
            System.out.print(user.getDisplayName());
            userModel.setAddress(edt_address.getText().toString());
            userModel.setPhone(edt_phone.getText().toString());


            userRef.child(user.getUid()).setValue(userModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable(){
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        goToHomeActivity(userModel);
                                    }
                                });
                            }
                        }
                    });
        });

        builder.setView(itemView);


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToHomeActivity(UserModel userModel) {

                FirebaseInstanceId.getInstance().getInstanceId()
                .addOnFailureListener(e -> {
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    Common.currentUser = userModel; // Important, you need alwayw assign value for it before use

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();

                })
                .addOnCompleteListener(task -> {
                    String token = "";
                    Common.currentUser = userModel; // Important, you need always assign value for it before use
                    token = task.getResult().getToken();
                    Common.currentToken = token;
                    Common.updateToken(MainActivity.this, task.getResult().getToken());
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();

                });
        Common.currentUser = userModel;

        // startActivity
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }


    private void Login() {

       startActivityForResult(AuthUI.getInstance()
               .createSignInIntentBuilder()
               .setLogo(R.drawable.teahouselogo)
               .setTheme(R.style.LoginTheme)
               .setAvailableProviders(providers).build(),
               APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed to sign in!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
