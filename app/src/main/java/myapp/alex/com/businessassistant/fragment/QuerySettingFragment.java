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

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class QuerySettingFragment extends DialogFragment {


    private EditText et_query_name,et_query_start,et_query_end;
    private String mName,mStart,mEnd;
    private Spinner spinner;
    private int mPosition;
    DateTimePickDialogUtil dialog;

    private static final String NAME = "param1";
    private static final String START = "param2";
    private static final String END = "param3";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static QuerySettingFragment newInstance(String mName, String mStart, String mEnd,int mPosition) {
        QuerySettingFragment fragment = new QuerySettingFragment();
        Bundle args = new Bundle();
        args.putString(NAME,mName);
        args.putString(START,mStart);
        args.putString(END, mEnd);
        args.putInt(POSITION,mPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
            mStart = getArguments().getString(START);
            mEnd = getArguments().getString(END);
            mPosition=getArguments().getInt(POSITION);
        }
    }
    public QuerySettingFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface SettingInputListener
    {
        void onSettingInputComplete(String input_name,String input_start,String input_end,int input_position);
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
        View view = inflater.inflate(R.layout.fragment_query_dialog, null);
        et_query_name= (EditText) view.findViewById(R.id.et_query_name);
        et_query_start= (EditText) view.findViewById(R.id.et_query_start);
        et_query_end= (EditText) view.findViewById(R.id.et_query_end);
        spinner= (Spinner) view.findViewById(R.id.sp_setting);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.sp_states));
        //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        et_query_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = "";
                // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
                if (et_query_start.getText().toString().equals("")) {

                    t1 = FuncUtils.getTime();
                } else {
                    t1 = et_query_start.getText().toString();
                }
                if (dialog==null){
//                    dialog=DateTimePickDialogUtil.getInstance(getActivity(),true);
                    dialog=new DateTimePickDialogUtil(getActivity(),true);
                }
                dialog.dateTimePicKDialog(et_query_start,t1);
//                DateTimePickDialogUtil dialog1 = DateTimePickDialogUtil.getInstance(getActivity(),t1,true);
//                dialog1.dateTimePicKDialog(et_query_start);
            }
        });

        et_query_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t2 = "";
                // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
                if (et_query_end.getText().toString().equals("")) {

                    t2 = FuncUtils.getTime();
                } else {
                    t2 = et_query_end.getText().toString();
                }
                if (dialog==null){
//                    dialog=DateTimePickDialogUtil.getInstance(getActivity(),true);
                    dialog=new DateTimePickDialogUtil(getActivity(),true);
                }
                dialog.dateTimePicKDialog(et_query_end,t2);
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
                                SettingInputListener listener= (SettingInputListener) getActivity();
                                String name=et_query_name.getText().toString();
                                String start=et_query_start.getText().toString();
                                String end=et_query_end.getText().toString();
                                listener.onSettingInputComplete(name,start,end,spinner.getSelectedItemPosition());
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

        et_query_name.setText(mName);
        et_query_start.setText(mStart);
        et_query_end.setText(mEnd);
        spinner.setSelection(mPosition);

    };

}
