package interware.coolapp.ui.PlaceDetail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interware.coolapp.R;
import interware.coolapp.models.Comment;
import interware.coolapp.models.Place;
import interware.coolapp.utils.LoaderUtils;
import interware.coolapp.utils.MapBoxUtil;

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener,
        AddCommentFragmentDialog.AddCommentFragmentDialogListener, ValueEventListener {

    private ImageView ivTopImage, ivMap;
    private TextView txtPlaceName, txtPlaceState, txtPlaceDescr;
    private ViewGroup btnComment, btnSeeComments;

    private Place place;
    private LoaderUtils loaderUtils;
    private ArrayList<Comment> comments;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        ivTopImage = (ImageView)findViewById(R.id.ivBigImage);
        ivMap = (ImageView)findViewById(R.id.iv_map);
        txtPlaceName = (TextView)findViewById(R.id.txt_place_name);
        txtPlaceState = (TextView)findViewById(R.id.txt_place_state);
        txtPlaceDescr = (TextView)findViewById(R.id.txt_place_descr);
        btnComment = (ViewGroup)findViewById(R.id.btn_comment);
        btnSeeComments = (ViewGroup)findViewById(R.id.btn_see_comments);

        place = getIntent().getParcelableExtra("selectedPlace");
        Picasso.with(getApplicationContext()).load(place.getImagen()).into(ivTopImage);
        txtPlaceName.setText(place.getLugar());
        txtPlaceState.setText(place.getEstado());
        txtPlaceDescr.setText(place.getDescripcion());
        btnComment.setOnClickListener(this);
        btnSeeComments.setOnClickListener(this);

        String mapUrl = "https://api.mapbox.com/v4/mapbox.emerald/pin-m-heart+FF0000" +
                "(" + place.getLon() + "," + place.getLat() + ")/" + place.getLon() + "," + place.getLat() +
                ",12/500x250.png?access_token=" + MapBoxUtil.mapBoxToken;

        Picasso.with(getApplicationContext()).load(mapUrl).into(ivMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDataBase.addValueEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataBase.removeEventListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comment:
                showCommentDialog();
                break;
            case R.id.btn_see_comments:
                showCommentsListDialog();
                break;
        }
    }


    @Override
    public void onCommented(String comment) {
        postComment(comment);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.i("Chelix", "Data have changed");
        DataSnapshot commentsSnapShot = dataSnapshot.child("places").child(String.valueOf(place.getId()-1)).child("comments");
        Map<String, Comment> mapComments = (HashMap<String, Comment>)commentsSnapShot.getValue();
        comments = getComments((HashMap) mapComments);
        getLoaderUtils().showLoader(false);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("Chelix","The read failed: " + databaseError.getMessage());
        getLoaderUtils().showLoader(false);
    }

    private void showCommentDialog(){
        AddCommentFragmentDialog dialog = AddCommentFragmentDialog.newInstance(place.getLugar());
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "addComment");
    }

    private void showCommentsListDialog(){
        ShowCommentsFragment dialog = ShowCommentsFragment.newInstance(comments);
        dialog.show(getFragmentManager(), "showComments");
    }

    private void postComment(String comment){
        getLoaderUtils().showLoader(true);
        Log.i("Chelix","posteando comentario: " + comment);
        Comment c = new Comment(firebaseUser.getUid(), firebaseUser.getEmail(), comment);
        String key = mDataBase.child("places").child(String.valueOf(place.getId()-1)).child("comments").push().getKey();
        Map<String, Object> postComment = c.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/places/" + (place.getId()-1) + "/comments/" + key, postComment);
        mDataBase.updateChildren(childUpdates);
    }

    private LoaderUtils getLoaderUtils(){
        if (loaderUtils==null)
            loaderUtils = new LoaderUtils(this);
        return loaderUtils;
    }

    private ArrayList<Comment> getComments(HashMap mapComments){
        ArrayList<Comment> cmts = new ArrayList<Comment>();
        Iterator it = mapComments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            cmts.add(convertComment((HashMap) pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return cmts;
    }

    private Comment convertComment(HashMap commentMap){
        Comment comment = new Comment();
        Iterator it = commentMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            switch (String.valueOf(pair.getKey())){
                case "uid":
                    comment.setUid(String.valueOf(pair.getValue()));
                    break;
                case "user":
                    comment.setUser(String.valueOf(pair.getValue()));
                    break;
                case "text":
                    comment.setText(String.valueOf(pair.getValue()));
                    break;
            }
            it.remove();
        }
        return comment;
    }
}
