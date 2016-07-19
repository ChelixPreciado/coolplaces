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
import android.widget.ListView;

import java.util.ArrayList;

import interware.coolapp.R;
import interware.coolapp.adapters.CommentsAdapter;
import interware.coolapp.models.Comment;

/**
 * Created by chelixpreciado on 7/18/16.
 */
public class ShowCommentsFragment extends DialogFragment {

    private View rootView;
    private ListView lvComments;
    private ArrayList<Comment> comments;
    private CommentsAdapter commentsAdapter;

    private static final String commentsTag = "COMMENTS_TAG";

    public ShowCommentsFragment(){}

    public static ShowCommentsFragment newInstance(ArrayList<Comment> comments) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(commentsTag, comments);
        ShowCommentsFragment fragment = new ShowCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Transparents);
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
            comments = getArguments().getParcelableArrayList(commentsTag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_place_comments, null);
        lvComments = (ListView) rootView.findViewById(R.id.lv_comments);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentsAdapter = new CommentsAdapter(getActivity().getApplicationContext());
        commentsAdapter.addAll(comments);
        lvComments.setAdapter(commentsAdapter);
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
}
