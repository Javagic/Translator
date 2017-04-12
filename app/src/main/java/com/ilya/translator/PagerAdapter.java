package com.ilya.translator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 10.04.17 20:47.
 */
public class PagerAdapter extends FragmentPagerAdapter {

  private final Fragment[] fragments;

  public PagerAdapter(FragmentManager fragmentManager, Fragment[] fragments) {
    super(fragmentManager);
    this.fragments = fragments;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return position == 0 ? "История" : "Избранное";
  }

  @Override
  public Fragment getItem(int position) {
    return fragments[position];
  }

  @Override
  public int getCount() {
    return fragments.length;
  }
}
