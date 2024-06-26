package com.genius.employee.adapter;

/*
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
*/

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.genius.employee.activity.ProfileActivity;
import com.genius.employee.fragment.ContactFragment;
import com.genius.employee.fragment.EducationFragment;
import com.genius.employee.fragment.FamilyFragment;
import com.genius.employee.fragment.OfficialFragment;
import com.genius.employee.fragment.PersonalFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 5;

    private String tabTitles[] = new String[]{"Official", "Personal", "Contact","Family","Education"
    };

    public ProfileViewPagerAdapter(FragmentManager fm, ProfileActivity profileActivity) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:

                return new OfficialFragment();


            case 1:

                return new PersonalFragment();


            case 2:

                return new ContactFragment();

            case 3:

                return new FamilyFragment();
            case 4:

                return new EducationFragment();


        }

        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];

    }

}
