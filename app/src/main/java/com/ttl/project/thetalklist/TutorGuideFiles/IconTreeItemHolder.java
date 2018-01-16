package com.ttl.project.thetalklist.TutorGuideFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ttl.project.thetalklist.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);


        return inflater.inflate(R.layout.row_first, null, false);
    }

    @Override
    public void toggle(boolean active) {
    }

    public static class IconTreeItem {
        public final String text;

        public IconTreeItem( String text) {
            this.text = text;
        }
    }

}
