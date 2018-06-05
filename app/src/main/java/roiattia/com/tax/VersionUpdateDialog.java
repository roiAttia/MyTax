package roiattia.com.tax;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VersionUpdateDialog extends DialogFragment {

    public VersionUpdateDialog(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialog_version_update_message, container, false);
        TextView message = rootview.findViewById(R.id.tv_message);
        Button close = rootview.findViewById(R.id.btn_close);
        Button toStore = rootview.findViewById(R.id.btn_to_store);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        toStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.roi.tax"));
                startActivity(i);
            }
        });

        message.setText(Html.fromHtml(getString(R.string.dialog_version_update_message).
                replaceAll("(\\n)", "<br />")));
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferencesUtil.setFirstUpdate(getContext());
    }
}
