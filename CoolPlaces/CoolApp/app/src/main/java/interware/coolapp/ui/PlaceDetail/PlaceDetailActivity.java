package interware.coolapp.ui.PlaceDetail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import interware.coolapp.R;
import interware.coolapp.models.Place;

public class PlaceDetailActivity extends AppCompatActivity {

    private ImageView ivTopImage;

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivTopImage = (ImageView)findViewById(R.id.ivBigImage);

        place = getIntent().getParcelableExtra("selectedPlace");
        Picasso.with(getApplicationContext()).load(place.getImagen()).into(ivTopImage);
    }
}
