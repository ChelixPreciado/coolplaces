package interware.coolapp.ui.PlaceDetail;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import interware.coolapp.R;

/**
 * Created by chelixpreciado on 7/18/16.
 */
public class AddCommentFragmentDialog extends DialogFragment implements View.OnClickListener{

    private View rootView;
    private EditText edComment;
    private ViewGroup btnAcept, btnCancel;
    private TextView txtPlaceName;
    private String dialogTitle;
    private AddCommentFragmentDialogListener listener;

    private static final String placeNameTag = "PLACE_NAME";

    public AddCommentFragmentDialog() {
    }

    public static AddCommentFragmentDialog newInstance(String placeName){
        AddCommentFragmentDialog dialog = new AddCommentFragmentDialog();
        Bundle b = new Bundle();
        b.putString(placeNameTag, placeName);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Transparents);
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
            dialogTitle = getArguments().getString(placeNameTag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_add_comment, null);
        edComment = (EditText)rootView.findViewById(R.id.ed_comment);
        btnAcept = (ViewGroup)rootView.findViewById(R.id.btn_acept);
        btnCancel = (ViewGroup)rootView.findViewById(R.id.btn_cancel);
        txtPlaceName = (TextView)rootView.findViewById(R.id.txt_place_name);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPlaceName.setText(dialogTitle);
        btnAcept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_enterup_exitbotton);
    }

    public void setListener(AddCommentFragmentDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_acept:
                if (listener!=null)
                    listener.onCommented(edComment.getText().toString().trim());
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public interface AddCommentFragmentDialogListener{
        public void onCommented(String comment);
    }
}
