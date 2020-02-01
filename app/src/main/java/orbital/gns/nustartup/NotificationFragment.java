package orbital.gns.nustartup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid;

    private TextView panel;
    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        panel = view.findViewById(R.id.panel);

        return view;
    }

    ArrayList<NotiModel> notiModels;
    ListView listView;
    private NotiAdapter notiAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.list);
        notiModels = new ArrayList<>();
        //notiModels.add(new NotiModel("Hello", "Meow meow", true));
        notiAdapter= new NotiAdapter(notiModels, getContext());
        listView.setAdapter(notiAdapter);

        FirebaseDatabase.getInstance().getReference("/users/" + uid + "/notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    panel.setText("No New Notifications");
                    return;
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String messageUser = ds.getKey();
                    FirebaseDatabase.getInstance().getReference("/users/" + uid + "/notifications/" + messageUser)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot second) {
                                    String message = second.getValue(String.class);

                                    if(message.contains("would like to collaborate with you in")){
                                        String personName = message.split("would")[0];
                                        String companyName = message.split("in")[1];
                                        notiModels.add(new NotiModel(personName, companyName, true, messageUser));
                                    } else  if(message.contains("has accepted to collaborate with you in")){
                                        String personName = message.split("has")[0];
                                        String companyName = message.split("in")[1];
                                        notiModels.add(new NotiModel(personName, companyName, false, messageUser));
                                    }
                                    notiAdapter= new NotiAdapter(notiModels, getContext());
                                    listView.setAdapter(notiAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //thing to do when button is pressed
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                AcceptReject acceptReject = new AcceptReject();
                Bundle args = new Bundle();

                String mName = notiModels.get(position).getCompanyName();
                String personName = notiModels.get(position).getPersonName();
                args.putString("Name", mName.trim());
                args.putString("otherPerson", personName.trim());
                args.putString("UID", uid);
                args.putString("messageUser", notiModels.get(position).getMessageUser());
                acceptReject.setArguments(args);
                transaction.replace(R.id.container, acceptReject);

                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }
}
