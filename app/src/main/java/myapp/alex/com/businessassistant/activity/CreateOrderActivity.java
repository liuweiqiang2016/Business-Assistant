package myapp.alex.com.businessassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeone.framework.db.sqlite.DbUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.CustomerAdapter;
import myapp.alex.com.businessassistant.adapter.OrderAdapter;
import myapp.alex.com.businessassistant.fragment.CreateOrderFragment;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.model.OrderServiceModel;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class CreateOrderActivity extends AppCompatActivity implements CreateOrderFragment.CreateInputListener, CustomerAdapter.SaveEditListener {


//    private ImageButton btn_add;
    private TextView money, order_id, order_time;
    private RecyclerView order_rv, customer_rv;
    private Button btn_save;
    View customView;
    ImageButton ib;
    TextView title;
    //当前系统时间
    private long millis = 0;
    //格式化时间
    private String d_time = "";

    private List<OrderServiceModel> list = new ArrayList<>();
    private float total = 0;
    OrderAdapter orderAdapter;
    Map<Integer, String> map = new HashMap<>();
    //数据库
    private DbUtils db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
//        getSupportActionBar().setDisplayShowHomeEnabled(true);////使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
//        getSupportActionBar().setHomeButtonEnabled(true);//这个小于4.0版本的默认值为true的。但是在4.0及其以上是false，该方法的作用：决定左上角的图标是否可以点击。没有向左的小图标
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setContentView(R.layout.activity_create_order);
        //初始化布局
        initView();
        //绑定事件
        initEvevt();


    }

    private void initView() {
        ib= (ImageButton) customView.findViewById(R.id.action_bar_ib);
        ib.setVisibility(View.VISIBLE);
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[0]);
//        btn_add = (ImageButton) findViewById(R.id.btn_add);
        money = (TextView) findViewById(R.id.text_money);
        order_rv = (RecyclerView) findViewById(R.id.order_recyclerView);
        customer_rv = (RecyclerView) findViewById(R.id.customer_recyclerView);
        btn_save = (Button) findViewById(R.id.btn_save);
        order_id = (TextView) findViewById(R.id.order_id);
        order_time = (TextView) findViewById(R.id.order_time);
        //设置订单编号
        millis = System.currentTimeMillis();
        order_id.setText("订单编号:" + millis);
        //设置订单时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = new Date(millis);
        d_time = df.format(date);
        order_time.setText("下单时间:" + d_time);
        //设置客户信息界面
        String[] infos = getResources().getStringArray(R.array.customer_info);
        String[] hints = getResources().getStringArray(R.array.customer_info_hint);
        CustomerAdapter customerAdapter = new CustomerAdapter(infos, hints, this);
        customer_rv.setLayoutManager(new LinearLayoutManager(this));
        customer_rv.setAdapter(customerAdapter);
        //动态存储RecyclerView每个item上EditText的内容
        map.clear();
        //存储订单内容
        list.clear();
        orderAdapter = new OrderAdapter(list, this);
        order_rv.setLayoutManager(new LinearLayoutManager(this));
        order_rv.setAdapter(orderAdapter);
        //数据库初始化
        db = MyDbUtils.getInstance().Db(this);
    }

    private void initEvevt() {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrderFragment dialog = new CreateOrderFragment();
                dialog.show(getFragmentManager(), "tag");
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
        if (list != null) {
            list.clear();
        }
        if (map != null) {
            map.clear();
        }
        total = 0;
        d_time = "";
//        super.onBackPressed();
        finish();
    }

//    //创建新订单
//    public void createOrder(View view) {
//
////        CreateOrderFragment dialog = new CreateOrderFragment();
////        dialog.show(getFragmentManager(), "tag");
//
//    }

    ;

    @Override
    public void onCreateInputComplete(ServiceModel model, String num) {

        OrderServiceModel orderServiceModel = new OrderServiceModel();
        orderServiceModel.setName(model.getName());
        orderServiceModel.setPrice(model.getPrice());
        orderServiceModel.setNum(num);
        //设置服务id，非常重要，与订单表唯一联系
        orderServiceModel.setOrder_id(millis+"");
        list.add(orderServiceModel);
        orderAdapter.notifyDataSetChanged();

        total = total + (model.getPrice() * Integer.parseInt(num));
        money.setText("总金额:" + total + "元");

    }

    //保存订单
    public void OnSave(View view) {
        if (list == null || list.size() < 1) {
            FuncUtils.showToast(this, "订单不能为空，请创建订单后再保存!");
            return;
        } else {
            //position为0时，确定是第一项内容，即客户名称，必填
            //为null或为空白提示错误
            if (map.get(0) == null || map.get(0).trim().equals("")) {
                FuncUtils.showToast(this, "客户名称为必填项，且不可为空格!");
                return;
            } else {
                //存储订单
                saveOrder();
                FuncUtils.showToast(this, "订单已创建成功，请尽快处理！");
                onBackPressed();
//                //判断是否多次点击了保存按钮
//                OrderModel orderModel = db.findFirst(Selector.from(OrderModel.class).where("C_OrderID", "=", millis+""));
//                if (orderModel != null) {
//                    //删除之前的订单信息
//                    db.delete(orderModel);
//                    //删除之前的订单详情信息
//                    db.delete(OrderServiceModel.class, WhereBuilder.b("C_OrderID", "=", millis+""));
//                    //存储订单
//                    saveOrder();
//                    Toast.makeText(this, "订单已更新成功，请尽快处理！", Toast.LENGTH_SHORT).show();
//                } else {
//                    saveOrder();
//                    Toast.makeText(this, "订单已创建成功，请尽快处理！", Toast.LENGTH_SHORT).show();
//                }

            }


        }
    }

    void saveOrder() {
        //开始存储订单详情数据
        db.saveAll(list);
        //存储订单数据
        OrderModel orderModel = new OrderModel();
        orderModel.setOrder_id(millis+"");
        orderModel.setTime(d_time);
        orderModel.setTotal(total + "");
        //由于客户信息表是已知长度，以下项就直接用数字取出来
        //更为通用的方法是采用for循环，根据infos长度来一一取出来
        CustomerModel customerModel=new CustomerModel();
        //设置客户编码
        customerModel.setOrder_id(millis+"");
        //设置数据来源
        customerModel.setSource("0");
        //设置名称
        customerModel.setName(map.get(0).trim());
        //如果联系方式为null，设置为空字符串，以下类似
        if (map.get(1) == null) {
            customerModel.setTel("");
        } else {
            customerModel.setTel(map.get(1).trim());
        }
        if (map.get(2) == null) {
            customerModel.setAdds("");
        } else {
            customerModel.setAdds(map.get(2).trim());
        }
        if (map.get(3) == null) {
            customerModel.setInfo("");
        } else {
            customerModel.setInfo(map.get(3).trim());
        }
        //新建订单，状态统一为"0"
        orderModel.setState("0");
        //客户名称(为节省运算内存，本应放只在CustomerModel表中)
        orderModel.setName(map.get(0).trim());
        //存储用户信息
        db.save(customerModel);
        //存储订单信息
        db.save(orderModel);
    }

    //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
    @Override
    public void SaveEdit(int position, String string) {
        map.put(position, string);
    }
}
