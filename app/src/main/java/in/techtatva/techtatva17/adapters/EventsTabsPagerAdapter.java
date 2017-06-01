package in.techtatva.techtatva17.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.techtatva.techtatva17.fragments.DaysFragment;

/**
 * Created by skvrahul on 30/5/17.
 */

public class EventsTabsPagerAdapter extends FragmentPagerAdapter {
    String tabTitles[] = new String[]{"Day 1", "Day 2", "Day 3", "Day 4"};
    public EventsTabsPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return DaysFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
