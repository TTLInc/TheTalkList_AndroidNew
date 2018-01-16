package com.ttl.project.thetalklist.TutorGuideFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ttl.project.thetalklist.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Saubhagyam on 10/05/2017.
 */

public class ForthLevelHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    public ForthLevelHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.row_forth, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.eventsListEventRowTextforth);
        tvValue.setText(value.text);


        return view;
    }

    @Override
    public void toggle(boolean active) {
    }
}
