package myapp.alex.com.businessassistant.activity;

import android.content.Intent;
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
import myapp.alex.com.businessassistant.adapter.UnDoneAdapter;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class UnDoneActivity extends AppCompatActivity {

    RecyclerView rv_undown;
    //数据库
    private DbUtils db;
    List<OrderModel> list;
    UnDoneAdapter adapter;
    String TAG="UnDone";
    View customView;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getResources().getStringArray(R.array.items_txt)[1]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_undone);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化数据
        initDate();
        //初始化界面
        initView();
        //绑定监听
        initEvent();
    }

    private void initDate() {
        db = MyDbUtils.getInstance().Db(this);
        //查询未完成订单
        list=db.findAll(Selector.from(OrderModel.class).where("C_State","=","0"));
    }

    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[1]);
        if (list==null)
        {list=new ArrayList<>();}
        adapter=new UnDoneAdapter(this,list);
        rv_undown= (RecyclerView) findViewById(R.id.rv_undown);
        rv_undown.setLayoutManager(new LinearLayoutManager(this));
        rv_undown.setAdapter(adapter);
        rv_undown.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    private void initEvent() {

        adapter.setOnItemClickLitener(new UnDoneAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent();
                intent.putExtra("orderid",list.get(position).getOrder_id());
                //tag 0:UnDoneActivity 1:QueryOrderActivity
                intent.putExtra("tag","0");
                intent.setClass(UnDoneActivity.this,OrderDetailActivity.class);
                startActivity(intent);
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
}
