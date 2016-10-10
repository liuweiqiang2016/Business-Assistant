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
import myapp.alex.com.businessassistant.model.CustomerModel;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class CustomerDetailFragment extends DialogFragment {


    TextView tv_name,tv_id,tv_source,tv_tel,tv_adds,tv_info;
    String name,id,source,tel,adds,info;

    private static final String NAME = "param1";
    private static final String C_ID = "param2";
    private static final String ADDS = "param3";
    private static final String TEL = "param4";
    private static final String SOURCE = "param5";
    private static final String INFO = "param6";

    private CostModel costModel;

    // TODO: Rename and change types and number of parameters
    public static CustomerDetailFragment newInstance(CustomerModel model) {
        CustomerDetailFragment fragment = new CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putString(NAME,model.getName());
        args.putString(C_ID,model.getOrder_id());
        args.putString(ADDS,model.getAdds());
        args.putString(TEL,model.getTel());
        args.putString(SOURCE,model.getSource());
        args.putString(INFO,model.getInfo());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(C_ID);
            name = getArguments().getString(NAME);
            tel = getArguments().getString(TEL);
            adds = getArguments().getString(ADDS);
            info = getArguments().getString(INFO);
            source = getArguments().getString(SOURCE);
        }
    }
    public CustomerDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_customer_detail_dialog, null);
        tv_id= (TextView) view.findViewById(R.id.tv_customerdetail_id);
        tv_name= (TextView) view.findViewById(R.id.tv_customerdetail_name);
        tv_info= (TextView) view.findViewById(R.id.tv_customerdetail_info);
        tv_tel= (TextView) view.findViewById(R.id.tv_customerdetail_tel);
        tv_adds= (TextView) view.findViewById(R.id.tv_customerdetail_adds);
        tv_source= (TextView) view.findViewById(R.id.tv_customerdetail_source);
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

        tv_id.setText("客户编号:"+id);
        tv_name.setText("客户名称:"+name);
        tv_tel.setText("联系方式:"+tel);
        tv_adds.setText("客户住址:"+adds);
        if (source.equals("0")){
            tv_source.setText("数据来源:订单生成");
        }
        if (source.equals("1")){
            tv_source.setText("数据来源:自主添加");
        }
        tv_info.setText("备注信息:"+info);
    };

}
