package orbital.gns.nustartup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStartUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileFragment.OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;
    private User userProfile;

    private Button uploadButton;

    private EditText startUpNameHolder;
    private EditText startUpIdeaHolder;
    private EditText startupSkillsHolder;
    private EditText startUpFounderHolder;
    private EditText startUpLocationHolder;
    private EditText startUpContactHolder;

    public CreateStartUp() {
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
    public static CreateStartUp newInstance(String param1, String param2) {
        CreateStartUp fragment = new CreateStartUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_start_up, container, false);
        setup(view);

        FirebaseDatabase.getInstance().getReference("/users/" + uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userProfile = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", userProfile.name);
                if(userProfile.name == null || userProfile.biography == null || userProfile.email == null
                        || userProfile.password == null || userProfile.phoneNumber == null){
                    Toast.makeText(getContext(), "Please update your profile first", Toast.LENGTH_LONG).show();
                    return;
                }
                String postUUID = UUID.randomUUID().toString();
                DataModel dataModel = new DataModel(startUpNameHolder.getText().toString(), startUpIdeaHolder.getText().toString()
                        , startupSkillsHolder.getText().toString(), startUpFounderHolder.getText().toString(),
                        startUpContactHolder.getText().toString(), startUpLocationHolder.getText().toString(), uid, postUUID);
                FirebaseDatabase.getInstance().getReference("/users/" + uid + "/organisations/" + postUUID).setValue(postUUID);
                Toast.makeText(getActivity(), "Created", Toast.LENGTH_SHORT).show();
                dataModel.members.put(uid, userProfile.name);
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("/posts/" + postUUID);
                Log.d("Debug",dataModel.dataModelUid);
                database.setValue(dataModel);

            }
        });

        return view;
    }

        private void setup(View view) {
        uploadButton = view.findViewById(R.id.submitButton);
        startUpNameHolder = view.findViewById(R.id.startUpNameHolder);
        startUpIdeaHolder = view.findViewById(R.id.startUpIdeaHolder);
        startupSkillsHolder = view.findViewById(R.id.startUpSkillsHolder);
        startUpFounderHolder = view.findViewById(R.id.startUpFounderHolder);
        startUpLocationHolder = view.findViewById(R.id.startUpLocationHolder);
        startUpContactHolder = view.findViewById(R.id.startUpContactHolder);
    }

}
