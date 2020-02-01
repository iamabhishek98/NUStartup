package orbital.gns.nustartup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import java.net.URI;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;
    private User userProfile;
    private Uri filePath;

    private Button logoutButton;
    private Button saveButton;
    private ImageView mImageView;
    private String imageURI;

    private EditText nameTextView;
    private EditText phoneNumberText;
    private EditText bioTextView;
    private EditText linkedinTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //Firebase
//    FirebaseStorage storage;
//    StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = mAuth.getUid();
        Log.d("Debug", uid);
        FirebaseDatabase.getInstance().getReference("/users/" + uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("Debug", "Here");
                        userProfile = dataSnapshot.getValue(User.class);
                        Log.d("Debug", userProfile.email);
                        setInformation(userProfile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

    }

    private void setInformation(User user) {
        if (user.name != null) {
            nameTextView.setText(user.name);
        }
        if (user.phoneNumber != null) {
            phoneNumberText.setText(user.phoneNumber);
        }
        if (user.biography != null) {
            bioTextView.setText(user.biography);
        }
        if (user.url != null) {
            linkedinTextView.setText(user.url);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1000) {
//<<<<<<< HEAD
//            filePath = data.getData();
//            mImageView.setImageURI(filePath);
//=======
            mImageView.setImageURI(data.getData());
            imageURI = data.getData().toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        phoneNumberText = view.findViewById(R.id.phoneNumberText);
        bioTextView = view.findViewById(R.id.bioTextView);
        linkedinTextView = view.findViewById(R.id.linkedinTextView);

        mImageView = view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1001);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        logoutButton = view.findViewById(R.id.logoutButton1);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSaveData();
            }
        });
        return view;
    }

    private void checkAndSaveData() {
        String name = nameTextView.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();
        String biography = bioTextView.getText().toString();
        String url = linkedinTextView.getText().toString();
        if (!name.isEmpty()) {
            userProfile.name = name;
        }
        if (!phoneNumber.isEmpty()) {
            userProfile.phoneNumber = phoneNumber;
        }
        if (!biography.isEmpty()) {
            userProfile.biography = biography;
        }
        if (!url.isEmpty()) {
            userProfile.url = url;
        }
        FirebaseDatabase.getInstance().getReference("/users/" + uid)
            .setValue(userProfile);
        //uploadImage();
    }

//    private void uploadImage() {
//
//        if(filePath != null)
//        {
//            final ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
//            ref.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                        }
//                    });
//        }
//    }




//    private void uploadImagetoFirebase(Uri image) {
//        if (image == null) {
//            return;
//        }
//
//        String filename = UUID.randomUUID().toString();
//        StorageReference ref = FirebaseStorage.getInstance().getReference("/images/$filename");
//
//        ref.putFile(image).addOnSuccessListener({
//                ref.getDownloadUrl().addOnSuccessListener();
//        });
//
//            .addOnSuccessListener {
//            ref.downloadUrl.addOnSuccessListener {
//                //Toast.makeText(this@RegisterActivity, "Image successfully created", Toast.LENGTH_SHORT).show()
//                saveUsertoFirebase(it.toString())
//            }
//        }
//            .addOnFailureListener {
//            Log.d("Debug", "uploadImagetoFirebase failed")
//            Toast.makeText(this@RegisterActivity, "Image not successfully created", Toast.LENGTH_SHORT).show()
//        }
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
