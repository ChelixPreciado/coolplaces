package interware.coolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import interware.coolapp.R;

/**
 * Created by chelixpreciado on 7/18/16.
 */
public class CommentItemHolder {

    private View base;
    private TextView txtUserMail, txtUserComment;

    public CommentItemHolder(View base) {
        this.base = base;
    }

    public TextView getTxtUserMail() {
        if (txtUserMail==null)
            txtUserMail = (TextView)base.findViewById(R.id.txt_user_mail);
        return txtUserMail;
    }

    public TextView getTxtUserComment() {
        if (txtUserComment==null)
            txtUserComment = (TextView)base.findViewById(R.id.txt_user_comment);
        return txtUserComment;
    }
}
