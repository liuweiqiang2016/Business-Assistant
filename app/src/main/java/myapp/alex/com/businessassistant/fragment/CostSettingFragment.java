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
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class CostSettingFragment extends DialogFragment {


    private EditText et_start, et_end;
    private String mStart,mEnd;
    private Spinner spinner;
    private int mPosition;
    private ArrayList<String> mStringList;
    DateTimePickDialogUtil dialog;

    private static final String TYPES = "param1";
    private static final String START = "param2";
    private static final String END = "param3";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static CostSettingFragment newInstance(String mStart, String mEnd, int mPosition,ArrayList<String> stringList) {
        CostSettingFragment fragment = new CostSettingFragment();
        Bundle args = new Bundle();
        args.putString(START,mStart);
        args.putString(END, mEnd);
        args.putInt(POSITION,mPosition);
        args.putStringArrayList(TYPES,stringList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(START);
            mEnd = getArguments().getString(END);
            mPosition=getArguments().getInt(POSITION);
            mStringList=getArguments().getStringArrayList(TYPES);
        }
    }
    public CostSettingFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface CostSettingInputListener
    {
        void onCostSettingInputComplete(String input_start, String input_end, int input_position,String type);
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
        View view = inflater.inflate(R.layout.fragment_costsetting_dialog, null);
        et_start = (EditText) view.findViewById(R.id.et_cost_start);
        et_end = (EditText) view.findViewById(R.id.et_cost_end);
        spinner= (Spinner) view.findViewById(R.id.sp_cost);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mStringList);
        //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        et_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = "";
                // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
                if (et_start.getText().toString().equals("")) {

                    t1 = FuncUtils.getDate();
                } else {
                    t1 = et_start.getText().toString();
                }
                if (dialog==null){
                    dialog=new DateTimePickDialogUtil(getActivity(),false);
                }
                dialog.dateTimePicKDialog(et_start,t1);
            }
        });

        et_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t2 = "";
                // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
                if (et_end.getText().toString().equals("")) {

                    t2 = FuncUtils.getDate();
                } else {
                    t2 = et_end.getText().toString();
                }
                if (dialog==null){
                    dialog=new DateTimePickDialogUtil(getActivity(),false);
                }
                dialog.dateTimePicKDialog(et_end,t2);
            }
        });

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
                                CostSettingInputListener listener= (CostSettingInputListener) getActivity();
                                String start= et_start.getText().toString();
                                String end= et_end.getText().toString();
                                listener.onCostSettingInputComplete(start,end,spinner.getSelectedItemPosition(),spinner.getSelectedItem().toString());
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

        et_start.setText(mStart);
        et_end.setText(mEnd);
        spinner.setSelection(mPosition);

    };

}
