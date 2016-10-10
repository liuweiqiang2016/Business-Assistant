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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.DateTimePickDialogUtil;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class AddServiceFragment extends DialogFragment {


    private EditText et_addservice_name,et_addservice_price;
    private TextView tv_addservice_id;
    private String mId;
    private ArrayList<String> mNames;

    private static final String MID = "param1";
    private static final String MNAMES = "param2";
    // TODO: Rename and change types and number of parameters
    public static AddServiceFragment newInstance(String mId,ArrayList<String> mNames) {
        AddServiceFragment fragment = new AddServiceFragment();
        Bundle args = new Bundle();
        args.putString(MID,mId);
        args.putStringArrayList(MNAMES,mNames);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(MID);
            mNames=getArguments().getStringArrayList(MNAMES);
        }
    }
    public AddServiceFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface AddServiceListener
    {
        void onAddServiceInputComplete(String name, String price, String c_id);
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
        View view = inflater.inflate(R.layout.fragment_addservice_dialog, null);
        et_addservice_name= (EditText) view.findViewById(R.id.et_addservice_name);
        et_addservice_price= (EditText) view.findViewById(R.id.et_addservice_price);
        tv_addservice_id= (TextView) view.findViewById(R.id.tv_addservice_id);

        tv_addservice_id.setText(mId);
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
                                if (et_addservice_name.getText().toString().trim().equals("")){
                                    Toast.makeText(getActivity(),"服务名称不能为空！",Toast.LENGTH_SHORT).show();
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

                                    if (mNames.contains(et_addservice_name.getText().toString().trim())){
                                        Toast.makeText(getActivity(),"服务名称已存在！",Toast.LENGTH_SHORT).show();
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
                                        if (et_addservice_price.getText().toString().trim().equals("")){
                                            Toast.makeText(getActivity(),"项目单价不能为空！",Toast.LENGTH_SHORT).show();
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

                                            AddServiceListener listener= (AddServiceListener) getActivity();
                                            listener.onAddServiceInputComplete(et_addservice_name.getText().toString().trim(),et_addservice_price.getText().toString().trim(),mId);
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
//    void initData(){
//
//        et_query_name.setText(mName);
//        et_query_start.setText(mStart);
//        et_query_end.setText(mEnd);
//
//    };


}
