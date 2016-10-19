package myapp.alex.com.businessassistant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.DetailCustomerAdapter;
import myapp.alex.com.businessassistant.adapter.OrderAdapter;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.model.OrderServiceModel;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class OrderDetailActivity extends AppCompatActivity {

    String orderid = "";
    String tag = "";
    Button done;
    TextView detail_total, detail_id, detail_time,detail_state,detail_dtime;
    RecyclerView detail_order, detail_customer;
    //数据库
    private DbUtils db;
    List<OrderServiceModel> list;
    OrderModel orderModel;
    CustomerModel customerModel;
    OrderAdapter orderAdapter;
    DetailCustomerAdapter detailCustomerAdapter;
    String[] datas;
    View customView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(getResources().getString(R.string.title_activity_detailorder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_order_detail);
        //初始化数据
        initData();
        //初始化布局
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        tag = intent.getStringExtra("tag");
        db = MyDbUtils.getInstance().Db(this);
        orderModel = db.findFirst(Selector.from(OrderModel.class).where("C_OrderID", "=", orderid));
        list = db.findAll(Selector.from(OrderServiceModel.class).where("C_OrderID", "=", orderid));
        customerModel=db.findFirst(Selector.from(CustomerModel.class).where("C_OrderID", "=", orderid));
//        datas = new String[]{orderModel.getName(), orderModel.getTel(), orderModel.getAdds(), orderModel.getInfo()};
        datas = new String[]{customerModel.getName(), customerModel.getTel(), customerModel.getAdds(), customerModel.getInfo()};
    }

    private void initView() {

        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getString(R.string.title_activity_detailorder));

        done = (Button) findViewById(R.id.btn_done);
        detail_total = (TextView) findViewById(R.id.detail_total);
        detail_time = (TextView) findViewById(R.id.detail_time);
        detail_id = (TextView) findViewById(R.id.detail_id);
        detail_state= (TextView) findViewById(R.id.detail_state);
        detail_dtime= (TextView) findViewById(R.id.detail_dtime);
        detail_order = (RecyclerView) findViewById(R.id.detail_order);
        detail_customer = (RecyclerView) findViewById(R.id.detail_customer);

        detail_total.setText("总金额:"+orderModel.getTotal()+"元");
        detail_id.setText("订单编号:"+orderModel.getOrder_id());
        detail_time.setText("下单时间:"+orderModel.getTime());
        if (orderModel.getState().equals("0")){
            detail_state.setText("订单状态:未处理");
            detail_dtime.setText("完成时间:未处理");
        }else{
            detail_state.setText("订单状态:已完成");
            detail_dtime.setText("完成时间:"+orderModel.getDtime());
        }
        //如果为订单查询，完成按钮不显示
        if (tag.equals("1")) {
            done.setVisibility(View.INVISIBLE);
        }
        orderAdapter = new OrderAdapter(list, this);
        detail_order.setLayoutManager(new LinearLayoutManager(this));
        detail_order.setAdapter(orderAdapter);
        detailCustomerAdapter = new DetailCustomerAdapter(getResources().getStringArray(R.array.customer_info), datas, this);
        detail_customer.setLayoutManager(new LinearLayoutManager(this));
        detail_customer.setAdapter(detailCustomerAdapter);

    }

    //处理已完成按钮
    public void OnDone(View view) {
        //更新订单状态
        orderModel.setState("1");
        orderModel.setDtime(FuncUtils.getTime());
        db.update(orderModel);
        FuncUtils.showToast(this,"订单状态更新为:已完成!");
        onBackPressed();

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
