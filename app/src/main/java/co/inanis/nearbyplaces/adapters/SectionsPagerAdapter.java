package co.inanis.nearbyplaces.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.inanis.nearbyplaces.fragments.MapFragment;
import co.inanis.nearbyplaces.fragments.PlaceListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int PLACE_LIST_FRAGMENT_POSITION = 0;
    private static final int MAP_FRAGMENT_POSITION = 1;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PLACE_LIST_FRAGMENT_POSITION:
                return PlaceListFragment.newInstance();
            case MAP_FRAGMENT_POSITION:
                return MapFragment.newInstance();
            default:
                return PlaceListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
