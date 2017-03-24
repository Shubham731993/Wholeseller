package abominable.com.wholeseller.common;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 17/03/17.
 */

public class WMTextView extends TextView {

  /**
   * @param context
   * @param attrs
   * @param defStyle
   */
  public WMTextView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
    if (!isInEditMode()) {
      init();
    }
  }

  /**
   * @param context
   * @param attrs
   */
  public WMTextView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) {
      init();
    }
  }

  /**
   * @param context
   */
  public WMTextView(final Context context) {
    super(context);
    if (!isInEditMode()) {
      init();
    }

  }

  private void init() {
    if (getTag() != null) {
      if (getTag().equals(getResources().getString(R.string.medium))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.bold))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Bold.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.thin))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.regular))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.light))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_ROBOTO_LIGHT_TTF);
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.font_black))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Black.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.light_underline))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_ROBOTO_LIGHT_TTF);
        setTypeface(tf);
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
      } else if (getTag().equals(getResources().getString(R.string.condensed_regular))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.condensed_light))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");
        setTypeface(tf);
      } else if (getTag().equals(getResources().getString(R.string.condensed_bold))) {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Bold.ttf");
        setTypeface(tf);
      } else {
        final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
        setTypeface(tf);
      }
    } else {
      final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
      setTypeface(tf);
    }
  }



}
