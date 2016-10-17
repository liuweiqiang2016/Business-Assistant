package myapp.alex.com.businessassistant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.ManualAdapter;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;

public class ManualActivity extends AppCompatActivity {

    View customView;
    TextView title;
    RecyclerView rv;
    String[] titles;
    String[] contents;
    ManualAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_manual);
        //初始化数据
        initData();
        //初始化布局
        initView();

    }
    private void initData() {
        titles=getResources().getStringArray(R.array.manual_titles);
        contents=getResources().getStringArray(R.array.manual_contents);

    }
    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[12]);
        rv= (RecyclerView) findViewById(R.id.rv_manual);
        adapter=new ManualAdapter(titles,contents,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));



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
