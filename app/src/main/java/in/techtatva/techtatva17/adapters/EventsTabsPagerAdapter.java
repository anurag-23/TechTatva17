package in.techtatva.techtatva17.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.techtatva.techtatva17.fragments.DaysFragment;

/**
 * Created by skvrahul on 30/5/17.
 */

public class EventsTabsPagerAdapter extends FragmentStatePagerAdapter {
    String tabTitles[] = new String[]{"Day 1", "Day 2", "Day 3", "Day 4"};
    String searchTerm;
    public EventsTabsPagerAdapter(FragmentManager fm,String searchTerm){
        super(fm);
        this.searchTerm=searchTerm;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {
        return DaysFragment.newInstance(position,searchTerm);
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
