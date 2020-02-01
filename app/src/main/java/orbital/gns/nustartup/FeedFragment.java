package orbital.gns.nustartup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
//    private OnFragmentInteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    ArrayList<DataModel> dataModels;
    ListView listView;
    SearchView searchView;
    private CustomAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.list);
        searchView = getView().findViewById(R.id.searchView);
        Log.d("Debug", "yo");
        dataModels = new ArrayList<>();

        retrieveData();
        Log.d("Debug",dataModels.size() + " ");
        adapter= new CustomAdapter(dataModels, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                StartUpInfo startUpInfo = new StartUpInfo();
                Bundle args = new Bundle();
                //String mStartUpName = ((TextView)view.findViewById(R.id.startUpName)).getText().toString();
                String mName = dataModels.get(position).getName();
                String mDescription = dataModels.get(position).getDescription();
                String mSkills = dataModels.get(position).skills;
                String mLocation = dataModels.get(position).getLocation();
                String mContact = dataModels.get(position).getContact();
                String mOwnerUid = dataModels.get(position).ownerUid;
                String mDataModelUid = dataModels.get(position).dataModelUid;
                //Log.d("SamuelSamuel", );
                args.putString("Name", mName);
                args.putString("Description", mDescription);
                args.putString("Skills", mSkills);
                args.putString("Location", mLocation);
                args.putString("Contact", mContact);
                args.putString("OwnerUid", mOwnerUid);
                args.putString("DataModelUid", mDataModelUid);

                startUpInfo.setArguments(args);
                transaction.replace(R.id.container, startUpInfo);

                transaction.addToBackStack(null);
                transaction.commit();
//
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<DataModel> searchedDataModels = new ArrayList<>();

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).name.toLowerCase().contains(s.toLowerCase())) {
                        searchedDataModels.add(adapter.getItem(i));
                    }
                }

                for (int i = 0; i < searchedDataModels.size(); i++) {
                    System.out.println(searchedDataModels.get(i).name);
                }

                CustomAdapter tempAdapter = new CustomAdapter(searchedDataModels, getContext());
                listView.setAdapter(tempAdapter);

                return false;
            }
        });
    }

    private void retrieveData() {
        FirebaseDatabase.getInstance().getReference("/posts/").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot first : dataSnapshot.getChildren()) {
                    if (first != null) {
                        String firstkey = first.getKey();
                        Log.d("Debug", firstkey);
                        FirebaseDatabase.getInstance().getReference("/posts/" + firstkey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot p0) {
//                                            Log.d("Debug", "Hello");
                                DataModel dm = p0.getValue(DataModel.class);
                                Log.d("Debug", dm.name);
                                dataModels.add(new DataModel(dm.name, dm.getDescription(), dm.skills, dm.getFounder(), dm.getFounder(), dm.getLocation(),dm.ownerUid, dm.dataModelUid));
                                CustomAdapter tempAdapter = new CustomAdapter(dataModels, getContext());
                                listView.setAdapter(tempAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
