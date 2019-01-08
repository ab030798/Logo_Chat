package com.example.hp.logo_chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPageAdapter extends FragmentPagerAdapter {
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case  2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       // return super.getPageTitle(position);
        switch (position){
            case 0:
                return "REQUEST";
            case  1:
                return  "CHAT";
            case  2:
                return  "FRIENDS";
            default:
                return  null;
        }
    }
}
