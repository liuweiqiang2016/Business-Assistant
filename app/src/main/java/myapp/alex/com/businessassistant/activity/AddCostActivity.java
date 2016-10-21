package myapp.alex.com.businessassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.CostModel;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class AddCostActivity extends AppCompatActivity {

    View customView;
    TextView title,tv_cost_id;
    //数据库
    private DbUtils db;
    List<CostTypeModel> typeModelList;

    Spinner sp_type;
    EditText et_cost_time,et_cost_name,et_cost_info,et_cost_money;

    DateTimePickDialogUtil dialog;
    String c_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_add_cost);
        //初始化数据
        initData();
        //初始化布局
        initView();

    }

    private void initData() {
        db = MyDbUtils.getInstance().Db(this);
        typeModelList=db.findAll(Selector.from(CostTypeModel.class).where("C_Show","=","1"));
    }
    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[4]);
        sp_type= (Spinner) findViewById(R.id.sp_cost_type);
        et_cost_time= (EditText) findViewById(R.id.et_cost_time);
        et_cost_name= (EditText) findViewById(R.id.et_cost_name);
        et_cost_info= (EditText) findViewById(R.id.et_cost_info);
        et_cost_money= (EditText) findViewById(R.id.et_cost_money);
        tv_cost_id= (TextView) findViewById(R.id.tv_cost_id);
        c_id=System.currentTimeMillis()+"";
        tv_cost_id.setText("开销编号："+c_id);
        List<String> names=new ArrayList<>();
        if (typeModelList!=null){
            for (CostTypeModel model:typeModelList){
                names.add(model.getName());
            }
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,names);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_type.setAdapter(adapter);

    }

    public void OnTime(View view) {

        String t1 = "";
        // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
        if (et_cost_time.getText().toString().equals("")) {

            t1 = FuncUtils.getDate();
        } else {
            t1 = et_cost_time.getText().toString();
        }
        if (dialog==null){
//            dialog = DateTimePickDialogUtil.getInstance(this,false);
            dialog = new DateTimePickDialogUtil(this,false);
        }
        dialog.dateTimePicKDialog(et_cost_time,t1);

    }

    public void OnAddCost(View view) {
        if(et_cost_time.getText().toString().equals("")){
            FuncUtils.showToast(this,"开销时间必填!");
            return;
        }

        if(et_cost_name.getText().toString().trim().equals("")){
            FuncUtils.showToast(this,"开销名称必填且不可为空格!");
            return;
        }
        if(et_cost_money.getText().toString().equals("")){
            FuncUtils.showToast(this,"开销金额必填!");
            return;
        }
        CostModel model=new CostModel();
        model.setCid(c_id);
        model.setType(sp_type.getSelectedItem().toString());
        model.setTime(et_cost_time.getText().toString());
        model.setName(et_cost_name.getText().toString());
        model.setMoney(et_cost_money.getText().toString());
        model.setInfo(et_cost_info.getText().toString());
        db.save(model);
        FuncUtils.showToast(this,"开销保存完成!");

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
