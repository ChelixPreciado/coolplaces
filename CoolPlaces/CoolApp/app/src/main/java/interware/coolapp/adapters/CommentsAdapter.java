package interware.coolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import interware.coolapp.R;
import interware.coolapp.ViewHolders.CommentItemHolder;
import interware.coolapp.models.Comment;

/**
 * Created by chelixpreciado on 7/18/16.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {

    public CommentsAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment tComment = getItem(position);
        CommentItemHolder holder;

        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_comment, null);
            holder = new CommentItemHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (CommentItemHolder)convertView.getTag();
        }

        holder.getTxtUserMail().setText(tComment.getUser());
        holder.getTxtUserComment().setText(tComment.getText());

        return convertView;
    }
}
