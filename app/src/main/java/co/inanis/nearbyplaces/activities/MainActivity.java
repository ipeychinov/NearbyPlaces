package co.inanis.nearbyplaces.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.inanis.nearbyplaces.R;
import co.inanis.nearbyplaces.adapters.SectionsPagerAdapter;
import co.inanis.nearbyplaces.model.Place;
import co.inanis.nearbyplaces.util.RequestUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_LOCATION = 1001;

    private static final int SEARCH_RADIUS = 3000;
    private static final String SEARCH_PLACE_TYPE = "bar";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    private RequestUtil mRequestUtil;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mRequestUtil = RequestUtil.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            findLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Let EasyPermissions handle the result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(PERMISSION_LOCATION)
    private void findLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, this::onLocationFound);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), PERMISSION_LOCATION, perms);
        }
    }

    private void onLocationFound(Location location) {
        if (location != null) {
            mLastLocation = location;
            requestNearbyPlaces(location.getLatitude(), location.getLongitude());
        } else {
            //TODO check if locations is enabled
        }
    }

    private void requestNearbyPlaces(double lat, double lon) {
        mRequestUtil.addToRequestQueue(RequestUtil.buildNearbyPlacesRequest(lat, lon, SEARCH_RADIUS, SEARCH_PLACE_TYPE, this::onResponse, this::onErrorResponse));
    }

    private void onResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.has("results")) {
                List<Place> places = deserializeResponse(jsonObject.getJSONArray("results"));
                //TODO display them in a list
            } else {
                Toast.makeText(this, "No nearby places found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to get nearby places.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(this, volleyError.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    private List<Place> deserializeResponse(JSONArray results) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Place.class, new Place.PlaceDeserializer());

        Gson gson = builder.create();
        Type placeListType = new TypeToken<List<Place>>() {
        }.getType();

        return gson.fromJson(results.toString(), placeListType);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
