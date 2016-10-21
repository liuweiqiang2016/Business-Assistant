package myapp.alex.com.businessassistant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    TextView title,data_tv_income,data_tv_cost,data_tv_profit,data_tv_customer;

    private LineChart mLineChart;
    private PieChart mPieChart;
    private BarChart mBarChart_money,mBarChart_count;
    private LinearLayout mLinearLayout;
    //收入
    List<OrderModel> orderModels;
    //成本
    List<CostModel> costModels;
    //存放消费者消费的次数
    List<Float> counts=new ArrayList<>();
    //存放消费者消费的金额
    List<Float> totals=new ArrayList<>();
    //对所得的消费者信息进行排序
    int position=0;
    float max=0.0f;

    //初始条件
    String start = "", end = "";
//    int position=0;
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
        //初始化数据  2016-10-12至2016-10-12间的数据分析
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
        mLinearLayout= (LinearLayout) findViewById(R.id.data_lin);
        mBarChart_money= (BarChart) findViewById(R.id.data_barchart_money);
        mBarChart_count= (BarChart) findViewById(R.id.data_barchart_count);
        data_tv_income= (TextView) findViewById(R.id.data_tv_income);
        data_tv_cost= (TextView) findViewById(R.id.data_tv_cost);
        data_tv_customer= (TextView) findViewById(R.id.data_tv_customer);
        data_tv_profit= (TextView) findViewById(R.id.data_tv_profit);
        //默认为隐藏
        mLinearLayout.setVisibility(View.GONE);

    }

    //设置查询条件
    public void OnDataSetting(View view) {

//        CostSettingFragment fragment=CostSettingFragment.newInstance(start,end,position,new ArrayList<String>());
//        fragment.show(getFragmentManager(),"tag11");
        DataSettingFragment fragment=DataSettingFragment.newInstance(start,end);
        fragment.show(getFragmentManager(),"tag");
    }

    //处理查询
    public void OnDataQuery(View view) {
        if (start.equals("")||end.equals("")){
            FuncUtils.showToast(this,"请先设置查询条件!");
            return;
        }
        try{
            if (!FuncUtils.compareDate(start,end)&&(FuncUtils.daysBetween(start,end)>0)){
                //根据position，决定展示什么图形
//                showViewByPosition(position);
                showView();
            }else{
                FuncUtils.showToast(this, "开始时间需早于结束时间，请重新选择!");
            }
        }catch (Exception e){

        }

    }

    private void showView() {

        mLinearLayout.setVisibility(View.VISIBLE);

        try {
            //---------------------展示收入信息---------------
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
            float total_income=0;
            for (int i=0;i<size;i++){
                dayIncomes.add(QueryDayIncome(days.get(i)));
                total_income=total_income+QueryDayIncome(days.get(i));
            }
            data_tv_income.setText("本段时间内，收入总额为"+total_income+"元，其中每天的收入如下表所示:");
            if (total_income>0){
                LineData mLineData = ChartUtil.getLineData(days,dayIncomes,start+"至"+end+"期间收入数据统计");
                ChartUtil.showLineChart(mLineChart, mLineData, Color.rgb(114, 188, 223));

            }
            //------------------------展示支出信息-----------------
            //查询数据
            costModels=db.findAll(Selector.from(CostModel.class).where("C_Time",">",start).and("C_Time","<",end));
//            if (costModels==null||costModels.size()<1){
//                Toast.makeText(this, start+"至"+end+"期间内，无任何开支入账!", Toast.LENGTH_SHORT).show();
//                return;
//            }
            //获得成本类型
            List<String> types=new ArrayList<>();
            types.clear();
            if (costModels!=null){
                for(CostModel model:costModels){
                    //添加的时候，注意去除重复类型数据
                    if (!types.contains(model.getType())){
                        types.add(model.getType());
                    }
                }
            }
            List<Float> typeCosts=new ArrayList<>();
            typeCosts.clear();
            float total_cost=0;
            //获得成本数据
            for(int i=0;i<types.size();i++){
                typeCosts.add(QueryTypeCost(types.get(i)));
                total_cost=total_cost+QueryTypeCost(types.get(i));
            }
            data_tv_cost.setText("本段时间内，支出总额为"+total_cost+"元，其中各类支出如下表所示:");
            PieData mPieData = ChartUtil.getPieData(types,typeCosts,this);
            ChartUtil.showPieChart(mPieChart, mPieData,start+"至"+end+"期间开支数据统计","各类开支百分比");

            //---------------------展示利润信息-----------------
            data_tv_profit.setText("本段时间内，收入总额为"+total_income+"元，支出总额为"+total_cost+"元，净利润为"+FuncUtils.accuracyFloat(total_income,total_cost)+"元");

            //--------------------展示客户信息-----------------
            //存放所有消费者的名称
            List<String> names=new ArrayList<>();
            names.clear();
            if (orderModels!=null){
                for (int i = 0; i < orderModels.size(); i++) {
                    if (!names.contains(orderModels.get(i).getName())){
                        names.add(orderModels.get(i).getName());
                    }
                }
            }

            if (names.size()<1){
                data_tv_customer.setText("本段时间内，无任何客户消费");

            }else{
                //存放次数
                counts.clear();
                //存放金额
                totals.clear();

                for (int i = 0; i < names.size(); i++) {
                    QueryCostInfoByName(names.get(i));
                }
                sortList(counts);
                int count_position=position;
                float count_max=max;
                sortList(totals);
                int total_position=position;
                float total_max=max;
                data_tv_customer.setText("本段时间内，前来消费的用户共计"+orderModels.size()+"人次，其中消费总金额最高的是"+names.get(total_position)+"("+total_max+"元)，消费频率最多的是"+names.get(count_position)+"（"+count_max+"次），具体情况如下表所示:");

                //金额表 x轴名称，y轴金额
                BarData moneyData=ChartUtil.getBarData(names,totals,start+"至"+end+"期间客户消费金额数据统计");
                ChartUtil.showBarChart(mBarChart_money,moneyData);

                //次数表 x轴名称，y轴次数
                BarData countData=ChartUtil.getBarData(names,counts,start+"至"+end+"期间客户消费次数数据统计");
                ChartUtil.showBarChart(mBarChart_count,countData);

            }

        }catch (Exception e){

        }



    }

    //根据消费者名称，查找其消费信息
    void QueryCostInfoByName(String name){
        try {
            int count=0;
            float money=0;
            for (int i = 0; i < orderModels.size(); i++) {
                if (name.equals(orderModels.get(i).getName())){
                    count++;
                    money=money+Float.parseFloat(orderModels.get(i).getTotal());
                }
            }
            counts.add((float)count);
            totals.add(money);
        }catch (Exception e){

        }

    };

    //对队列进行排序，得到该数组的最大项，及最大项位置
    void sortList(List<Float> mList){
        position=0;
        max=mList.get(position);
        for (int i = 1; i < mList.size(); i++) {
            if (mList.get(i)>max){
                max=mList.get(i);
                position=i;
            }
            }
    };




