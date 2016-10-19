package myapp.alex.com.businessassistant.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import myapp.alex.com.businessassistant.adapter.QueryNoteAdapter;
import myapp.alex.com.businessassistant.fragment.NoteSettingFragment;
import myapp.alex.com.businessassistant.model.NoteModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class QueryNoteActivity extends AppCompatActivity implements NoteSettingFragment.NoteSettingInputListener{


    RecyclerView rv_query;
    QueryNoteAdapter adapter;
    //数据库
    private DbUtils db;
    List<NoteModel> list = new ArrayList<>();
    //初始条件
    String subject = "",start="",end="";
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
        setContentView(R.layout.activity_query_note);
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
        title.setText(getResources().getStringArray(R.array.items_txt)[11]);
        rv_query = (RecyclerView) findViewById(R.id.rv_note);
        list.clear();
//        list=db.findAll(Selector.from(NoteModel.class));
        adapter=new QueryNoteAdapter(this,list,getFragmentManager());
        rv_query.setLayoutManager(new LinearLayoutManager(this));
        rv_query.setAdapter(adapter);
        rv_query.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    //设置查询条件
    public void OnNoteSetting(View view) {
        NoteSettingFragment fragment=NoteSettingFragment.newInstance(subject,start,end);
        fragment.show(getFragmentManager(), "tag");
    }

    //处理查询
    public void OnNoteQuery(View view) {
        QueryDate();
        ShowView();

    }



    private void QueryDate() {
        //查询数据
       if (!FuncUtils.compareDateHMS(start,end)){
           if (subject.equals("")){
               if (start.equals("")){
                   if (end.equals("")){
                       //主题为空，开始时间为空，结束时间为空
                       list=db.findAll(Selector.from(NoteModel.class));
                   }else {
                       //主题为空，开始时间为空，结束时间不为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time","<",end));
                   }
               }else {
                   if (end.equals("")){
                       //主题为空，开始时间不为空，结束时间为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time",">",start));
                   }else {
                       //主题为空，开始时间不为空，结束时间不为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time","<",end).and("C_Time",">",start));
                   }
               }
           }else{
               if (start.equals("")){
                   if (end.equals("")){
                       //主题不为空，开始时间为空，结束时间为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Subject","=",subject));
                   }else {
                       //主题不为空，开始时间为空，结束时间不为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time","<",end).and("C_Subject","=",subject));
                   }
               }else {
                   if (end.equals("")){
                       //主题不为空，开始时间不为空，结束时间为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time",">",start).and("C_Subject","=",subject));
                   }else {
                       //主题不为空，开始时间不为空，结束时间不为空
                       list=db.findAll(Selector.from(NoteModel.class).where("C_Time","<",end).and("C_Time",">",start).and("C_Subject","=",subject));
                   }
               }
           }

       }else{
           FuncUtils.showToast(this, "开始时间不能大于结束时间，请重新选择!");
       }
    }
    private void ShowView() {
        //更新界面
        if(list==null||list.size()<1){
            FuncUtils.showToast(this, "没有满足所选查询条件下的客户信息，请重新设置查询条件!");
            if (list==null){
                list=new ArrayList<>();
            }
            list.clear();
            adapter=new QueryNoteAdapter(this,list,getFragmentManager());
            rv_query.setLayoutManager(new LinearLayoutManager(this));
            rv_query.setAdapter(adapter);
        }else {
            adapter=new QueryNoteAdapter(this,list,getFragmentManager());
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
    public void onNoteSettingInputComplete(String input_subject, String input_start, String input_end) {
        subject=input_subject;
        start=input_start;
        end=input_end;
    }
}
