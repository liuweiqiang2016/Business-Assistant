package myapp.alex.com.businessassistant.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.SectionsPagerAdapter;
import myapp.alex.com.businessassistant.fragment.AddCostFragment;
import myapp.alex.com.businessassistant.fragment.AddServiceFragment;
import myapp.alex.com.businessassistant.fragment.CostFragment;
import myapp.alex.com.businessassistant.fragment.DeleteCostFragment;
import myapp.alex.com.businessassistant.fragment.DeleteServiceFragment;
import myapp.alex.com.businessassistant.fragment.EditServiceFragment;
import myapp.alex.com.businessassistant.fragment.ServiceFragment;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class EditProjectActivity extends AppCompatActivity implements EditServiceFragment.EditOrderListener,ServiceFragment.ServiceDoneListener,DeleteServiceFragment.DeleteServiceListener,AddServiceFragment.AddServiceListener,DeleteCostFragment.DeleteCostListener,AddCostFragment.AddCostListener,CostFragment.CostDoneListener{

    View customView;
    ImageButton ib;
    TextView title;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    //数据库
    private DbUtils db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_edit_project);

        //初始化数据
        initData();
        //初始化布局
        initView();
        //绑定事件
        initEvent();
    }

    private void initData() {
        db = MyDbUtils.getInstance().Db(this);
    }

    private void initView() {
        ib= (ImageButton) customView.findViewById(R.id.action_bar_ib);
        ib.setVisibility(View.VISIBLE);
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[3]);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this,db);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void initEvent() {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos=mViewPager.getCurrentItem();
                if (pos==0){
                    //通知服务fragment弹出dialog
                    mSectionsPagerAdapter.AddService();
                }
                if (pos==1){
                    mSectionsPagerAdapter.AddCost();

                }

            }
        });
    }
    //返回主界面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //返回主界面逻辑处理
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    //编辑一个服务
    @Override
    public void onEditPriceComplete(String edit_price, int position) {
        mSectionsPagerAdapter.setEditPriceComplete(edit_price,position);
    }
    //删除一个服务
    @Override
    public void onDeleteServiceComplete(int position) {
        mSectionsPagerAdapter.setDeleteServiceComplete(position);
    }

    //增加一个服务
    @Override
    public void onAddServiceInputComplete(String name, String price, String c_id) {
        mSectionsPagerAdapter.setAddServiceComplete(name,price,c_id);
    }
    //服务fragment操作完成后，更新viewpager
    @Override
    public void onServiceDone() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    //删除一个开销
    @Override
    public void onDeleteCostComplete(int position) {
        mSectionsPagerAdapter.setDeleteCostComplete(position);

    }

    //新增一条开销
    @Override
    public void onAddCostComplete(String name, String c_id) {
        mSectionsPagerAdapter.setAddCostComplete(name,c_id);
    }

    //开销fragment完成，更新viewpager
    @Override
    public void onCostDone() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
}
