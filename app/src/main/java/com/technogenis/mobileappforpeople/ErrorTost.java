package com.technogenis.mobileappforpeople;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class ErrorTost {
    Context context;
    Animation slideInBottom,slideOutBottom;
    Dialog dialog;

    public ErrorTost(Context context) {
        this.context = context;
    }

    public void showErrorMessage(String message)
    {
       slideInBottom = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
       slideOutBottom = AnimationUtils.loadAnimation(context,R.anim.slide_out_bottom);


        dialog = new Dialog(context);
        dialog.setContentView(R.layout.error_toast_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView error_text = dialog.findViewById(R.id.error_text);
        error_text.setText(message);
        dialog.create();
        dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HideDialog();
            }
        },2000);

    }

    public void HideDialog()
    {
        dialog.dismiss();
    }

}


