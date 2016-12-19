package abominable.com.wholeseller.detail;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import abominable.com.wholeseller.R;
import abominable.com.wholeseller.common.Utility;

/**
 * Created by shubham.srivastava on 05/08/16.
 */
public class AddToCartFragment extends DialogFragment {
  private Activity mActivity;
  private DetailObject detailObject;
  PassData mCallback;
  private int position;

  public static AddToCartFragment newInstance(DetailObject detailObject, int position) {
    AddToCartFragment addToCartFragment = new AddToCartFragment();
    Bundle args = new Bundle();
    args.putParcelable("detailObject", detailObject);
    args.putInt("position", position);
    addToCartFragment.setArguments(args);
    return addToCartFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    detailObject = getArguments().getParcelable("detailObject");
    position = getArguments().getInt("position");
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_add_to_cart, container);
    final TextView itemText = (TextView) view.findViewById(R.id.title);
    final EditText editText = (EditText) view.findViewById(R.id.editText);
    CardView cardView = (CardView) view.findViewById(R.id.card_view);
    Button hundred = (Button) view.findViewById(R.id.hundred);
    Button fifty = (Button) view.findViewById(R.id.fifty);
    Button twentyfive = (Button) view.findViewById(R.id.twentyfive);
    Button ten = (Button) view.findViewById(R.id.ten);
    Button done = (Button) view.findViewById(R.id.done);
    Button cancel = (Button) view.findViewById(R.id.dismiss);
    LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.buttons);
    final Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.show_cart_item);
    anim.setFillAfter(true);
    buttonLayout.startAnimation(anim);
    cardView.startAnimation(anim);
    hundred.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editText.setText("100");
      }
    });
    twentyfive.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editText.setText("25");
      }
    });
    fifty.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editText.setText("50");
      }
    });
    ten.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editText.setText("10");
      }
    });
    done.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
          mCallback.passNoOfKgs(editText.getText().toString(), detailObject.getId(),position,detailObject.getName(),detailObject.getPrice());
          dismiss();
        } else {
          editText.setError("Please enter value");
          editText.requestFocus();
        }
      }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    itemText.setText(detailObject.getName());
    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = activity;
    try {
      mCallback = (PassData) activity;
    } catch (ClassCastException e) {
      Utility.reportException(e);
    }
  }

  public interface PassData {
    void passNoOfKgs(String val, String id,int itemPosition,String itemName,double price);
  }
}
