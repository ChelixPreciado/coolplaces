package interware.coolapp.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import interware.coolapp.R;

/**
 * Created by chelixpreciado on 7/15/16.
 */
public class PlaceItemHolder extends RecyclerView.ViewHolder {

    private ImageView ivPlace;
    private TextView txtPlaceName;
    private TextView txtPlaceState;
    private ViewGroup vgRoot;

    public PlaceItemHolder(View itemView) {
        super(itemView);
    }

    public ImageView getIvPlace() {
        if (ivPlace==null)
            ivPlace = (ImageView)itemView.findViewById(R.id.iv_place_thumb);
        return ivPlace;
    }

    public TextView getTxtPlaceName() {
        if (txtPlaceName==null)
            txtPlaceName = (TextView)itemView.findViewById(R.id.txt_place_name);
        return txtPlaceName;
    }

    public TextView getTxtPlaceState() {
        if (txtPlaceState==null)
            txtPlaceState = (TextView)itemView.findViewById(R.id.txt_place_state);
        return txtPlaceState;
    }

    public ViewGroup getVgRoot() {
        if (vgRoot==null)
            vgRoot = (ViewGroup)itemView.findViewById(R.id.view_root);
        return vgRoot;
    }
}
