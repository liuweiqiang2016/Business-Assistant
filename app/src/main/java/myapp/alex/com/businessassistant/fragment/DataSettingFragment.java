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
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;
import myapp.alex.com.businessassistant.utils.FuncUtils;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class DataSettingFragment extends DialogFragment {


    private EditText et_start, et_end;
    private String mStart,mEnd;
//    private Spinner spinner;
//    private int mPosition;
//    private ArrayList<String> mStringList;
    DateTimePickDialogUtil dialog;

//    private static final String TYPES = "param1";
    private static final String START = "param2";
    private static final String END = "param3";
//    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static DataSettingFragment newInstance(String mStart, String mEnd) {
        DataSettingFragment fragment = new DataSettingFragment();
        Bundle args = new Bundle();
        args.putString(START,mStart);
        args.putString(END, mEnd);
//        args.putInt(POSITION,mPosition);
//        args.putStringArrayList(TYPES,stringList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(START);
            mEnd = getArguments().getString(END);
//            mPosition=getArguments().getInt(POSITION);
//            mStringList=getArguments().getStringArrayList(TYPES);
        }
    }
    public DataSettingFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：开始时间、结束时间、选择类型
    public interface DataSettingInputListener
    {
        void onDataSettingInputComplete(String input_start, String input_end);
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
        View view = inflater.inflate(R.layout.fragment_datasetting_dialog, null);
        et_start = (EditText) view.findViewById(R.id.et_data_start);
        et_end = (EditText) view.findViewById(R.id.et_data_end);
//        spinner= (Spinner) view.findViewById(R.id.sp_data);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.sp_datas));
//        //adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        et_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = "";
                // 若EditText未设定日期，此时显示当前系统日期，否则显示EditText设定的日期
                if (et_start.getText().toString().equals("")) {

                    t1 = FuncUtils.getTime();
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

                    t2 = FuncUtils.getTime();
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
                                DataSettingInputListener listener= (DataSettingInputListener) getActivity();
                                String start= et_start.getText().toString();
                                String end= et_end.getText().toString();
                                if (start.equals("")){
                                    Toast.makeText(getActivity(),"开始时间不能为空！",Toast.LENGTH_SHORT).show();
                                    try
                                    {
                                        Field field = dialog.getClass()
                                                .getSuperclass().getDeclaredField(
                                                        "mShowing" );
                                        field.setAccessible( true );
                                        // 将mShowing变量设为false，表示对话框已关闭
                                        field.set(dialog, false );
                                        dialog.dismiss();
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }else {
                                    if (end.equals("")){
                                        Toast.makeText(getActivity(),"结束时间不能为空！",Toast.LENGTH_SHORT).show();

                                        try
                                        {
                                            Field field = dialog.getClass()
                                                    .getSuperclass().getDeclaredField(
                                                            "mShowing" );
                                            field.setAccessible( true );
                                            // 将mShowing变量设为false，表示对话框已关闭
                                            field.set(dialog, false );
                                            dialog.dismiss();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        listener.onDataSettingInputComplete(start,end);
                                        try
                                        {
                                            Field field = dialog.getClass()
                                                    .getSuperclass().getDeclaredField(
                                                            "mShowing" );
                                            field.setAccessible( true );
                                            // 将mShowing变量设为false，表示对话框已关闭
                                            field.set(dialog, true );
                                            dialog.dismiss();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    Field field = dialog.getClass()
                            .getSuperclass().getDeclaredField(
                                    "mShowing" );
                    field.setAccessible( true );
                    // 将mShowing变量设为false，表示对话框已关闭
                    field.set(dialog, true );
                    dialog.dismiss();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return builder.create();

    }

    //赋值数据
    void initData(){

        et_start.setText(mStart);
        et_end.setText(mEnd);
//        spinner.setSelection(mPosition);

    };

}
