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
import android.widget.TextView;
import android.widget.Toast;

public class VatDialog extends DialogFragment {

    private VatDialogListener mVatDialogListener;

    public VatDialog(){ }

    public interface VatDialogListener{
        void OnDialogVatUpdateHandler(int newVat);
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

        TextView currentVat = rootview.findViewById(R.id.tv_current_vat_rate);
        Button confirm = rootview.findViewById(R.id.btn_conifrm);
        final EditText vatInput = rootview.findViewById(R.id.et_vat_input);

        int vat = PreferencesUtil.getVatRate(getContext());

        currentVat.setText(String.format("%s %d", getString(R.string.current_vat_text), vat));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check to ensure input is not blank
                if(vatInput.getText().toString().length() == 0){
                    Toast.makeText(getContext(), R.string.invalid_vat_number_text, Toast.LENGTH_SHORT).show();
                }
                else {
                    int newVat = Integer.parseInt(vatInput.getText().toString());
                    mVatDialogListener.OnDialogVatUpdateHandler(newVat);
                    dismiss();
                }
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
