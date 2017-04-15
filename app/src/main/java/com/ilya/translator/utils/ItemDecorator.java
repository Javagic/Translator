package com.ilya.translator.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 14.04.17 21:02.
 */
public class ItemDecorator extends RecyclerView.ItemDecoration {
  private static final int WHITE_SPACE=0;
  private Drawable mDivider;

  public ItemDecorator(Drawable divider) {
    mDivider = divider;
  }
  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int dividerLeft = parent.getPaddingLeft();
    int dividerRight = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int dividerTop = child.getBottom() + params.bottomMargin ;
      int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

      mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
      mDivider.draw(c);
    }
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);

    outRect.top = mDivider.getIntrinsicHeight()+WHITE_SPACE;
  }
}