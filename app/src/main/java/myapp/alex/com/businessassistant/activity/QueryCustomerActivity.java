package myapp.alex.com.businessassistant.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.QueryCustomerAdapter;
import myapp.alex.com.businessassistant.fragment.CustomerSettingFragment;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class QueryCustomerActivity extends AppCompatActivity implements CustomerSettingFragment.CustomerSettingInputListener {


    RecyclerView rv_query;
    QueryCustomerAdapter adapter;
    //数据库
    private DbUtils db;
    List<CustomerModel> list = new ArrayList<>();
    //初始条件
    String name = "";
    String source="";
    int position=0;
    View customView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getResources().getStringArray(R.array.items_txt)[2]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_query_customer);
        //初始化数据
        initData();
        //初始化布局
        initView();
    }

    private void initData() {
        db = MyDbUtils.getInstance().Db(this);
    }

    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[8]);
        rv_query = (RecyclerView) findViewById(R.id.rv_customer);
        list.clear();
        adapter=new QueryCustomerAdapter(this,list,getFragmentManager());
        rv_query.setLayoutManager(new LinearLayoutManager(this));
        rv_query.setAdapter(adapter);
        rv_query.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    //设置查询条件
    public void OnCustomerSetting(View view) {
        CustomerSettingFragment dialog = CustomerSettingFragment.newInstance(name,position);
        dialog.show(getFragmentManager(), "tag");
    }

    //处理查询
    public void OnCustomerQuery(View view) {
        QueryDate();
        ShowView();

    }



    private void QueryDate() {
        //查询数据
        switch (position){
            case 0:
                source="";
                break;
            case 1:
                source="0";
                break;
            case 2:
                source="1";
                break;
        }
        if (source.equals("")){
            if (name.equals("")){
                //客户名称为空，数据来源为空
                list=db.findAll(Selector.from(CustomerModel.class));
            }else{
                //客户名称不为空，数据来源为空
                list=db.findAll(Selector.from(CustomerModel.class).where("C_Name","=",name));
            }
        }else{
            if (name.equals("")){
                //客户名称为空，数据来源不为空
                list=db.findAll(Selector.from(CustomerModel.class).where("C_Source","=",source));
            }else{
                //客户名称不为空，数据来源不为空
                list=db.findAll(Selector.from(CustomerModel.class).where("C_Name","=",name).and("C_Source","=",source));
            }
        }
    }
    private void ShowView() {
        //更新界面
        if(list==null||list.size()<1){
            FuncUtils.showToast(this, "没有满足所选查询条件下的客户信息，请重新设置查询条件!");
            if (list==null){
                list=new ArrayList<>();
            }
            list.clear();
            adapter=new QueryCustomerAdapter(this,list,getFragmentManager());
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);
        }else {
            adapter=new QueryCustomerAdapter(this,list,getFragmentManager());
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);
        }
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

    @Override
    public void onCustomersSettingInputComplete(String input_name, int input_position) {
        name=input_name;
        position=input_position;
    }
}
