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
import myapp.alex.com.businessassistant.adapter.QueryCostAdapter;
import myapp.alex.com.businessassistant.fragment.CostSettingFragment;
import myapp.alex.com.businessassistant.model.CostModel;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class QueryCostActivity extends AppCompatActivity implements CostSettingFragment.CostSettingInputListener{

    RecyclerView rv_query;
    QueryCostAdapter adapter;
    //数据库
    private DbUtils db;
    List<CostModel> list = new ArrayList<>();
    //初始条件
    String start = "", end = "",mType="";
    int position=0;
    View customView;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_cost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
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
        title.setText(getResources().getStringArray(R.array.items_txt)[5]);
        rv_query = (RecyclerView) findViewById(R.id.rv_cost);
        list.clear();
//        list=db.findAll(Selector.from(CostModel.class));
//        adapter=new QueryCostAdapter(this,list);
        adapter=new QueryCostAdapter(this,list,getFragmentManager());
        rv_query.setLayoutManager(new LinearLayoutManager(this));
        rv_query.setAdapter(adapter);
        rv_query.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public void OnCostSetting(View view) {
        List<CostTypeModel> list =db.findAll(Selector.from(CostTypeModel.class));
        ArrayList<String> mStringList=new ArrayList<>();
        mStringList.clear();
        mStringList.add("全部");
        for (CostTypeModel model:list){
            mStringList.add(model.getName());
        }
        CostSettingFragment fragment=CostSettingFragment.newInstance(start,end,position,mStringList);
        fragment.show(getFragmentManager(),"tag");

    }
    public void OnCostQuery(View view) {

        QueryDate();
        ShowView();

    }

    private void ShowView() {

        if(list==null||list.size()<1){
            FuncUtils.showToast(this, "没有满足所选查询条件下的开销信息，请重新设置查询条件!");
            if (list==null){
                list=new ArrayList<>();
            }
            list.clear();
            adapter=new QueryCostAdapter(this,list,getFragmentManager());
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);
        }else {
            adapter=new QueryCostAdapter(this,list,getFragmentManager());
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);
        }
    }

    private void QueryDate() {

        if (!FuncUtils.compareDate(start,end)){
            if(position==0){
                if (start.equals("")){

                    if (end.equals("")){
                        //开始时间为空，结束时间为空，类型为全部
                        list=db.findAll(Selector.from(CostModel.class));

                    }else {
                        //开始时间为空，结束时间不为空，类型为全部
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time","<",end+" 00:00:00"));
                    }

                }else{
                    if (end.equals("")){
                        //开始时间不为空，结束时间为空，类型为全部
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time",">",start+" 00:00:00"));

                    }else{
                        //开始时间不为空，结束时间不为空，类型为全部
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time","<",end+" 00:00:00").and("C_Time",">",start+" 00:00:00"));

                    }
                }

            }else{
                if (start.equals("")){

                    if (end.equals("")){
                        //开始时间为空，结束时间为空，类型为某项
                        list=db.findAll(Selector.from(CostModel.class).where("C_Type","=",mType));

                    }else {
                        //开始时间为空，结束时间不为空，类型为某项
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time","<",end+" 00:00:00").and("C_Type","=",mType));
                    }

                }else{
                    if (end.equals("")){
                        //开始时间不为空，结束时间为空，类型为某项
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time",">",start+" 00:00:00").and("C_Type","=",mType));

                    }else{
                        //开始时间不为空，结束时间不为空，类型为某项
                        list=db.findAll(Selector.from(CostModel.class).where("C_Time","<",end+" 00:00:00").and("C_Time",">",start+" 00:00:00").and("C_Type","=",mType));

                    }
                }
            }

        }else{
            FuncUtils.showToast(this, "开始时间不能大于结束时间，请重新选择!");
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
    public void onCostSettingInputComplete(String input_start, String input_end, int input_position,String type) {
        //回调返回查询条件
        start=input_start;
        end=input_end;
        position=input_position;
        mType=type;
    }
}
