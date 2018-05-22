package co.inanis.nearbyplaces.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.inanis.nearbyplaces.R;
import co.inanis.nearbyplaces.adapters.PlaceListAdapter;
import co.inanis.nearbyplaces.model.Place;
import co.inanis.nearbyplaces.model.PlaceViewModel;

public class PlaceListFragment extends Fragment {

    @BindView(R.id.place_list)
    ListView mPlaceListView;
    private PlaceListAdapter mListAdapter;

    public PlaceListFragment() {
        // Required empty public constructor
    }

    public static PlaceListFragment newInstance() {
        return new PlaceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place_list, container, false);
        ButterKnife.bind(this, rootView);

        mListAdapter = new PlaceListAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        mPlaceListView.setAdapter(mListAdapter);
        mPlaceListView.setOnItemClickListener(this::onPlaceClicked);

        return rootView;
    }

    private void onPlaceClicked(AdapterView<?> adapterView, View view, int position, long id) {
        Place place = (Place) view.getTag();
        Uri gmmIntentUri = Uri.parse(getString(R.string.maps_uri_template, place.getName(), place.getPlaceId()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        PlaceViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PlaceViewModel.class);
        model.getPlaces().observe(this, this::updateUI);
    }

    private void updateUI(List<Place> places) {
        mListAdapter.clear();
        mListAdapter.addAll(places);
        mListAdapter.notifyDataSetChanged();
    }
}
