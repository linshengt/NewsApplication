package com.linshengt.newsapplication.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.linshengt.newsapplication.Fragment.NewsContent;

import java.util.List;

/**
 * Created by linshengt on 2016/10/29.
 */
public class NewsAdapter extends FragmentPagerAdapter {

    private List<String[]> list;
    public NewsAdapter(FragmentManager fm, List<String[]> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsContent.newInstance(list.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position)[0];
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
