package roiattia.com.newtaxapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class VatDialog extends DialogFragment {

    private VatDialogListener mVatDialogListener;

    public VatDialog(){ }

    public interface VatDialogListener{
        void OnUpdateHandler(int newVat);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mVatDialogListener = (VatDialogListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialog_vat_update, container, false);

        Button confirm = rootview.findViewById(R.id.btn_conifrm);
        ImageView clearInput = rootview.findViewById(R.id.iv_clear_input);
        final EditText vatInput = rootview.findViewById(R.id.et_vat_input);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int vat = sharedPreferences.getInt(getString(R.string.shared_preferences_vat_key),
                getResources().getInteger(R.integer.vat_default));

        vatInput.setText(String.valueOf(vat));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Integer.parseInt(vatInput.getText().toString());
                    mVatDialogListener.OnUpdateHandler(Integer.parseInt(String.valueOf(vatInput.getText())));
                    dismiss();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Enter valid number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vatInput.setText("");
                vatInput.requestFocus();
            }
        });

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
}
