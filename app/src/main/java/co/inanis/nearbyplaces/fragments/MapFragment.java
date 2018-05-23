package co.inanis.nearbyplaces.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.inanis.nearbyplaces.R;
import co.inanis.nearbyplaces.model.Place;
import co.inanis.nearbyplaces.model.PlaceViewModel;

public class MapFragment extends Fragment {

    @BindView(R.id.map_view)
    MapView mMapView;
    private GoogleMap mMap;
    private boolean mMapReady = false;

    private PlaceViewModel mViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PlaceViewModel.class);
        mViewModel.getPlaces().observe(this, this::updateUI);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        MapsInitializer.initialize(Objects.requireNonNull(getContext()).getApplicationContext());

        mMapView.getMapAsync(this::onMapReady);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapReady = true;

        mMap.setOnMarkerClickListener(this::onMarkerClicked);

        updateUI(mViewModel.getPlaces().getValue());
    }

    private void updateUI(List<Place> places) {
        if (mMapReady) {
            Location cameraLoc = mViewModel.getLocation().getValue();
            if (cameraLoc != null) {
                LatLng cameraPos = new LatLng(cameraLoc.getLatitude(), cameraLoc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPos, 14));
            }

            if (places != null && !places.isEmpty()) {
                for (Place place : places) {
                    LatLng pos = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(pos).title(place.getName()));
                    marker.setTag(place);
                }
            }
        }
    }

    private boolean onMarkerClicked(Marker marker) {
        Place place = (Place) marker.getTag();

        if (place != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle(place.getName())
                    .setMessage(getString(R.string.long_distance_template, place.getDistance()));

            builder.create().show();
        }

        return false;
    }
}
