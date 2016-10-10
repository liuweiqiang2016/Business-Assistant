package myapp.alex.com.businessassistant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.fragment.DataSettingFragment;
import myapp.alex.com.businessassistant.model.CostModel;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.ChartUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

public class DataAnalyzeActivity extends AppCompatActivity implements DataSettingFragment.DataSettingInputListener{


    //数据库
    private DbUtils db;
    View customView;
    TextView title;

    private LineChart mLineChart;
    private PieChart mPieChart;

    //收入
    List<OrderModel> orderModels;
    //成本
    List<CostModel> costModels;

    //初始条件
    String start = "", end = "";
    int position=0;
    String[] dateTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getResources().getStringArray(R.array.items_txt)[2]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        customView=getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_data_analyze);
        //初始化数据
        initData();
        //初始化布局
        initView();
    }

    private void initData() {
        db = MyDbUtils.getInstance().Db(this);
        dateTypes=getResources().getStringArray(R.array.sp_datas);
    }

    private void initView() {
        title= (TextView) customView.findViewById(R.id.action_bar_tv);
        title.setText(getResources().getStringArray(R.array.items_txt)[6]);
        mLineChart= (LineChart) findViewById(R.id.data_linechart);
        mPieChart= (PieChart) findViewById(R.id.data_piechart);

//        LineData mLineData = FuncUtils.getLineData(20, 10,"8月份数据");
//        FuncUtils.showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));


    }

    //设置查询条件
    public void OnDataSetting(View view) {

//        CostSettingFragment fragment=CostSettingFragment.newInstance(start,end,position,new ArrayList<String>());
//        fragment.show(getFragmentManager(),"tag11");
        DataSettingFragment fragment=DataSettingFragment.newInstance(start,end,position);
        fragment.show(getFragmentManager(),"tag");
    }

    //处理查询
    public void OnDataQuery(View view) {
        if (start.equals("")||end.equals("")){
            Toast.makeText(this,"请先设置查询条件!",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            if (!FuncUtils.compareDate(start,end)&&(FuncUtils.daysBetween(start,end)>0)){
                //根据position，决定展示什么图形
                showViewByPosition(position);
            }else{
                Toast.makeText(this, "开始时间需早于结束时间，请重新选择!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }

    }


    private void showViewByPosition(int position) {
        try {
            switch (position){
                //特定时间段的收入数据展示、折线图
                case 0:
                    //设置控件显示状态
                    mLineChart.setVisibility(View.VISIBLE);
                    mPieChart.setVisibility(View.GONE);
                    //由数据库查询，修改为内存查询
                    orderModels=db.findAll(Selector.from(OrderModel.class).where("C_Time",">",start+" 00:00:00").and("C_Time","<",end+" 00:00:00"));
                    int size=FuncUtils.daysBetween(start,end);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(start));
                    long time = cal.getTimeInMillis();
                    //存放每天的日期
                    List<String> days=new ArrayList<>();
                    days.clear();
                    for (int i=0;i<size;i++){
                        //添加日期 1L防止数据溢出
                        days.add(sdf.format((1L*i*(1000*3600*24)+time)));
                    }
                    //存放每天的收入总额
                    List<Float> dayIncomes=new ArrayList<>();
                    dayIncomes.clear();
                    for (int i=0;i<size;i++){
                        dayIncomes.add(QueryDayIncome(days.get(i)));
                    }
                    LineData mLineData = ChartUtil.getLineData(days,dayIncomes,dateTypes[position]+"数据统计");
                    ChartUtil.showLineChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
                    break;
                //特定时间段内的开支数据展示、饼图
                case 1:
                    //设置控件显示状态
                    mPieChart.setVisibility(View.VISIBLE);
                    mLineChart.setVisibility(View.GONE);
                    //查询数据
                    costModels=db.findAll(Selector.from(CostModel.class).where("C_Time",">",start).and("C_Time","<",end));
                    if (costModels==null||costModels.size()<1){
                        Toast.makeText(this, start+"至"+end+"期间内，无任何开支入账!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //获得成本类型
                    List<String> types=new ArrayList<>();
                    types.clear();
                    for(CostModel model:costModels){
                        //添加的时候，注意去除重复类型数据
                        if (!types.contains(model.getType())){
                            types.add(model.getType());
                        }
                    }
                    List<Float> typeCosts=new ArrayList<>();
                    typeCosts.clear();
                    //获得成本数据
                    for(int i=0;i<types.size();i++){
                        typeCosts.add(QueryTypeCost(types.get(i)));
                    }

                    PieData mPieData = ChartUtil.getPieData(types,typeCosts,this);
                    ChartUtil.showPieChart(mPieChart, mPieData,start+"至"+end+"期间内开支数据统计","各类开支百分比");
                    break;
                case 2:break;
                case 3:break;
                case 4:break;
            }

        }catch (Exception e){

        }
    }

    //获得每种类型的开支
    private  float QueryTypeCost(String type){
        float total=0;
        for(CostModel model:costModels){
            if (model.getType().equals(type)){
                total=total+Float.parseFloat(model.getMoney());
            }
        }
        return total;
    };

    //获得每天的收入
    private  float QueryDayIncome(String date){
        float total=0;
        for(OrderModel model:orderModels){
            if (model.getTime().contains(date)){
                total=total+Float.parseFloat(model.getTotal());
            }
        }
        return total;
    };


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
    public void onDataSettingInputComplete(String input_start, String input_end, int input_position) {
        start=input_start;
        end=input_end;
        position=input_position;
    }
}
