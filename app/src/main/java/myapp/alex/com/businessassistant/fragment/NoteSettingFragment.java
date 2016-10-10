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
public class NoteSettingFragment extends DialogFragment {


    private EditText et_start, et_end,et_subject;
    private String mStart,mEnd,mSubject;
    DateTimePickDialogUtil dialog;

    private static final String SUBJECT = "param1";
    private static final String START = "param2";
    private static final String END = "param3";


    // TODO: Rename and change types and number of parameters
    public static NoteSettingFragment newInstance(String mSubject,String mStart, String mEnd) {
        NoteSettingFragment fragment = new NoteSettingFragment();
        Bundle args = new Bundle();
        args.putString(START,mStart);
        args.putString(END, mEnd);
        args.putString(SUBJECT,mSubject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(START);
            mEnd = getArguments().getString(END);
            mSubject=getArguments().getString(SUBJECT);
        }
    }
    public NoteSettingFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface NoteSettingInputListener
    {
        void onNoteSettingInputComplete(String input_subject,String input_start, String input_end);
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
        View view = inflater.inflate(R.layout.fragment_notesetting_dialog, null);
        et_start = (EditText) view.findViewById(R.id.et_note_start);
        et_end = (EditText) view.findViewById(R.id.et_note_end);
        et_subject=(EditText) view.findViewById(R.id.et_subject);

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
                    dialog=new DateTimePickDialogUtil(getActivity(),true);
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
                    dialog=new DateTimePickDialogUtil(getActivity(),true);
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
                                NoteSettingInputListener listener= (NoteSettingInputListener) getActivity();
                                String str1=et_subject.getText().toString().trim();
                                String str2=et_start.getText().toString();
                                String str3=et_end.getText().toString();
                                listener.onNoteSettingInputComplete(str1,str2,str3);
//                                CostSettingInputListener listener= (CostSettingInputListener) getActivity();
//                                String start= et_start.getText().toString();
//                                String end= et_end.getText().toString();
//                                listener.onCostSettingInputComplete(start,end,spinner.getSelectedItemPosition(),spinner.getSelectedItem().toString());
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
        et_subject.setText(mSubject);
//        spinner.setSelection(mPosition);

    };

}
