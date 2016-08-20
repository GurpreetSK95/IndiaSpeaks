package gurpreetsk.me.indiaspeaks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gurpreetsk.me.indiaspeaks.Adapters.MyAdapter;
import gurpreetsk.me.indiaspeaks.Models.Feed;

public class FeedActivity extends AppCompatActivity{

    private static final String TAG = "FeedActivity";
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<String> complaint = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> pincode = new ArrayList<>();
    ArrayList<String> link = new ArrayList<>();
    ArrayList<Integer> image = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Feed");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FeedActivity.this, MainActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyAdapter(complaint, image, pincode, name, email, link);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(FeedActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        complaint.add("Unsafe road");
        complaint.add("Wire problem");
        complaint.add("Water water everywher");
        complaint.add("Ditch");
        complaint.add("Problem");
        complaint.add("Flowing sewage");
        complaint.add("No maintainance of roads");
        complaint.add("Water flow not controlable");
        image.add(getResources().getIdentifier("a1", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a2", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a3", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a4", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a5", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a6", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a7", "raw", this.getPackageName()));
        image.add(getResources().getIdentifier("a8", "raw", this.getPackageName()));
        pincode.add("110075");
        pincode.add("110062");
        pincode.add("110042");
        pincode.add("110009");
        pincode.add("110006");
        pincode.add("110020");
        pincode.add("110031");
        pincode.add("110001");
        name.add("Sh. Adarsh Shastri");
        name.add("Sh. Ajay Dutt");
        name.add("Sh. Ajesh Yadav");
        name.add("Sh. Akhilesh Pati Tripathi");
        name.add("Ms. Alka Lamba");
        name.add("Sh. Amanatullah Khan");
        name.add("Sh. Anil Kumar Bajpai");
        name.add("Sh. Arvind Kejriwal");
        email.add("mla.dwarka-dl@gov.in");
        email.add("mla.ambedkarngr-dl@gov.in");
        email.add("mla.badli-dl@gov.in");
        email.add("mla.modeltown-dl@gov.in");
        email.add("mla.chchowk-dl@gov.in");
        email.add("mla.okhla-dl@gov.in");
        email.add("mla.gandhinagar-dl@gov.in");
        email.add("mla.newdelhi-dl@gov.in");
        link.add("AdarshShastri");
        link.add("AjayDutt");
        link.add("AjeshYadav");
        link.add("AkhileshPatiTripathi");
        link.add("AlkaLamba");
        link.add("AmanatullahKhan");
        link.add("AnilKumarBajpai");
        link.add("ArvindKejriwal");
        adapter.notifyDataSetChanged();
        prepareData();
    }



    private void prepareData() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class);
                    complaint.add(feed.getComplaint());
                    String imageget = feed.getImage();
                    image.add(getResources().getIdentifier(imageget, "raw", FeedActivity.this.getPackageName()));
                    name.add(feed.getName());
                    email.add(feed.getEmail());
                    link.add(feed.getLink());
                    pincode.add(feed.getPincode());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", databaseError.getDetails());
            }
        });
        System.out.println(complaint);
        System.out.println(image);
    }
}