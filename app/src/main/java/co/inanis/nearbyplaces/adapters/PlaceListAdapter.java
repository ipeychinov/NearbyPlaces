package co.inanis.nearbyplaces.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.inanis.nearbyplaces.R;
import co.inanis.nearbyplaces.model.Place;

public class PlaceListAdapter extends ArrayAdapter<Place> {
    private List<Place> mPlaces;

    public PlaceListAdapter(@NonNull Context context, @NonNull List<Place> objects) {
        super(context, R.layout.item_place, objects);
        mPlaces = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_place, null);
        }

        Place place = mPlaces.get(position);

        TextView name = view.findViewById(R.id.name);
        name.setText(place.getName());

        TextView distance = view.findViewById(R.id.distance);
        distance.setText(getContext().getString(R.string.distance_template, place.getDistance()));

        view.setTag(place);

        return view;
    }
}
