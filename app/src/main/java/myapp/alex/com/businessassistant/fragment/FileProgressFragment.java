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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.VersionInfoModel;
import myapp.alex.com.businessassistant.progress.NumberProgressBar;

/**
 * Created by liuweiqiang on 2016-10-18 10:39:06.
 * 版本升级
 */
public class FileProgressFragment extends DialogFragment {


    private TextView title;
    private NumberProgressBar bar;
//    int type;
    String head;
    String foot;
//    private static final String TYPE = "param1";
    private static final String HEAD = "param2";
    private static final String FOOT = "param3";

    // TODO: Rename and change types and number of parameters
    public static FileProgressFragment newInstance(String head,String foot) {
        FileProgressFragment fragment = new FileProgressFragment();
        Bundle args = new Bundle();
//        args.putInt(TYPE,type);
        args.putString(HEAD,head);
        args.putString(FOOT,foot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            type = getArguments().getInt(TYPE);
            head=getArguments().getString(HEAD);
            foot=getArguments().getString(FOOT);
        }
    }
    public FileProgressFragment() {
    }
//    //回调函数：回调处理下载 参数为下载地址link
//    public interface SoftUpdateListener
//    {
//        void onSoftUpdate(String link);
//    }
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
        View view = inflater.inflate(R.layout.fragment_progress_dialog,null);
        title= (TextView) view.findViewById(R.id.progress_title);
        bar= (NumberProgressBar) view.findViewById(R.id.progress_bar);
        initData();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(foot,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
//                                SoftUpdateListener listener= (SoftUpdateListener) getActivity();
//                                listener.onSoftUpdate(versionInfoModel.getLink());

                            }

                        });
        return builder.create();

    }

    //赋值数据
    void initData(){
        title.setText(head);
    };

   public void setProgress(int progress){

        bar.setProgress(progress);

    }

}
