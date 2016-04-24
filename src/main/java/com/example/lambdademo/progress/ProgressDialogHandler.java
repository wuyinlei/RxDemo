package com.example.lambdademo.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.example.lambdademo.listener.ProgressCancelListener;


public class ProgressDialogHandler extends Handler {

    /*显示Dialog*/
    public static final int SHOW_PROGRESS_DIALOG = 1;

    /*隐藏Dialog*/
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    /**
     *
     * @param context   上下文
     * @param mProgressCancelListener    dialog取消监听事件
     * @param cancelable   是否可以取消
     */
    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    /**
     * 初始化dialog
     */
    private void initProgressDialog(){
        if (pd == null) {
            pd = new ProgressDialog(context);

            //设置是否可以取消
            pd.setCancelable(cancelable);

            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    /**
     * 隐藏dialog
     */
    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
