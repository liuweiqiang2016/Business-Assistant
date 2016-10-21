package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zeone.framework.db.sqlite.DbUtils;

import java.util.List;

import myapp.alex.com.businessassistant.fragment.CostFragment;
import myapp.alex.com.businessassistant.fragment.ServiceFragment;
import myapp.alex.com.businessassistant.model.ServiceModel;

/**
 * Created by liuweiqiang on 2016/10/21.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    DbUtils mdb;
    ServiceFragment serviceFragment;
    CostFragment costFragment;
    private int mChildCount = 0;

    public SectionsPagerAdapter(FragmentManager fm, Context mContext,DbUtils db) {
        super(fm);
        this.mContext = mContext;
        this.mdb=db;
    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if (position==0){
            serviceFragment=ServiceFragment.newInstance(mContext,mdb);
            return serviceFragment;
        }
        if (position==1){
            costFragment=CostFragment.newInstance(mContext,mdb);
            return costFragment;

        }
        return new Fragment();

    }

    @Override
    public int getItemPosition(Object object) {

        if (mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
        }
        return null;
    }

    //弹出增加dialog
    public void AddService(){
        if (serviceFragment!=null){
            serviceFragment.AddService();
        }
    }
    public void setEditPriceComplete(String edit_price, int position){
        if (serviceFragment!=null){
            serviceFragment.setEditPriceComplete(edit_price,position);
        }
    }

    public void setDeleteServiceComplete(int position){
        if (serviceFragment!=null){
            serviceFragment.setDeleteServiceComplete(position);
        }
    }
    public void setAddServiceComplete(String name, String price, String c_id){
        if (serviceFragment!=null){
            serviceFragment.setAddServiceInputComplete(name,price,c_id);
        }
    }


    public void AddCost(){
        if (costFragment!=null){
            costFragment.AddCost();
        }
    }

    public void setDeleteCostComplete(int position){
        if (costFragment!=null){
            costFragment.setDeleteCostComplete(position);
        }
    }
    public void setAddCostComplete(String name,String c_id){
        if (costFragment!=null){
            costFragment.setAddCostComplete(name,c_id);
        }
    }


}
