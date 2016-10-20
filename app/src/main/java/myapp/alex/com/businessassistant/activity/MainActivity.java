package myapp.alex.com.businessassistant.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

import java.io.InputStream;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.HomeAdapter;
import myapp.alex.com.businessassistant.fragment.CheckVersionFragment;
import myapp.alex.com.businessassistant.fragment.FileProgressFragment;
import myapp.alex.com.businessassistant.fragment.SoftUpdateFragment;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.model.VersionInfoModel;
import myapp.alex.com.businessassistant.model.VersionModel;
import myapp.alex.com.businessassistant.utils.DownFileUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;
import myapp.alex.com.businessassistant.utils.ParseXMLUtils;


public class MainActivity extends AppCompatActivity implements SoftUpdateFragment.SoftUpdateListener {

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private String[] items_txt;
    private int[] items_img;
    //数据库
    private DbUtils db;
    private VersionInfoModel versionInfoModel;
    private SoftUpdateFragment fragment;
    private FileProgressFragment progressFragment;
    CheckVersionFragment checkFragment;

    //判断是否在下载apk文件
    private boolean isDowning=false;
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
        mAdapter = new HomeAdapter(this, items_txt, items_img, 0);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
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
            model.setPrice((float) 2.5);
            db.save(model);
        }
        List<CostTypeModel> costModelList;
        costModelList = db.findAll(Selector.from(CostTypeModel.class));
        if (costModelList == null) {
            //初始化成本表
            String[] castNames = getResources().getStringArray(R.array.cost_name);
            CostTypeModel castModel = new CostTypeModel();
            for (int i = 0; i < castNames.length; i++) {
                //设置成本名称
                castModel.setName(castNames[i]);
                //设置成本编号，唯一
                castModel.setC_id(i);
                db.save(castModel);
            }
        }
        //软件打开10s后，检测版本 牺牲用户体验（去除）且会崩溃
