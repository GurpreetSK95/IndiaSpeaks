package gurpreetsk.me.indiaspeaks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import gurpreetsk.me.indiaspeaks.Models.MLAclass;
import gurpreetsk.me.indiaspeaks.R;
import gurpreetsk.me.indiaspeaks.WebViewActivity;

/**
 * Created by Gurpreet on 18/08/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<String> complaint;
    private ArrayList<Integer> image;
    private ArrayList<String> mlaInfoLink;
    private ArrayList<String> pincode;
    private ArrayList<String> name;
    private ArrayList<String> email;
    private ArrayList<String> link;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefData = database.getReference("MLAs");


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView2;
        public TextView mTextView2;
        public ImageView imageView;
        private TextView mlaLink, mlaEmail;
        private TextView txtname;

        public MyViewHolder(View v) {
            super(v);
            mCardView2 = (CardView) v.findViewById(R.id.card_view2);
            mTextView2 = (TextView) v.findViewById(R.id.tv_text2);
            imageView = (ImageView) v.findViewById(R.id.imagebmp);
            mlaEmail = (TextView) v.findViewById(R.id.mlaEmailTV);
            mlaLink = (TextView) v.findViewById(R.id.mlaLinkTV);
            txtname = (TextView) v.findViewById(R.id.mlaNameTV);
        }

        public void mlaLinkClicked(View view){

        }

        public void mlaEmailClicked(View view){

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> complaint,ArrayList<Integer> image, ArrayList<String> pincode,
                     ArrayList<String> name, ArrayList<String> email, ArrayList<String> link) {
        this.complaint = complaint;
        this.image = image;
        this.pincode = pincode;
        this.name = name;
        this.email = email;
        this.link = link;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.mTextView2.setText(complaint.get(position));
            holder.txtname.setText(name.get(position));
            final String complaintpos = complaint.get(position);
            final String pin = pincode.get(position);
            final String emailpos = email.get(position);
            final String namepos = link.get(position);
            holder.mlaEmail.setText(email.get(position));
            holder.mlaEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {;
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("vnd.android.cursor.item/email");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"1"+emailpos});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sent via IndiaSpeaks");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, complaintpos);
                    view.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                }
            });

            holder.mlaLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                    intent.putExtra("MinisterName", namepos);
                    view.getContext().startActivity(intent);
                }
            });

           holder.imageView.setImageResource(image.get(position));
            setScaleAnimation(holder.itemView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return complaint.size();
    }


    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }
}