package abominable.com.wholeseller.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 24/12/16.
 */
public class CarousalImageAdapter extends PagerAdapter {
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public CarousalImageAdapter(Context context) {
    this.mContext = context;
    this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return 4;
  }


  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    ViewGroup layout = (ViewGroup) mLayoutInflater.inflate(R.layout.item_welcome_pager,
        container, false);
    ImageView imageView= (ImageView) layout.findViewById(R.id.image_view);
    //Picasso.with(mContext).load("http://194.168.1.171:8080/wholemart-1367/webapi/imagebucket/rice.jpg").into(imageView);
    imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.toordal_carousel));
    container.addView(layout);
    return layout;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public void destroyItem(ViewGroup collection, int position, Object view) {
    collection.removeView((View) view);
  }

}
