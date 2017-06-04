package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import abominable.com.wholeseller.BuildConfig;
import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Constants;
import abominable.com.wholeseller.detail.DetailActivity;

/**
 * Created by shubham.srivastava on 28/05/17.
 */

public class GridAdapter extends BaseAdapter {

  private Context mContext;
  private  List<ChildData> childData;
  private  String genreName;

  public GridAdapter(Context context, List<ChildData> childData,String genreName) {
    mContext = context;
    this.childData = childData;
    this.genreName = genreName;
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
      holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_layout);
      holder.parentLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          final Intent intent = new Intent(mContext, DetailActivity.class);
          intent.putExtra(Constants.GENRE_NAME, genreName);
          mContext.startActivity(intent);
        }
      });
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
    RelativeLayout parentLayout;
  }
}
