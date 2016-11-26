package abominable.com.wholeseller.common;

/**
 * Created by shubham.srivastava on 17/07/16.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
  public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
  public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
  private static final int[] ATTRS = {android.R.attr.listDivider};
  private final Drawable mDivider;
  private int lineColor;
  private int mOrientation;

  public DividerItemDecoration(final Context context, final int orientation) {
    super();
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDivider = a.getDrawable(0);
    a.recycle();
    setOrientation(orientation);
  }

  public DividerItemDecoration(final Context context, final int orientation, int color) {
    super();
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDivider = a.getDrawable(0);
    a.recycle();
    setOrientation(orientation);
    this.lineColor = color;
  }

  public void setOrientation(final int orientation) {
    if ((orientation != HORIZONTAL_LIST) && (orientation != VERTICAL_LIST)) {
      throw new IllegalArgumentException("invalid orientation");
    }
    mOrientation = orientation;
  }

  @Override
  public void onDraw(final Canvas c, final RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      if (lineColor == 0) {
        drawVertical(c, parent);
      } else {
        drawVertical(c, parent, lineColor);
      }
    } else {
      drawHorizontal(c, parent);
    }
  }

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
      mDivider.draw(c);
    }
  }

  public void drawVertical(final Canvas c, final RecyclerView parent, int lineColor) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();
    mDivider.setColorFilter(new PorterDuffColorFilter(lineColor, PorterDuff.Mode.MULTIPLY));
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int top = child.getBottom() + params.bottomMargin;
      final int bottom = top + mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  public void drawHorizontal(final Canvas c, final RecyclerView parent) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getHeight() - parent.getPaddingBottom();
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
          .getLayoutParams();
      final int left = child.getRight() + params.rightMargin;
      final int right = left + mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  @Override
  public void getItemOffsets(final Rect outRect, final int itemPosition, final RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    } else {
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
  }
}
