package myapp.alex.com.businessassistant.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import com.zeone.framework.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.EditServiceAdapter;
import myapp.alex.com.businessassistant.fragment.AddServiceFragment;
import myapp.alex.com.businessassistant.fragment.DeleteServiceFragment;
import myapp.alex.com.businessassistant.fragment.EditServiceFragment;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class EditServiceActivity extends AppCompatActivity implements EditServiceFragment.EditOrderListener,AddServiceFragment.AddServiceListener,DeleteServiceFragment.DeleteServiceListener{

    View customView;
    ImageButton ib;
    TextView title;
    RecyclerView rv;
    EditServiceAdapter adapter;
    //数据库
    private DbUtils db;
    List<ServiceModel> list;
    //存放所有的服务名称
    ArrayList<String> names=new ArrayList<>();
    String TAG="标签";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getResources().getStringArray(R.array.items_txt)[4]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_edit);
        //初始化数据
        initData();
        //初始化布局
        initView();
        //绑定事件
        initEvent();

    }

    private void initEvent() {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加服务项
                List<ServiceModel> tempList= db.findAll(Selector.from(ServiceModel.class));
                AddServiceFragment fragment=AddServiceFragment.newInstance(tempList.size()+"",names);
                fragment.show(getFragmentManager(),"tag");

            }
        });

        adapter.setOnItemClickLitener(new EditServiceAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //编辑服务项
                EditServiceFragment fragment= EditServiceFragment.newInstance(list.get(position).getName(),list.get(position).getC_id()+"",list.get(position).getPrice()+"",position);
                fragment.show(getFragmentManager(),"tag");

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //删除服务项
                DeleteServiceFragment fragment=DeleteServiceFragment.newInstance(list.get(position).getName(),list.get(position).getC_id()+"",list.get(position).getPrice()+"",position);
                fragment.show(getFragmentManager(),"tag");

            }
        });

    }

    private void initData() {
        db = MyDbUtils.getInstance().Db(this);
        list=db.findAll(Selector.from(ServiceModel.class).where("C_Show","=","1"));
        //存放服务名称
        names.clear();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                names.add(list.get(i).getName());
            }
        }
    }

    private void initView() {
        ib= (ImageButton) customView.findViewById(R.id.action_bar_ib);
        ib.setVisibility(View.VISIBLE);
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[3]);
        rv= (RecyclerView) findViewById(R.id.rv_edit);
        rv.setLayoutManager(new LinearLayoutManager(this));
        if (list==null)
        {list=new ArrayList<>();}
        adapter=new EditServiceAdapter(this,list);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
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

    //编辑一个服务
    @Override
    public void onEditPriceComplete(String edit_price, int position) {
        //处理Fragment单价输入按完成后，处理更新一个项目
        ServiceModel model=list.get(position);
        if (model!=null){
            try {
                model.setPrice(Float.parseFloat(edit_price));
                db.update(model);
                //更新列表
                adapter.notifyItemChanged(position,model);
                FuncUtils.showToast(this,"修改服务项成功!");

        }catch (Exception e){

            };
    }
    }

    //新增一个服务
    @Override
    public void onAddServiceInputComplete(String name, String price, String c_id) {

        //处理新增一个项目
        ServiceModel model=new ServiceModel();
        model.setName(name);
        try {
            model.setPrice(Float.parseFloat(price));
            model.setC_id(Integer.parseInt(c_id));
            model.setShow("1");
            db.save(model);
            //存放名称
            names.add(name);
//            order_list.add(model);
            //更新列表
            //order_list=db.findAll(Selector.from(ServiceModel.class).where("C_Show","=","1"));
//            adapter.addData(0,model);
            adapter.addData(model);
            FuncUtils.showToast(this,"新增服务项成功!");
        }catch (Exception e){

        };


    }

    //删除一个服务
    @Override
    public void onDeleteServiceComplete(int position) {

//        ServiceModel model=order_list.get(order_position);
//        model.setShow("0");
//        db.update(model);
        Message msg=mHandler.obtainMessage();
        msg.what=1;
        msg.arg1=position;
        mHandler.sendMessage(msg);


    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    int position=msg.arg1;
                    ServiceModel model=list.get(position);
                    if (model!=null){
                        try {
                            ServiceModel temp=new ServiceModel();
                            temp.setC_id(model.getC_id());
                            temp.setName(model.getName());
                            temp.setShow("0");
                            temp.setPrice(model.getPrice());
                            db.delete(ServiceModel.class, WhereBuilder.b("C_ID","=",model.getC_id()));
                            db.save(temp);
//                            model.setShow("0");
//                            db.update(model);
                            //更新列表
                            adapter.removeData(position);
                            //adapter.notifyItemRemoved(order_position);
                            //删除该项数据
                            // order_list.remove(order_position);
                            names.remove(position);
                            FuncUtils.showToast(EditServiceActivity.this,"删除服务项成功!");

                        }catch (Exception e){

                            Log.e(TAG, "onDeleteServiceComplete: "+e.getMessage());

                        };
                    }
                    break;
            }

        }
    };
}
