package com.dspread.demoui.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.LogFileConfig;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.TRACE;
import com.dspread.demoui.utils.TitleUpdateListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2023/9/12 16:24]
 * @updateRemark : [说明本次修改内容]
 */
public class LogsFragment extends Fragment {
    private TitleUpdateListener myListener;
    private TextView tv_log;
    private static LogFileConfig logFileConfig;
    private ProgressBar progress_loading;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (TitleUpdateListener) getActivity();
        myListener.sendValue(getString(R.string.show_log));
        TRACE.setContext(getContext());
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        tv_log = view.findViewById(R.id.tv_log);
        progress_loading = view.findViewById(R.id.progress_loading);
        logFileConfig = LogFileConfig.getInstance(getContext());
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        String log = logFileConfig.readLog();
        tv_log.setMovementMethod(ScrollingMovementMethod.getInstance());
        if(log != null && !"".equals(log)){
            tv_log.setText(log);
        }else {
            tv_log.setText("Empty logs");
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logs_upload,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logs_upload){
            EditText editText = new EditText(getContext());
            editText.setHint(getString(R.string.input_email));
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(getString(R.string.input_email));
            dialog.setView(editText);
            dialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String email = editText.getText().toString();
                    if("Empty logs".equals(tv_log.getText().toString())){
                        Toast.makeText(getContext(),"Empty logs",Toast.LENGTH_SHORT).show();
                    }else {
                        uploadLogs(email + " " + tv_log.getText().toString());
                    }
                }
            });
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadLogs(String logs){
        OkGo.post("https://gitlab.com/api/v4/projects/4128550/issues?title=Issues%20with%20android%20logs&labels=bug")
                .tag(this)
                .headers("PRIVATE-TOKEN","glpat-DvHTwzDMSBNwhEHbjxuz")
                .params("description",logs)
                .execute(new AbsCallback<Object>() {
                    @Override
                    public void onSuccess(Response<Object> response) {
                        progress_loading.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Upload logs successful!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public Object convertResponse(okhttp3.Response response) throws Throwable {
                        return null;
                    }

                    @Override
                    public void onStart(Request<Object, ? extends Request> request) {
                        super.onStart(request);
                        progress_loading.setVisibility(View.VISIBLE);
                    }
                });
    }
}
