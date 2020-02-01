package orbital.gns.nustartup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import orbital.gns.nustartup.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailText = findViewById(R.id.emailText);
    }


    public void sendRecoveryLink(View view) {
        String emailToSendPasswordResetEmail = emailText.getText().toString();
        if (emailToSendPasswordResetEmail.isEmpty()) {
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailToSendPasswordResetEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent. Please check.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