//    private void showViewByPosition(int position) {
//        try {
//            switch (position){
//                //特定时间段的收入数据展示、折线图
//                case 0:
//                    //设置控件显示状态
//                    mLineChart.setVisibility(View.VISIBLE);
//                    mPieChart.setVisibility(View.GONE);
//                    //由数据库查询，修改为内存查询
//                    orderModels=db.findAll(Selector.from(OrderModel.class).where("C_Time",">",start+" 00:00:00").and("C_Time","<",end+" 00:00:00"));
//                    int size=FuncUtils.daysBetween(start,end);
//                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(sdf.parse(start));
//                    long time = cal.getTimeInMillis();
//                    //存放每天的日期
//                    List<String> days=new ArrayList<>();
//                    days.clear();
//                    for (int i=0;i<size;i++){
//                        //添加日期 1L防止数据溢出
//                        days.add(sdf.format((1L*i*(1000*3600*24)+time)));
//                    }
//                    //存放每天的收入总额
//                    List<Float> dayIncomes=new ArrayList<>();
//                    dayIncomes.clear();
//                    for (int i=0;i<size;i++){
//                        dayIncomes.add(QueryDayIncome(days.get(i)));
//                    }
//                    LineData mLineData = ChartUtil.getLineData(days,dayIncomes,start+"至"+end+"期间内收入数据统计");
//                    ChartUtil.showLineChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
//                    break;
//                //特定时间段内的开支数据展示、饼图
//                case 1:
//                    //设置控件显示状态
//                    mPieChart.setVisibility(View.VISIBLE);
//                    mLineChart.setVisibility(View.GONE);
//                    //查询数据
//                    costModels=db.findAll(Selector.from(CostModel.class).where("C_Time",">",start).and("C_Time","<",end));
//                    if (costModels==null||costModels.size()<1){
//                        FuncUtils.showToast(this, start+"至"+end+"期间内，无任何开支入账!");
//                        return;
//                    }
//                    //获得成本类型
//                    List<String> types=new ArrayList<>();
//                    types.clear();
//                    for(CostModel model:costModels){
//                        //添加的时候，注意去除重复类型数据
//                        if (!types.contains(model.getType())){
//                            types.add(model.getType());
//                        }
//                    }
//                    List<Float> typeCosts=new ArrayList<>();
//                    typeCosts.clear();
//                    //获得成本数据
//                    for(int i=0;i<types.size();i++){
//                        typeCosts.add(QueryTypeCost(types.get(i)));
//                    }
//
//                    PieData mPieData = ChartUtil.getPieData(types,typeCosts,this);
//                    ChartUtil.showPieChart(mPieChart, mPieData,start+"至"+end+"期间内开支数据统计","各类开支百分比");
//                    break;
//                case 2:break;
//                case 3:break;
//                case 4:break;
//            }
//
//        }catch (Exception e){
//
//        }
//    }

    //获得每种类型的开支
    private  float QueryTypeCost(String type){
        float total=0;
        if (costModels==null){
            total=0;
        }else {
            for(CostModel model:costModels){
                if (model.getType().equals(type)){
                    total=total+Float.parseFloat(model.getMoney());
                }
            }
        }
        return total;
    };

    //获得每天的收入
    private  float QueryDayIncome(String date){
        float total=0;
        if (orderModels==null){
            total=0;
        }else{
            for(OrderModel model:orderModels){
                if (model.getTime().contains(date)){
                    total=total+Float.parseFloat(model.getTotal());
                }
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
    public void onDataSettingInputComplete(String input_start, String input_end) {
        start=input_start;
        end=input_end;
//        position=input_position;
    }
}
