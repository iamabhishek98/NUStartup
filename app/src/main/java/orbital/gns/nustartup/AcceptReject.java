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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptReject extends Fragment {

    private String uid;
    private String messageUser;
    private String otherPerson;
    public AcceptReject() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accept_reject, container, false);
    }

    String otherPersonUID;
    String mName;
    String UUID ="";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = "";
        Bundle bundle = this.getArguments();
        if(bundle != null){
            mName = bundle.getString("Name");
            ((TextView)getActivity().findViewById(R.id.infoStartUpNameHolder)).setText(mName);
            uid = bundle.getString("UID");
            messageUser = bundle.getString("mesageUser");
            otherPerson = bundle.getString("otherPerson");
        }

        getActivity().findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("/users/").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference("/users/" + ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dSnapshot) {
                                            String name = dSnapshot.getValue(User.class).name;
                                            Log.d("Debug", otherPerson + "1");
                                            if (name == null) {
                                                Log.d("Debug", "SELECTED EMPTY");
                                            } else if (name.equals(otherPerson)) {
                                                Log.d("Debug", name);
                                                otherPersonUID = ds.getKey();
                                                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("Debug", "Here " + name);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    } );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );

                FirebaseDatabase.getInstance().getReference("/posts/").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference("/posts/" + ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dSnapshot) {
                                            String name = dSnapshot.getValue(DataModel.class).dataModelUid;
                                            Log.d("Debug", otherPerson + "1");
                                            if (name == null) {
                                                Log.d("Debug", "SELECTED EMPTY");
                                            } else if (name.equals(mName)) {
                                                Log.d("Debug", name);
                                                UUID = ds.getKey();
                                                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("Debug", "Here " + name);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    } );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
                FirebaseDatabase.getInstance().getReference("/users/" + otherPersonUID + "/organisations/" + UUID).setValue(UUID);
                deleteRequest(messageUser);
            }
        });

        getActivity().findViewById(R.id.logoutButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void deleteRequest(final String mUser) {
        FirebaseDatabase.getInstance().getReference("/users/" + uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User curr = dataSnapshot.getValue(User.class);
                        curr.notifications.remove(mUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}
