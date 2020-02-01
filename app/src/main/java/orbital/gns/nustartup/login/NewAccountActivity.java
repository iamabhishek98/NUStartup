package orbital.gns.nustartup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import orbital.gns.nustartup.MainActivity;
import orbital.gns.nustartup.R;
import orbital.gns.nustartup.User;

public class NewAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView emailText;
    private TextView password1Text;
    private TextView password2Text;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.email_TextView);
        password1Text = findViewById(R.id.password_TextView);
        password2Text = findViewById(R.id.password2_TextView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("users");
    }

    public void createNewAccount(View view) {
        final String email = emailText.getText().toString();
        final String password1 = password1Text.getText().toString();
        String password2 = password2Text.getText().toString();
        if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
        } else if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification();
                                String uid = FirebaseAuth.getInstance().getUid();
                                ref = database.getReference("/users/" + uid);
                                ref.setValue(new User(email, password1));
                                Toast.makeText(NewAccountActivity.this, "Account Successfully created. Please verify your email to unlock features.", Toast.LENGTH_SHORT).show();
                                gotoMainMenu();
                            } else {
                                Toast.makeText(NewAccountActivity.this, "Account not successfully created.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void gotoMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
