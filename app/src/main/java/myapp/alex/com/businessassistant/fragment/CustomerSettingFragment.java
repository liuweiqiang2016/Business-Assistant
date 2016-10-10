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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class CustomerSettingFragment extends DialogFragment {


    private EditText et_name;
    private String name;
    private Spinner spinner;
    private int mPosition;

    private static final String NAME = "param3";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static CustomerSettingFragment newInstance(String name,int mPosition) {
        CustomerSettingFragment fragment = new CustomerSettingFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putInt(POSITION,mPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
            mPosition=getArguments().getInt(POSITION);
        }
    }
    public CustomerSettingFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface CustomerSettingInputListener
    {
        void onCustomersSettingInputComplete(String input_name,int input_position);
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
        View view = inflater.inflate(R.layout.fragment_customersetting_dialog, null);
        et_name= (EditText) view.findViewById(R.id.et_customer_name_dialog);
        spinner= (Spinner) view.findViewById(R.id.sp_customer);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.sp_source));
        //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
                                CustomerSettingInputListener listener= (CustomerSettingInputListener) getActivity();
                                listener.onCustomersSettingInputComplete(et_name.getText().toString().trim(),spinner.getSelectedItemPosition());
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();

    }

    //赋值数据
    void initData(){

        et_name.setText(name);
        spinner.setSelection(mPosition);

    };

}
