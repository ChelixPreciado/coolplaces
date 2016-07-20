package interware.coolapp.ui.PlaceDetail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import interware.coolapp.R;
import interware.coolapp.firebase.database.DBManager;
import interware.coolapp.models.Comment;
import interware.coolapp.models.Place;
import interware.coolapp.utils.LoaderUtils;
import interware.coolapp.utils.MapBoxUtil;

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener,
        AddCommentFragmentDialog.AddCommentFragmentDialogListener, ValueEventListener {

    private ImageView ivTopImage, ivMap;
    private TextView txtPlaceName, txtPlaceState, txtPlaceDescr;
    private ViewGroup btnComment, btnSeeComments;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    private Place place;
    private LoaderUtils loaderUtils;
    private ArrayList<Comment> comments;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DBManager dbManager;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        dbManager = new DBManager(getApplicationContext());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ivTopImage = (ImageView)findViewById(R.id.ivBigImage);
        ivMap = (ImageView)findViewById(R.id.iv_map);
        txtPlaceName = (TextView)findViewById(R.id.txt_place_name);
        txtPlaceState = (TextView)findViewById(R.id.txt_place_state);
        txtPlaceDescr = (TextView)findViewById(R.id.txt_place_descr);
        btnComment = (ViewGroup)findViewById(R.id.btn_comment);
        btnSeeComments = (ViewGroup)findViewById(R.id.btn_see_comments);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        place = getIntent().getParcelableExtra("selectedPlace");
        Picasso.with(getApplicationContext()).load(place.getImagen()).into(ivTopImage);
        txtPlaceName.setText(place.getLugar());
        txtPlaceState.setText(place.getEstado());
        txtPlaceDescr.setText(place.getDescripcion());
        btnComment.setOnClickListener(this);
        btnSeeComments.setOnClickListener(this);
        collapsingToolbarLayout.setTitle("");

        String mapUrl = "https://api.mapbox.com/v4/mapbox.emerald/pin-m-heart+FF0000" +
                "(" + place.getLon() + "," + place.getLat() + ")/" + place.getLon() + "," + place.getLat() +
                ",12/500x250.png?access_token=" + MapBoxUtil.mapBoxToken;

        Picasso.with(getApplicationContext()).load(mapUrl).into(ivMap);

        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString("PlaceDetail", "Detalle: " + place.getLugar());
        firebaseAnalytics.logEvent("PlaceDetail", analyticsBundle);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(place.getLugar());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbManager.getmDataBase().addValueEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbManager.getmDataBase().removeEventListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_suscribe_notifications) {
            String topic = place.getLugar().replace(" ", "");
            Log.i("Chelix", "Suscrito a topic " + topic);
            FirebaseCrash.log("Suscrito a topic " + topic);
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        if (mapComments!=null)
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
        if (comments == null || comments.size()<1)
            Toast.makeText(getApplicationContext(), "Aun no hay comentarios en este lugar, deja el primero :)", Toast.LENGTH_SHORT).show();
        else{
            ShowCommentsFragment dialog = ShowCommentsFragment.newInstance(comments);
            dialog.show(getFragmentManager(), "showComments");
        }
    }

    private void postComment(String comment){
        getLoaderUtils().showLoader(true);
        Log.i("Chelix","posteando comentario: " + comment);
        Comment c = new Comment(firebaseUser.getUid(), firebaseUser.getEmail(), comment);
        String key = dbManager.getmDataBase().child("places").child(String.valueOf(place.getId()-1)).child("comments").push().getKey();
        Map<String, Object> postComment = c.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/places/" + (place.getId()-1) + "/comments/" + key, postComment);
        dbManager.getmDataBase().updateChildren(childUpdates);
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
