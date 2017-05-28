package abominable.com.wholeseller.botttomNavigation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.WMTextView;

/**
 * Created by shubham.srivastava on 28/05/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

  private Context mContext;
  private List<String> listDataHeader;
  private HashMap<String, List<ChildData>> listDataChild;

  public ExpandableListAdapter(Context context, List<String> listDataHeader,
                               HashMap<String, List<ChildData>> listChildData) {
    this.mContext = context;
    this.listDataHeader = listDataHeader;
    this.listDataChild = listChildData;
  }

  @Override
  public Object getChild(int groupPosition, int childPosititon) {
    return this.listDataChild.get(this.listDataHeader.get(groupPosition))
        .get(childPosititon);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public View getChildView(int groupPosition, final int childPosition,
                           boolean isLastChild, View convertView, ViewGroup parent) {

    final ChildData childData = (ChildData) getChild(groupPosition, childPosition);
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) this.mContext
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.list_item, null);
    }
    CustomGridView gridView = (CustomGridView) convertView
        .findViewById(R.id.GridView_toolbar);
    gridView.setNumColumns(3);// gridView.setGravity(Gravity.CENTER);//
    gridView.setHorizontalSpacing(10);// SimpleAdapter adapter =
    GridAdapter adapter = new GridAdapter(mContext, listDataChild.get(listDataHeader.get(groupPosition)));
    gridView.setAdapter(adapter);// Adapter

    int totalHeight = 0;
    double numOfRows=Math.ceil(adapter.getCount()/3.0);
   /* for (int size = 0; size < adapter.getCount()/3; size++) {
      RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
          size, null, gridView);
      ImageView imageView = (ImageView) relativeLayout.getChildAt(0);
      imageView.measure(0, 0);
      totalHeight += imageView.getMeasuredHeight();
      TextView textView = (TextView) relativeLayout.getChildAt(1);
      textView.measure(0, 0);
      totalHeight += textView.getMeasuredHeight();
    }*/
    gridView.SetHeight(450*(int)numOfRows);
    return convertView;

  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.listDataHeader.get(groupPosition);
  }

  @Override
  public int getGroupCount() {
    return this.listDataHeader.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater inflator = (LayoutInflater) this.mContext
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflator.inflate(R.layout.list_header, null);
    }
    WMTextView lblListHeader = (WMTextView) convertView
        .findViewById(R.id.title);
    ImageView genreImage = (ImageView) convertView
        .findViewById(R.id.image_view);
    lblListHeader.setText(listDataHeader.get(groupPosition));
    switch (listDataHeader.get(groupPosition)){
      case "Dal":
        genreImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.daal));
        break;
      case "Rice":
        genreImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.rice));
        break;
      case "Dry Fruits":
        genreImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.dry_fruits));
        break;
      default:
        genreImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.dry_fruits));
        break;
    }
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
