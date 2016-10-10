package myapp.alex.com.businessassistant.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.CostModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class CostDetailFragment extends DialogFragment {


    TextView tv_name,tv_id,tv_type,tv_time,tv_money,tv_info;
    String name,id,type,time,money,info;

    private static final String NAME = "param1";
    private static final String C_ID = "param2";
    private static final String MONEY = "param3";
    private static final String TIME = "param4";
    private static final String TYPE = "param5";
    private static final String INFO = "param6";

    private CostModel costModel;

    // TODO: Rename and change types and number of parameters
    public static CostDetailFragment newInstance(CostModel model) {
        CostDetailFragment fragment = new CostDetailFragment();
        Bundle args = new Bundle();
        args.putString(NAME,model.getName());
        args.putString(C_ID,model.getCid());
        args.putString(MONEY,model.getMoney());
        args.putString(TIME,model.getTime());
        args.putString(TYPE,model.getType());
        args.putString(INFO,model.getInfo());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(C_ID);
            type = getArguments().getString(TYPE);
            time = getArguments().getString(TIME);
            name = getArguments().getString(NAME);
            money = getArguments().getString(MONEY);
            info = getArguments().getString(INFO);
        }
    }
    public CostDetailFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去除最上方标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        // Get the layout inflateri
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_cost_detail_dialog, null);
        tv_id= (TextView) view.findViewById(R.id.tv_costdetail_id);
        tv_type= (TextView) view.findViewById(R.id.tv_costdetail_type);
        tv_time= (TextView) view.findViewById(R.id.tv_costdetail_time);
        tv_name= (TextView) view.findViewById(R.id.tv_costdetail_name);
        tv_money= (TextView) view.findViewById(R.id.tv_costdetail_money);
        tv_info= (TextView) view.findViewById(R.id.tv_costdetail_info);
        initData();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                }

                        });
        return builder.create();

    }

    //赋值数据
    void initData(){

        tv_id.setText("开销编号:"+id);
        tv_type.setText("开销类型:"+type);
        tv_time.setText("开销时间:"+time);
        tv_name.setText("开销名称:"+name);
        tv_money.setText("开销金额:"+money+"元");
        tv_info.setText("备注信息:"+info);
    };

}
