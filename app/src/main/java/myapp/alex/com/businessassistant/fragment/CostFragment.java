package myapp.alex.com.businessassistant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import com.zeone.framework.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.adapter.EditCostAdapter;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;

public class CostFragment extends Fragment {

    private static Context mContext;
    private  static List<CostTypeModel> mList;
    //存放所有的服务名称
    ArrayList<String> names=new ArrayList<>();
    private  static DbUtils db;
    private RecyclerView rv;
    EditCostAdapter adapter;

    public CostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param context Parameter 1.
     * @param mdb Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CostFragment newInstance(Context context, DbUtils mdb) {
        CostFragment fragment = new CostFragment();
        mContext=context;
        db=mdb;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface CostDoneListener
    {
        void onCostDone();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_project_cost, container, false);
        //初始化数据
        initData();
        //初始化布局
        initView(view);
        //绑定事件
        initEvent();
        return view;
    }

     void initData() {
        mList=db.findAll(Selector.from(CostTypeModel.class).where("C_Show","=","1"));
        //存放服务名称
        names.clear();
        if(mList!=null){
            for(int i=0;i<mList.size();i++){
                names.add(mList.get(i).getName());
            }
        }
    }

    void initView(View view){

        rv= (RecyclerView) view.findViewById(R.id.rv_edit);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        if (mList==null)
        {mList=new ArrayList<>();}
        adapter=new EditCostAdapter(mContext,mList);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    //增加一个标识，去除长按后，执行点击的事件
    boolean pushLong=false;
    private void initEvent() {
        adapter.setOnItemClickLitener(new EditCostAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (pushLong){
                    pushLong=false;
                    return;
                }
                //不可编辑
                FuncUtils.showToast(mContext,"开销项目不可编辑，请执行添加或删除操作!");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //如果是特殊项目：其他 不可删除
                if(FuncUtils.SPECIAL_SERVICE_NAME.equals(mList.get(position).getName())){
                    FuncUtils.showToast(mContext,FuncUtils.SPECIAL_SERVICE_NAME+"为特殊项目,不可删除!");
                    pushLong=true;
                    return;
                }
                //删除服务项
                DeleteCostFragment fragment=DeleteCostFragment.newInstance(mList.get(position).getName(),mList.get(position).getC_id()+"",position);
                fragment.show(getActivity().getFragmentManager(),"tag");

            }
        });

    }


    //删除一个开销
    public void setDeleteCostComplete(int position) {
        CostTypeModel model=mList.get(position);
        if (model!=null){
            try {
                CostTypeModel temp=new CostTypeModel();
                temp.setC_id(model.getC_id());
                temp.setName(model.getName());
                temp.setShow("0");
                db.delete(CostTypeModel.class, WhereBuilder.b("C_ID","=",model.getC_id()));
                db.save(temp);
                //更新列表
                adapter.removeData(position);
                names.remove(position);
                FuncUtils.showToast(mContext,"删除开销项成功!");
                //完成后,通知activity，更新界面
                CostDoneListener listener= (CostDoneListener) getActivity();
                listener.onCostDone();

            }catch (Exception e){
//                Log.e(TAG, "onDeleteServiceComplete: "+e.getMessage());
            };
        }

    }

    //弹出新增开销dialog
    public void AddCost() {
        //增加服务项
        List<CostTypeModel> tempList= db.findAll(Selector.from(CostTypeModel.class));
        AddCostFragment fragment=AddCostFragment.newInstance(tempList.size()+"",names);
        fragment.show(getActivity().getFragmentManager(),"tag");
    }

    //新增一个开销
    public void setAddCostComplete(String name,String c_id) {
        //处理新增一个项目
        CostTypeModel model=new CostTypeModel();
        model.setName(name);
        try {
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
            FuncUtils.showToast(mContext,"新增开销项成功!");
            //完成后,通知activity，更新界面
            CostDoneListener listener= (CostDoneListener) getActivity();
            listener.onCostDone();
        }catch (Exception e){

        };
    }
}
