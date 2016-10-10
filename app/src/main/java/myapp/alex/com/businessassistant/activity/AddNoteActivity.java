package myapp.alex.com.businessassistant.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeone.framework.db.sqlite.DbUtils;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.model.NoteModel;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class AddNoteActivity extends AppCompatActivity {

    View customView;
    TextView title,tv_note_time;
    //数据库
    private DbUtils db;

    EditText et_subject,et_body;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_add_note);
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
        title.setText(getResources().getStringArray(R.array.items_txt)[10]);
        et_subject= (EditText) findViewById(R.id.et_note_subject);
        et_body= (EditText) findViewById(R.id.et_note_body);
        tv_note_time= (TextView) findViewById(R.id.tv_note_time);
        time=FuncUtils.getTime();
        tv_note_time.setText("编辑时间："+ time);

    }


    public void OnAddNote(View view) {
        if(et_subject.getText().toString().trim().equals("")){
            Toast.makeText(this,"笔记主题必填且不可为空格!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(et_body.getText().toString().trim().equals("")){
            Toast.makeText(this,"笔记内容必填且不可为空格!!",Toast.LENGTH_SHORT).show();
            return;
        }
//        CustomerModel model=new CustomerModel();
//        model.setOrder_id(c_id);
//        model.setName(et_name.getText().toString().trim());
//        model.setTel(et_tel.getText().toString());
//        model.setAdds(et_adds.getText().toString());
//        model.setInfo(et_info.getText().toString());
//        //用户添加为1
//        model.setSource("1");
        NoteModel model=new NoteModel();
        model.setCid(System.currentTimeMillis()+"");
        model.setSubject(et_subject.getText().toString().trim());
        model.setBody(et_body.getText().toString());
        model.setTime(time);
        db.save(model);
        Toast.makeText(this,"工作笔记保存完成!",Toast.LENGTH_SHORT).show();
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
