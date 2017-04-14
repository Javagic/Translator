package com.ilya.translator.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.translator.R;
import com.ilya.translator.databinding.FBookmarkBinding;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 20:05.
 */
public class BookmarkFragment extends Fragment {
  FBookmarkBinding binding;
  ViewPager viewPager;
  public static BookmarkFragment newInstance() {
    BookmarkFragment fragment = new BookmarkFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    binding = DataBindingUtil.inflate(inflater, R.layout.f_bookmark, container, false);
    viewPager = binding.viewPager;
    TabLayout tabLayout = binding.tabLayout;
    Fragment[] fragments = {
        Fragment.instantiate(getActivity(), HistoryFragment.class.getName()),
        Fragment.instantiate(getActivity(), FavoritesFragment.class.getName())
    };
    PagerAdapter pagerAdapter = new com.ilya.translator.PagerAdapter(getChildFragmentManager(),fragments);
    viewPager.setAdapter(pagerAdapter);


    tabLayout.setupWithViewPager(viewPager);
    return binding.getRoot();
  }
}
