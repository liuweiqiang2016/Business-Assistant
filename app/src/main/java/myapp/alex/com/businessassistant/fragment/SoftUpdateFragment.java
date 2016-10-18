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
import myapp.alex.com.businessassistant.model.VersionInfoModel;

/**
 * Created by liuweiqiang on 2016-10-18 10:39:06.
 * 版本升级
 */
public class SoftUpdateFragment extends DialogFragment {


    private TextView old_code,new_code,name,size,des;
    private static VersionInfoModel versionInfoModel;

    // TODO: Rename and change types and number of parameters
    public static SoftUpdateFragment newInstance(VersionInfoModel model) {
        SoftUpdateFragment fragment = new SoftUpdateFragment();
        versionInfoModel=model;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public SoftUpdateFragment() {
    }
    //回调函数：回调处理下载 参数为下载地址link
    public interface SoftUpdateListener
    {
        void onSoftUpdate(String link);
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
        View view = inflater.inflate(R.layout.fragment_updateinfo_dialog, null);
        old_code= (TextView) view.findViewById(R.id.tv_update_old);
        new_code= (TextView) view.findViewById(R.id.tv_update_new);
        name= (TextView) view.findViewById(R.id.tv_update_name);
        size= (TextView) view.findViewById(R.id.tv_update_size);
        des= (TextView) view.findViewById(R.id.tv_update_des);

        initData();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("升级",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                SoftUpdateListener listener= (SoftUpdateListener) getActivity();
                                listener.onSoftUpdate(versionInfoModel.getLink());

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
        old_code.setText("当前版本:V"+versionInfoModel.getCode_old());
        new_code.setText("最新版本:V"+versionInfoModel.getCode());
        name.setText("应用名称:"+versionInfoModel.getName());
        size.setText("应用大小:"+versionInfoModel.getSize());
        des.setText("更新说明:"+versionInfoModel.getDes());
    };

}
