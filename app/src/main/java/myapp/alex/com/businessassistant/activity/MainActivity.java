package myapp.alex.com.businessassistant.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.HomeAdapter;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private String[] items_txt;
    private int[] items_img;
    //数据库
    private DbUtils db;

//    MaqueeTextView tv_orderinfo,tv_scheduleinfo;

    private static final int REQUECT_CODE_SDCARD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查权限
        checkPermission();
        //初始化数据
        initData();
        //初始化布局
        initView();
        //绑定监听
        initEvent();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new HomeAdapter(this, items_txt, items_img,0);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
//        tv_orderinfo= (MaqueeTextView) findViewById(R.id.tv_orderinfo);
//        tv_scheduleinfo= (MaqueeTextView) findViewById(R.id.tv_scheduleinfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        QureyData();
    }

    protected void initData() {
        //初始化主界面图标及文字
        items_txt = getResources().getStringArray(R.array.items_txt);
        items_img = new int[]{
                R.drawable.ic_add_box_blue_a400_48dp,
                R.drawable.ic_assignment_late_red_a200_48dp,
                R.drawable.ic_assignment_deep_purple_600_48dp,
                R.drawable.ic_mode_edit_green_600_48dp,
                R.drawable.ic_attach_money_amber_700_48dp,
                R.drawable.ic_find_in_page_green_600_48dp,
                R.drawable.ic_assessment_amber_700_48dp,
                R.drawable.ic_folder_shared_green_600_48dp,
                R.drawable.ic_supervisor_account_blue_a400_48dp,
                R.drawable.ic_system_update_blue_a400_48dp,
                R.drawable.ic_work_deep_purple_600_48dp,
                R.drawable.ic_import_contacts_amber_700_48dp,
                R.drawable.ic_library_books_red_a200_48dp,
                R.drawable.ic_help_outline_blue_a400_48dp,
                R.drawable.ic_power_settings_new_red_a200_48dp
        };
        //初始化可修改项目（服务和成本）
        db = MyDbUtils.getInstance().Db(this);
        //判断数据库中是否存在服务表和成本表，若不存在，初始化数据
        List<ServiceModel> modelList;
        modelList = db.findAll(Selector.from(ServiceModel.class));
        if (modelList == null) {
            //初始化服务表
            String[] names = getResources().getStringArray(R.array.model_name);
            int[] prices = getResources().getIntArray(R.array.model_price);
            ServiceModel model = new ServiceModel();
            for (int i = 0; i < names.length; i++) {
                //设置服务编码，唯一，方便用户查询
                model.setC_id(i);
                //设置服务名称
                model.setName(names[i]);
                //设置服务价格
                model.setPrice((float) prices[i]);
                //设置显示状态
                model.setShow("1");
                db.save(model);
            }
            //特殊的项目：<item>上衣+裤子（学生、夏）</item>5元/2套
            model.setC_id(names.length);
            //设置服务名称
            model.setName("上衣+裤子(学生、夏)");
            //设置服务价格
            model.setPrice((float)2.5);
            db.save(model);
        }
        List<CostTypeModel> costModelList;
        costModelList=db.findAll(Selector.from(CostTypeModel.class));
        if(costModelList==null){
            //初始化成本表
            String[] castNames=getResources().getStringArray(R.array.cost_name);
            CostTypeModel castModel=new CostTypeModel();
            for(int i=0;i<castNames.length;i++){
                //设置成本名称
                castModel.setName(castNames[i]);
                //设置成本编号，唯一
                castModel.setC_id(i);
                db.save(castModel);
            }
        }

    }
    //查找未完成订单信息
    void QureyData(){
        db = MyDbUtils.getInstance().Db(this);
        List<OrderModel> orderModelList;
        orderModelList=db.findAll(Selector.from(OrderModel.class).where("C_State","=","0"));
        if (orderModelList!=null){
            mAdapter.upData(1,orderModelList.size());
        }
//        if(orderModelList==null||orderModelList.size()<1){
//            tv_orderinfo.setText(getString(R.string.tv_orderinfo_null));
//            tv_orderinfo.setTextColor(this.getResources().getColor(R.color.color_infotxt_null));
//        }else {
//            tv_orderinfo.setText("您有"+orderModelList.size()+"个未完成订单需要处理");
//            tv_orderinfo.setTextColor(this.getResources().getColor(R.color.color_infotxt_has));
//
////            mAdapter.upData(1,orderModelList.size());
//
////            mAdapter.notifyItemChanged
//        }


    }

    void checkPermission(){

        if(!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUECT_CODE_SDCARD)){
            MPermissions.requestPermissions(MainActivity.this,REQUECT_CODE_SDCARD,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        MPermissions.onRequestPermissionsResult(MainActivity.this,requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @ShowRequestPermissionRationale(REQUECT_CODE_SDCARD)
    public void whyNeedSdCard()
    {
        Toast.makeText(this, "APP存储数据到手机内存中，需要手机内存读写权限!", Toast.LENGTH_SHORT).show();
        MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        //Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }
    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        Toast.makeText(this, "APP存储数据到手机内存中，需要手机内存读写权限!", Toast.LENGTH_SHORT).show();
    }

    private void initEvent() {
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent();
                switch (position) {
                    case 0:
                        //创建新订单
                        intent.setClass(MainActivity.this,CreateOrderActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //未完成订单
                        intent.setClass(MainActivity.this,UnDoneActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //订单查询
                        intent.setClass(MainActivity.this,QueryOrderActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //项目编辑
                        intent.setClass(MainActivity.this,EditServiceActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        //日常开销
                        intent.setClass(MainActivity.this,AddCostActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //开销查询
                        intent.setClass(MainActivity.this,QueryCostActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        //财务分析
                        intent.setClass(MainActivity.this,DataAnalyzeActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //客户添加
                        intent.setClass(MainActivity.this,AddCustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        //客户查询
                        intent.setClass(MainActivity.this,QueryCustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 9:
                        //版本更新
                        break;
                    case 10:
                        //工作笔记
                        intent.setClass(MainActivity.this,AddNoteActivity.class);
                        startActivity(intent);
                        break;
                    case 11:
                        //笔记查询
                        intent.setClass(MainActivity.this,QueryNoteActivity.class);
                        startActivity(intent);
                        break;
                    case 12:
                        //用户手册
                        intent.setClass(MainActivity.this,ManualActivity.class);
                        startActivity(intent);
                        break;
                    case 13:
                        //关于
                        intent.setClass(MainActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 14:
                        //退出
                        finish();
                        System.exit(0);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                    default:
                        break;
                }

            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    //按返回按钮，退出到手机主界面

    @Override
    public void onBackPressed() {

//        Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
//        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        startActivity(mHomeIntent);
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());

    }


//    //切换fragment
//    void setFragment(Fragment fragment){
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager
//                .beginTransaction();
//        try {
//            fragmentTransaction.replace(R.id.id_contanier, fragment,
//                    fragment.getClass().getName()).commitAllowingStateLoss();
//            //提交更改
////            fragmentTransaction.commit();
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println(e.getCause());
//        }
//
//    };

}


