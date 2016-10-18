package myapp.alex.com.businessassistant.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.ManualAdapter;
import myapp.alex.com.businessassistant.adapter.ThankAdapter;

public class AboutActivity extends AppCompatActivity {

    View customView;
    TextView title,tv_version;
    RecyclerView rv,rv_thk;
    String[] titles;
    String[] contents;
    String[] links;
    String version;
    ManualAdapter adapter;
    ThankAdapter thankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_about);
        //初始化数据
        initData();
        //初始化布局
        initView();

    }
    private void initData() {
        titles=getResources().getStringArray(R.array.about_dev);
        contents=getResources().getStringArray(R.array.about_dev_info);
        links=getResources().getStringArray(R.array.about_thank_links);
        version=getVersion();
    }
    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[13]);
        tv_version= (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本:V"+version);
        rv= (RecyclerView) findViewById(R.id.rv_dev);
        adapter=new ManualAdapter(titles,contents,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        rv_thk=(RecyclerView) findViewById(R.id.rv_thk);
        thankAdapter=new ThankAdapter(links,this);
        rv_thk.setLayoutManager(new LinearLayoutManager(this));
        rv_thk.setAdapter(thankAdapter);
    }

    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
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


}
