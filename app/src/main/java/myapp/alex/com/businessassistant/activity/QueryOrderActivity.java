package myapp.alex.com.businessassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import myapp.alex.com.businessassistant.adapter.QueryOrderAdapter;
import myapp.alex.com.businessassistant.fragment.QuerySettingFragment;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class QueryOrderActivity extends AppCompatActivity implements QuerySettingFragment.SettingInputListener {


    RecyclerView rv_query;
    QueryOrderAdapter adapter;
    //数据库
    private DbUtils db;
    List<OrderModel> list = new ArrayList<>();
    //初始条件
    String name = "", start = "", end = "";
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
        setContentView(R.layout.activity_queryorder);
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
        title.setText(getResources().getStringArray(R.array.items_txt)[2]);
        rv_query = (RecyclerView) findViewById(R.id.rv_query);
        list.clear();
        adapter=new QueryOrderAdapter(this,list);
        rv_query.setLayoutManager(new LinearLayoutManager(this));
        rv_query.setAdapter(adapter);
        rv_query.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    //设置查询条件
    public void OnSetting(View view) {
        QuerySettingFragment dialog = QuerySettingFragment.newInstance(name,start,end,position);
        dialog.show(getFragmentManager(), "tag");
    }

    //处理查询
    public void OnQuery(View view) {
        if (!FuncUtils.compareDateHMS(start, end)) {
            //开始时间为空
            if (start.equals("")) {
                //结束时间为空
                if (end.equals("")) {
                    //客户名称为空：查询全部
                    if (name.equals("")) {
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0"));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1"));
                                break;
                        };
                    } else {
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Name", "=", name));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Name", "=", name));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Name", "=", name));
                                break;
                        };
                    }
                }
                //结束时间不为空
                else {
                    //客户名称为空：查询全部
                    if (name.equals("")) {
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Time", "<", end));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Time", "<", end));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Time", "<", end));
                                break;
                        };
                    } else {
                        //客户名称不为空
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Name", "=", name).and("C_Time", "<", end));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Name", "=", name).and("C_Time", "<", end));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Name", "=", name).and("C_Time", "<", end));
                                break;
                        };
                    }
                }
            }
            //开始时间不为空
            else {
                //结束时间为空
                if (end.equals("")) {
                    //客户名称为空：查询全部
                    if (name.equals("")) {
                        //查询全部

                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Time", ">", start));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Time", ">", start));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Time", ">", start));
                                break;
                        };
                    } else {
                        //客户名称不为空
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Name", "=", name).and("C_Time", ">", start));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Name", "=", name).and("C_Time", ">", start));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Name", "=", name).and("C_Time", ">", start));
                                break;
                        };

                    }
                } else {
                    //客户名称为空：查询全部
                    if (name.equals("")) {
                        //结束时间不为空
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                        };

                    } else {
                        //客户名称不为空
                        //判断订单状态
                        switch (position){
                            case 0:
                                list = db.findAll(Selector.from(OrderModel.class).where("C_Name", "=", name).and("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                            case 1:
                                //未处理"0"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","0").and("C_Name", "=", name).and("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                            case 2:
                                //已完成"1"
                                list = db.findAll(Selector.from(OrderModel.class).where("C_State","=","1").and("C_Name", "=", name).and("C_Time", "<", end).and("C_Time", ">", start));
                                break;
                        };
                    }
                }

            }
            //展示查询后的订单列表
            showRecyclerView(list);
        } else {
            FuncUtils.showToast(this, "开始时间不能大于结束时间，请重新选择!");
        }

    }

    //展示查询后的订单列表
    private void showRecyclerView(List<OrderModel> list) {
        if (list == null || list.size() < 1) {
            FuncUtils.showToast(this, "没有满足所选查询条件下的订单信息，请重新设置查询条件!");
            if (list==null){
                list=new ArrayList<>();
            }
            list.clear();
            adapter = new QueryOrderAdapter(this,list);
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);

        } else {
            adapter = new QueryOrderAdapter(this,list);
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
    public void onSettingInputComplete(String input_name, String input_start, String input_end,int input_position) {
        name = input_name;
        start = input_start;
        end = input_end;
        position=input_position;
    }
}
