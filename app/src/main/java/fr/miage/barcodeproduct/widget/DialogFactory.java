package fr.miage.barcodeproduct.widget;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class DialogFactory {
    public interface DialogCallBack<T> {
        void callBackCall(T result);
    }

    public static void showInputDialog(Context context, String dialogTitle, int inputType,
                                       final DialogCallBack<String> dialogCallBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);

        final EditText input = new EditText(context);
        input.setInputType(inputType);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                dialogCallBack.callBackCall(text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
