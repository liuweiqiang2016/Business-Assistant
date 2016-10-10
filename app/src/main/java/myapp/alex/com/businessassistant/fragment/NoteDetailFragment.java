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
import myapp.alex.com.businessassistant.model.NoteModel;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class NoteDetailFragment extends DialogFragment {


    TextView tv_time,tv_subject,tv_body;
    String time,subject,body;

    private static final String TIME = "param1";
    private static final String SUBJECT = "param2";
    private static final String BODY = "param3";

    // TODO: Rename and change types and number of parameters
    public static NoteDetailFragment newInstance(NoteModel model) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putString(TIME,model.getTime());
        args.putString(SUBJECT,model.getSubject());
        args.putString(BODY,model.getBody());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getString(TIME);
            body = getArguments().getString(BODY);
            subject = getArguments().getString(SUBJECT);
        }
    }
    public NoteDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_note_detail_dialog, null);
        tv_time= (TextView) view.findViewById(R.id.tv_notedetail_time);
        tv_subject= (TextView) view.findViewById(R.id.tv_notedetail_subject);
        tv_body= (TextView) view.findViewById(R.id.tv_notedetail_body);
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

        tv_time.setText("编辑时间:"+time);
        tv_subject.setText("笔记主题:"+subject);
        tv_body.setText("笔记内容:"+body);
    };

}
