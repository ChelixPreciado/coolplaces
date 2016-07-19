package interware.coolapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import interware.coolapp.R;
import interware.coolapp.ViewHolders.PlaceItemHolder;
import interware.coolapp.models.Place;

/**
 * Created by chelixpreciado on 7/15/16.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlaceItemHolder> {

    private Context context;
    private ArrayList<Place> places;
    private PlacesAdapterListener listener;

    public PlacesAdapter(Context context, ArrayList<Place> places){
        this.context = context;
        this.places = places;
    }

    @Override
    public PlaceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, null);
        return new PlaceItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceItemHolder holder, int position) {
        final Place tPlace = places.get(position);
        holder.getTxtPlaceName().setText(tPlace.getLugar());
        holder.getTxtPlaceState().setText(tPlace.getEstado());
        Picasso.with(context).load(tPlace.getImagen()).into(holder.getIvPlace());

        holder.getVgRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null)
                    listener.onPlaceClick(tPlace);
            }
        });
    }

    public void setListener(PlacesAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public interface PlacesAdapterListener{
        public void onPlaceClick(Place place);
    }
}
