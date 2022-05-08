package net.chargerevolutionapp.login;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.registration.RegistrationActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();
    EditText userNameField;
    EditText passwordField;
    ImageView loginImageView;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        Log.i(LOG_TAG, "onCreate()");

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInLauncher = buildGoogleActivityResultLauncher();

        // Load the images into the ImageView using the Glide library.
        loginImageView = findViewById(R.id.loginImage);
        // TODO
        // Glide.with(this.getApplicationContext()).load(R.drawable.pinterest_board_photo).into(loginImageView);

    }

    public void login(View view) {
        String userNameStr = this.userNameField.getText().toString();
        String passwordStr = this.passwordField.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(userNameStr, passwordStr)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "Login successful!");
                        goToHomeScreen();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginWithGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        this.googleSignInLauncher.launch(signInIntent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private ActivityResultLauncher<Intent> buildGoogleActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(LOG_TAG, "Google sign in failed", e);
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(LOG_TAG, "Google sign in successful!");
                            goToHomeScreen();
                        } else {
                            Log.i(LOG_TAG, "Google sign in failed!", task.getException());
                        }
                    }
                });
    }

}
