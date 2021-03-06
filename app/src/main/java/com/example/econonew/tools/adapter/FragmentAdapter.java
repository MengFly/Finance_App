package com.example.econonew.tools.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.econonew.resource.Constant;

import java.util.List;

/**
 * FragmentAdapter
 *
 * @author agnes 是Fragment的适配器
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> list;
	public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Constant.tabList.get(position);
	}
}