//        mHandler.sendEmptyMessageDelayed(6,10*1000);
    }

    //查找未完成订单信息
    void QureyData() {
        db = MyDbUtils.getInstance().Db(this);
        List<OrderModel> orderModelList;
        orderModelList = db.findAll(Selector.from(OrderModel.class).where("C_State", "=", "0"));
        if (orderModelList != null) {
            mAdapter.upData(1, orderModelList.size());
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

    void checkPermission() {

        if (!MPermissions.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUECT_CODE_SDCARD)) {
            MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        MPermissions.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @ShowRequestPermissionRationale(REQUECT_CODE_SDCARD)
    public void whyNeedSdCard() {
        FuncUtils.showToast(this, "APP存储数据到手机内存中，需要手机内存读写权限!");
        MPermissions.requestPermissions(MainActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess() {
        //Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed() {
        FuncUtils.showToast(this, "APP存储数据到手机内存中，需要手机内存读写权限!");
    }

    private void initEvent() {
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        //创建新订单
                        intent.setClass(MainActivity.this, CreateOrderActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //未完成订单
                        intent.setClass(MainActivity.this, UnDoneActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //订单查询
                        intent.setClass(MainActivity.this, QueryOrderActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //项目编辑
                        intent.setClass(MainActivity.this, EditServiceActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        //日常开销
                        intent.setClass(MainActivity.this, AddCostActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //开销查询
                        intent.setClass(MainActivity.this, QueryCostActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        //财务分析
                        intent.setClass(MainActivity.this, DataAnalyzeActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //客户添加
                        intent.setClass(MainActivity.this, AddCustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        //客户查询
                        intent.setClass(MainActivity.this, QueryCustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 9:
                        //版本更新
                        checkVersionInfo(true);
                        break;
                    case 10:
                        //工作笔记
                        intent.setClass(MainActivity.this, AddNoteActivity.class);
                        startActivity(intent);
                        break;
                    case 11:
                        //笔记查询
                        intent.setClass(MainActivity.this, QueryNoteActivity.class);
                        startActivity(intent);
                        break;
                    case 12:
                        //用户手册
                        intent.setClass(MainActivity.this, ManualActivity.class);
                        startActivity(intent);
                        break;
                    case 13:
                        //关于
                        intent.setClass(MainActivity.this, AboutActivity.class);
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //解析版本更新信息xml文件
//                final String fileName = FuncUtils.APP_UPDATE_URL.substring(FuncUtils.APP_UPDATE_URL.lastIndexOf("/") + 1);
                final String fileName = FuncUtils.APP_XML_NAME;
                try {
                    //防止xml解析时异常
                    InputStream inputStream = FuncUtils.getInputStreamFromSDcard(fileName);
                    versionInfoModel = ParseXMLUtils.Parse(inputStream);
                } catch (Exception e) {
                } finally {
                    if (checkFragment!=null){
                        checkFragment.dismiss();
                    }
                    if (versionInfoModel != null) {
                        //版本信息下载完成后，入库
                        List<VersionModel> modelList;
                        modelList = db.findAll(Selector.from(VersionModel.class));
                        if (modelList == null||modelList.size()<1) {
                            //不存在版本数据，添加一条数据
                            VersionModel model=new VersionModel();
                            model.setCode(versionInfoModel.getCode());
                            model.setTime(System.currentTimeMillis()+"");
                            db.save(model);
                        }else{
                            //存在版本数据，更新该条数据
                            modelList.get(0).setCode(versionInfoModel.getCode());
                            modelList.get(0).setTime(System.currentTimeMillis()+"");
                            db.update(modelList.get(0));
                        }

                        String old_code = FuncUtils.getVersion(MainActivity.this);
                        //当前版本低于网络版本
                        if (FuncUtils.compareVersion(old_code, versionInfoModel.getCode())) {
                            versionInfoModel.setCode_old(old_code);
                            fragment = SoftUpdateFragment.newInstance(versionInfoModel);
                            fragment.show(getFragmentManager(), "tag");
                        } else {
                            FuncUtils.showToast(MainActivity.this, "当前版本已是最新版本，无需更新!");
                        }

                    }
                }

            }
            if (msg.what == 2) {
                if (fragment!=null){
                    fragment.dismiss();
                }
                FuncUtils.showToast(MainActivity.this, "网络异常，版本检测失败，请稍后再试!");
            }
            if (msg.what == 3) {
                //修改下载状态
                isDowning=true;
                //弹出下载进度提示栏
                progressFragment=FileProgressFragment.newInstance("版本升级中","后台下载");
                progressFragment.show(getFragmentManager(), "tag");
            }
            if (msg.what == 4) {
                if (progressFragment!=null){
                    //更新apk下载进度
                    progressFragment.setProgress(msg.arg1);
                }
            }
            if (msg.what==5){
                //apk下载完成
                if(progressFragment!=null){
                    progressFragment.dismiss();
                }
                FuncUtils.installApk(MainActivity.this);
                //修改下载状态
                isDowning=false;
            }
            if (msg.what==6){
                checkVersionInfo(false);
            }
        }
    };

    //按返回按钮，退出到手机主界面

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    public void onSoftUpdate(final String link) {

        if (isDowning){
            FuncUtils.showToast(this,"后台正在努力下载新版本，请稍后...");
            return;
        }

        //若apk存在且apk文件的版本号大于或等于xml文件中的版本号，不再重新下载，直接安装
        if(FuncUtils.checkFileState(FuncUtils.APP_DOWNFILE_NAME)){
            String apkCode=FuncUtils.apkCode(this);
            List<VersionModel> modelList;
            modelList = db.findAll(Selector.from(VersionModel.class));
            String xmlCode=modelList.get(0).getCode();
            //若apk文件的版本号等于xml文件版本号  或apk文件版本号高于xml文件版本号
            if (apkCode.equals(xmlCode)||FuncUtils.compareVersion(xmlCode,apkCode)){
                //直接安装该apk
                mHandler.sendEmptyMessage(5);
                return;
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                mHandler.sendEmptyMessage(3);
                //下载apk文件(版本升级信息)
                DownFileUtil util=new DownFileUtil(mHandler);
                util.getFile(link, FuncUtils.APP_DIR, false);
                return null;
            }
        }.execute();

    }

    //检查是否有新版本 userCheck true:用户主动检测

    void checkVersionInfo(boolean userCheck){
        //判断网络是否连接
        if (!FuncUtils.isNetworkAvailable(MainActivity.this)) {
            if (userCheck){//用户主动检测的，弹出toast，否则不提示
                FuncUtils.showToast(MainActivity.this, "版本检测需要联网，请联网后再试!");
            }
            return;
        }
        //版本更新 检查更新规定为1小时 1小时之内不下载新的更新信息
        List<VersionModel> modelList;
        modelList = db.findAll(Selector.from(VersionModel.class));

        if (modelList==null||modelList.size()<1||!FuncUtils.checkFileState(FuncUtils.APP_XML_NAME)) {
//            fragment = SoftUpdateFragment.newInstance(null);
//            fragment.show(getFragmentManager(), "tag");
            checkFragment=CheckVersionFragment.newInstance("版本检查中...","");
            checkFragment.show(getFragmentManager(), "tag");
            //不存在版本数据、不存在xml文件，必定下载
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    //下载xml文件(版本升级信息)
                    DownFileUtil util=new DownFileUtil(mHandler);
                    util.getFile(FuncUtils.APP_UPDATE_URL, FuncUtils.APP_DIR, true);

                    return null;
                }
            }.execute();
        }else{
            //存在版本数据，比较当前时间与版本数据时间，是否间隔1小时
            if (FuncUtils.checkUpdateTime(modelList.get(0).getTime())){
//                fragment = SoftUpdateFragment.newInstance(null);
//                fragment.show(getFragmentManager(), "tag");
                checkFragment=CheckVersionFragment.newInstance("版本检查中...","");
                checkFragment.show(getFragmentManager(), "tag");
                //大于一小时，下载xml文件
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        //下载xml文件(版本升级信息)
                        DownFileUtil util=new DownFileUtil(mHandler);
                        util.getFile(FuncUtils.APP_UPDATE_URL, FuncUtils.APP_DIR, true);

                        return null;
                    }
                }.execute();

            }else {
                //无需下载xml文件，直接解析已存在的xml文件
                mHandler.sendEmptyMessage(1);
            }

        }
    }

}


