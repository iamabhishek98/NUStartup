package orbital.gns.nustartup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import orbital.gns.nustartup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartUpInfo extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;
    private User myProfile;

    private Button requestButton;
    public StartUpInfo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        FirebaseDatabase.getInstance().getReference("/users/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myProfile = new User(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        View view = inflater.inflate(R.layout.fragment_start_up_info, container, false);
        requestButton = view.findViewById(R.id.infoStartUpJoinRequest);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            final String mName = bundle.getString("Name");
            final String mDescription = bundle.getString("Description");
            final String mSkills = bundle.getString("Skills");
            final String mLocation = bundle.getString("Location");
            final String mContact = bundle.getString("Contact");
            final String mOwnerUid = bundle.getString("OwnerUid");
            final String mDataModelUid = bundle.getString("DataModelUid");

            ((TextView)getActivity().findViewById(R.id.infoStartUpNameHolder)).setText(mName);
            ((TextView)getActivity().findViewById(R.id.infoStartUpIdeaHolder)).setText(mDescription);
            ((TextView)getActivity().findViewById(R.id.infoStartUpSkillsHolder)).setText(mSkills);
            ((TextView)getActivity().findViewById(R.id.infoStartUpFounderHolder)).setText(mContact);
            ((TextView)getActivity().findViewById(R.id.infoStartUpLocationHolder)).setText(mLocation);

            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("/users/" + mOwnerUid + "/notifications/" + uid).setValue(myProfile.name + " would like to collaborate with you in " + mName);
                    Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_SHORT).show();
                }
            });

            Log.d("Samuel-Samuel", mName + " is retrieved");
        }


    }
}
