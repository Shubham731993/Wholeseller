package abominable.com.wholeseller.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.detail.DetailActivity;

/**
 * Created by shubham.srivastava on 10/04/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
  private ArrayList<SearchObject> searchObjects;
  private Context context;
  private ArrayList<SearchObject> newSearchObjects;

  public SearchAdapter(Context context,ArrayList<SearchObject> searchObjects){
    this.context=context;
    this.searchObjects=searchObjects;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new SearchHolder(layoutInflater.inflate(R.layout.search_item, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    SearchHolder searchHolder=(SearchHolder)holder;
    searchHolder.name.setText(searchObjects.get(position).getName());
    searchHolder.category.setText(searchObjects.get(position).getGenreName());
  }

  @Override
  public int getItemCount() {
    return searchObjects.size();
  }

  private class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView name;
    TextView category;
    public SearchHolder(View inflate) {
      super(inflate);
      name= (TextView) inflate.findViewById(R.id.name);
      category= (TextView) inflate.findViewById(R.id.category);
      inflate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int position = getLayoutPosition();
      Intent intent=new Intent(context,DetailActivity.class);
      context.startActivity(intent);
    }
  }

  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        if(newSearchObjects==null){
          newSearchObjects=new ArrayList<>(searchObjects);
        }
        final FilterResults filterResults=new FilterResults();
        final ArrayList<SearchObject> filteredLocations = new ArrayList<>();
        constraint = constraint.toString().toLowerCase();
        for(int i=0;i<newSearchObjects.size();i++){
          if(newSearchObjects.get(i).getName().toLowerCase().contains(constraint)){
            filteredLocations.add(newSearchObjects.get(i));
          }
        }
        filterResults.count = filteredLocations.size();
        filterResults.values = filteredLocations;
        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        searchObjects = (ArrayList<SearchObject>) results.values;
        notifyDataSetChanged();
      }
    };

  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }
}

