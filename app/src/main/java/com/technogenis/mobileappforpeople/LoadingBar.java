package com.technogenis.mobileappforpeople;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;


public class LoadingBar {
    Context context;
    Dialog dialog;

    public LoadingBar(Context context) {
        this.context = context;
    }

    public void ShowDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView dialog_title_text = dialog.findViewById(R.id.dialog_title_text);
        dialog_title_text.setText(title);
        dialog.create();
        dialog.show();
    }

    public void HideDialog()
    {
        dialog.dismiss();
    }
}
