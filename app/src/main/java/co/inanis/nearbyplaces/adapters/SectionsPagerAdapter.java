package co.inanis.nearbyplaces.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.inanis.nearbyplaces.activities.MainActivity;
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
                return MainActivity.PlaceholderFragment.newInstance(position + 1);
            default:
                return MainActivity.PlaceholderFragment.newInstance(-1);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
