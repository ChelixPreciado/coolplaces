package interware.coolapp.ui.PlacesList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import interware.coolapp.R;
import interware.coolapp.adapters.PlacesAdapter;
import interware.coolapp.models.Place;
import interware.coolapp.ui.LogIn.LoginActivity;
import interware.coolapp.ui.PlaceDetail.PlaceDetailActivity;
import interware.coolapp.utils.LoaderUtils;

public class PlacesListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {

    private FirebaseAuth mAuth;
    private TextView txtUserMail;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDataBase;
    private LoaderUtils loaderUtils;

    private RecyclerView rvPlaces;
    private PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvPlaces = (RecyclerView)findViewById(R.id.rv_places);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlaces.setLayoutManager(mLayoutManager);
        rvPlaces.setItemAnimator(new DefaultItemAnimator());

        getLoaderUtils().showLoader(true);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.places_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtUserMail = (TextView)drawer.findViewById(R.id.txt_user_mail);
        if (firebaseUser.getEmail()!=null)
            txtUserMail.setText(firebaseUser.getEmail());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        placesAdapter = new PlacesAdapter(getApplicationContext(), getPlaces(dataSnapshot));
        placesAdapter.setListener(new PlacesAdapter.PlacesAdapterListener() {
            @Override
            public void onPlaceClick(Place place) {
                Log.i("Chelix", "Place selected: " + place.getLugar());
                Intent intent = new Intent(PlacesListActivity.this, PlaceDetailActivity.class);
                intent.putExtra("selectedPlace", place);
                startActivity(intent);
            }
        });
        rvPlaces.setAdapter(placesAdapter);
        getLoaderUtils().showLoader(false);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("Chelix","The read failed: " + databaseError.getMessage());
        getLoaderUtils().showLoader(false);
    }

    public ArrayList<Place> getPlaces(DataSnapshot dataSnapshot){
        Log.i("Chelix", "Snapchot: " + dataSnapshot.getValue());
        DataSnapshot placesSnapshot = dataSnapshot.child("places");
        GenericTypeIndicator<ArrayList<Place>> t = new GenericTypeIndicator<ArrayList<Place>>() {
            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        };
        ArrayList<Place> places = placesSnapshot.getValue(t);
        return places;
    }

    private LoaderUtils getLoaderUtils(){
        if (loaderUtils==null)
            loaderUtils = new LoaderUtils(this);
        return loaderUtils;
    }
}
