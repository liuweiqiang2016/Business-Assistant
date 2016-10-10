package myapp.alex.com.businessassistant.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeone.framework.db.sqlite.DbUtils;
import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class AddCustomerActivity extends AppCompatActivity {

    View customView;
    TextView title,tv_id;
    //数据库
    private DbUtils db;

    EditText et_name,et_tel,et_adds,et_info;

    String c_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_add_customer);
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
        title.setText(getResources().getStringArray(R.array.items_txt)[7]);
        et_name= (EditText) findViewById(R.id.et_customer_name);
        et_tel= (EditText) findViewById(R.id.et_customer_tel);
        et_adds= (EditText) findViewById(R.id.et_customer_adds);
        et_info= (EditText) findViewById(R.id.et_customer_info);
        tv_id= (TextView) findViewById(R.id.tv_customer_id);
        c_id=System.currentTimeMillis()+"";
        tv_id.setText("客户编号："+c_id);

    }


    public void OnAddCustomer(View view) {
        if(et_name.getText().toString().trim().equals("")){
            Toast.makeText(this,"客户名称必填且不可为空格!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(et_tel.getText().toString().equals("")){
            Toast.makeText(this,"联系方式必填!",Toast.LENGTH_SHORT).show();
            return;
        }
        CustomerModel model=new CustomerModel();
        model.setOrder_id(c_id);
        model.setName(et_name.getText().toString().trim());
        model.setTel(et_tel.getText().toString());
        model.setAdds(et_adds.getText().toString());
        model.setInfo(et_info.getText().toString());
        //用户添加为1
        model.setSource("1");
        db.save(model);
        Toast.makeText(this,"客户信息保存完成!",Toast.LENGTH_SHORT).show();
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
