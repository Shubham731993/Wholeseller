package abominable.com.wholeseller.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import abominable.com.wholeseller.common.DividerItemDecoration;

/**
 * Created by shubham.srivastava on 17/07/16.
 */
public class LoaderItemDecorater extends DividerItemDecoration {
    private static final int[] ATTRS = {android.R.attr.listDivider};
    private final Drawable mDivider;

    public LoaderItemDecorater(final Context context, final int orientation) {
      super(context, orientation);
      final TypedArray a = context.obtainStyledAttributes(ATTRS);
      mDivider = a.getDrawable(0);
      a.recycle();
      setOrientation(orientation);
    }

    @Override
    public void drawVertical(final Canvas c, final RecyclerView parent) {
      final int left = parent.getPaddingLeft();
      final int right = parent.getWidth() - parent.getPaddingRight();
      final int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
        final View child = parent.getChildAt(i);
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + mDivider.getIntrinsicHeight();
        mDivider.setBounds(left, top, right, bottom);
        int childPosition = parent.getChildAdapterPosition(child);
        if (childPosition == childCount - 1)
          continue;
        mDivider.draw(c);
      }
    }
  }

