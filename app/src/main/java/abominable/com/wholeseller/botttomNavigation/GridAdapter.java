package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;

/**
 * Created by shubham.srivastava on 28/05/17.
 */

public class GridAdapter extends BaseAdapter {

  private Context mContext;
  private  List<ChildData> childData;

  public GridAdapter(Context context, List<ChildData> childData) {
    mContext = context;
    this.childData = childData;
  }

  @Override
  public int getCount() {
    return childData.size();
  }

  @Override
  public String getItem(int position) {
    return childData.get(position).getName();
  }

  @Override
  public long getItemId(int arg0) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder holder = null;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) mContext
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.grid_item, parent, false);
      holder = new ViewHolder();

      holder.text = (TextView) convertView.findViewById(R.id.label);
      holder.image = (ImageView) convertView.findViewById(R.id.image);

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.text.setText(childData.get(position).getName());
    String imagePath= BuildConfig.IMAGE_BUCKET_HOST+childData.get(position).getImagePath()+".jpg";
    Picasso.with(mContext).load(imagePath).into(holder.image);
    return convertView;
  }

  private class ViewHolder {
    TextView text;
    ImageView image;
  }
}
