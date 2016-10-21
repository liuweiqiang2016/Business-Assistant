package myapp.alex.com.businessassistant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;
import com.zeone.framework.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.activity.EditProjectActivity;
import myapp.alex.com.businessassistant.activity.EditServiceActivity;
import myapp.alex.com.businessassistant.adapter.EditServiceAdapter;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.DividerItemDecoration;
import myapp.alex.com.businessassistant.utils.FuncUtils;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static Context mContext;
    private  static List<ServiceModel> mList;
    //存放所有的服务名称
    ArrayList<String> names=new ArrayList<>();
    private  static DbUtils db;
    private RecyclerView rv;
    EditServiceAdapter adapter;

    public ServiceFragment() {
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
    public static ServiceFragment newInstance(Context context,DbUtils mdb) {
        ServiceFragment fragment = new ServiceFragment();
        mContext=context;
        db=mdb;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface ServiceDoneListener
    {
        void onServiceDone();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_project_service, container, false);
        //初始化数据
        initData();
        //初始化布局
        initView(view);
        //绑定事件
        initEvent();
        return view;
    }

     void initData() {
        mList=db.findAll(Selector.from(ServiceModel.class).where("C_Show","=","1"));
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
        adapter=new EditServiceAdapter(mContext,mList);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initEvent() {
        adapter.setOnItemClickLitener(new EditServiceAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //如果是特殊项目：其他 不可编辑
                if(FuncUtils.SPECIAL_SERVICE_NAME.equals(mList.get(position).getName())){
                    FuncUtils.showToast(mContext,FuncUtils.SPECIAL_SERVICE_NAME+"为特殊项目,不可编辑!");
                    return;
                }
                //编辑服务项
                EditServiceFragment fragment= EditServiceFragment.newInstance(mList.get(position).getName(),mList.get(position).getC_id()+"",mList.get(position).getPrice()+"",position);
                fragment.show(getActivity().getFragmentManager(),"tag");

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //如果是特殊项目：其他 不可编辑
                if(FuncUtils.SPECIAL_SERVICE_NAME.equals(mList.get(position).getName())){
                    FuncUtils.showToast(mContext,FuncUtils.SPECIAL_SERVICE_NAME+"为特殊项目,不可删除!");
                    return;
                }
                //删除服务项
                DeleteServiceFragment fragment=DeleteServiceFragment.newInstance(mList.get(position).getName(),mList.get(position).getC_id()+"",mList.get(position).getPrice()+"",position);
                fragment.show(getActivity().getFragmentManager(),"tag");

            }
        });

    }


    //删除一个服务
    public void setDeleteServiceComplete(int position) {
        ServiceModel model=mList.get(position);
        if (model!=null){
            try {
                ServiceModel temp=new ServiceModel();
                temp.setC_id(model.getC_id());
                temp.setName(model.getName());
                temp.setShow("0");
                temp.setPrice(model.getPrice());
                db.delete(ServiceModel.class, WhereBuilder.b("C_ID","=",model.getC_id()));
                db.save(temp);
                //更新列表
                adapter.removeData(position);
                names.remove(position);
                FuncUtils.showToast(mContext,"删除服务项成功!");
                //完成后,通知activity，更新界面
                ServiceDoneListener listener= (ServiceDoneListener) getActivity();
                listener.onServiceDone();

            }catch (Exception e){
//                Log.e(TAG, "onDeleteServiceComplete: "+e.getMessage());
            };
        }

    }

    //编辑一个服务
    public void setEditPriceComplete(String edit_price, int position){
        //处理Fragment单价输入按完成后，处理更新一个项目
        ServiceModel model=mList.get(position);
        if (model!=null){
            try {
                model.setPrice(Float.parseFloat(edit_price));
                db.update(model);
                //更新列表
                adapter.notifyItemChanged(position,model);
                FuncUtils.showToast(mContext,"修改服务项成功!");
                //完成后,通知activity，更新界面
                ServiceDoneListener listener= (ServiceDoneListener) getActivity();
                listener.onServiceDone();
            }catch (Exception e){

            };
        }
    }


    //弹出新增项目dialog
    public void AddService() {
        //增加服务项
        List<ServiceModel> tempList= db.findAll(Selector.from(ServiceModel.class));
        AddServiceFragment fragment=AddServiceFragment.newInstance(tempList.size()+"",names);
        fragment.show(getActivity().getFragmentManager(),"tag");
    }

    //新增一个服务
    public void setAddServiceInputComplete(String name, String price, String c_id) {
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
            FuncUtils.showToast(mContext,"新增服务项成功!");
            //完成后,通知activity，更新界面
            ServiceDoneListener listener= (ServiceDoneListener) getActivity();
            listener.onServiceDone();
        }catch (Exception e){

        };
    }
}
