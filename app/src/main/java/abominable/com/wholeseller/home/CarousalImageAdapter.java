package abominable.com.wholeseller.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 24/12/16.
 */
public class CarousalImageAdapter extends PagerAdapter {
  private LayoutInflater mLayoutInflater;
  private Context mContext;
  private List<String> carouselItems;

  public CarousalImageAdapter(List<String> carouselItems,Context context) {
    this.mContext = context;
    this.carouselItems = carouselItems;
    this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return carouselItems.size();
  }


  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    ViewGroup layout = (ViewGroup) mLayoutInflater.inflate(R.layout.item_welcome_pager,
        container, false);
    ImageView imageView= (ImageView) layout.findViewById(R.id.image_view);
    Picasso.with(mContext).load(BuildConfig.IMAGE_BUCKET_HOST+carouselItems.get(position)).into(imageView);
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
