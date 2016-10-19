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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.HomeAdapter;
import myapp.alex.com.businessassistant.fragment.SoftUpdateFragment;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.model.VersionInfoModel;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;
import myapp.alex.com.businessassistant.utils.OkHttpHelper;
import myapp.alex.com.businessassistant.utils.ParseXMLUtils;
import myapp.alex.com.businessassistant.utils.UIProgressResponseListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements SoftUpdateFragment.SoftUpdateListener{

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private String[] items_txt;
    private int[] items_img;
    //数据库
    private DbUtils db;
    private VersionInfoModel versionInfoModel;
    private SoftUpdateFragment fragment;

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
        FuncUtils.showToast(this, "APP存储数据到手机内存中，需要手机内存读写权限!");
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
        FuncUtils.showToast(this, "APP存储数据到手机内存中，需要手机内存读写权限!");
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
                        //判断网络是否连接
                        if(!FuncUtils.isNetworkAvailable(MainActivity.this)){
                            FuncUtils.showToast(MainActivity.this,"版本检测需要联网，请联网后再试!");
                            return;
                        }
                        //版本更新
                        fragment=SoftUpdateFragment.newInstance(null);
                        fragment.show(getFragmentManager(),"tag");
                        new AsyncTask<Void,Void,Void>(){
                            @Override
                            protected Void doInBackground(Void... params) {

                                //下载xml文件(版本升级信息)
                                getFile(FuncUtils.APP_UPDATE_URL,FuncUtils.APP_DIR,true);

                                return null;
                            }
                        }.execute();
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

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                //解析版本更新信息xml文件
                final String fileName =FuncUtils.APP_UPDATE_URL.substring(FuncUtils.APP_UPDATE_URL.lastIndexOf("/") + 1);
                try {
                    //防止xml解析时异常
                    InputStream inputStream=FuncUtils.getInputStreamFromSDcard(fileName);
                    versionInfoModel= ParseXMLUtils.Parse(inputStream);
                }catch (Exception e){}
                finally {
                    fragment.dismiss();
                    if (versionInfoModel!=null){
                        String old_code=FuncUtils.getVersion(MainActivity.this);
                        //当前版本低于网络版本
                        if (FuncUtils.compareVersion(old_code,versionInfoModel.getCode())){
                            versionInfoModel.setCode_old(old_code);
                            fragment=SoftUpdateFragment.newInstance(versionInfoModel);
                            fragment.show(getFragmentManager(),"tag");
                        }else{
                            FuncUtils.showToast(MainActivity.this,"当前版本已是最新版本，无需更新!");
                        }

                    }
                }

            }
            if (msg.what==2){
                fragment.dismiss();
                FuncUtils.showToast(MainActivity.this,"网络异常，版本检测失败，请稍后再试!");
            }
        }
    };

    /**
     * 获取文件。
     *
     * @param url  地址
     * @param path 下载的文件地址
     * @param check 是否为版本检测(下载xml文件)
     */
    private void getFile(final String url, final String path,final boolean check) {
        final String fileName;
        if (check){
            //若下载xml文件
            fileName= url.substring(url.lastIndexOf("/") + 1);
        }else{
            //若下载apk文件
            fileName=FuncUtils.APP_DOWNFILE_NAME;
        }
        OkHttpClient client = new OkHttpClient();
        client = OkHttpHelper.getOkClient(client, new UIProgressResponseListener() {
            @Override
            public void onUIProgressRequest(long allBytes, long currentBytes, boolean done) {
                float progress = currentBytes * 100f / allBytes;
                Log.i("MAIN", "onUIProgressRequest: 总长度：" + allBytes + " 当前下载的长度：" + currentBytes + "是否下载完成：" + done + "下载进度：" + progress);

                if (done&&check){
                    //下载版本更新信息xml文件成功
                    mHandler.sendEmptyMessage(1);
                }

            }
        });
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (check){
                    //下载版本更新信息xml文件失败
                    mHandler.sendEmptyMessage(2);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File downLoad = new File(file, fileName);
                    FileOutputStream fos = new FileOutputStream(downLoad);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                }
            }
        });
    }

    //按返回按钮，退出到手机主界面

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    public void onSoftUpdate(final String link) {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                //下载apk文件(版本升级信息)
                getFile(link,FuncUtils.APP_DIR,false);

                return null;
            }
        }.execute();

    }


}


