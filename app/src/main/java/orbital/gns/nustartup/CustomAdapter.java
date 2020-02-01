package orbital.gns.nustartup;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        ImageView newPic;
        TextView tvStartUp;
        TextView tvStartUpFounder;
        TextView tvLocation;

//        TextView txtName;
//        TextView txtType;
//        TextView txtVersion;
//        ImageView info;
//        TextView startUpNameHolder;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        DataModel dataModel=(DataModel)object;
//
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
//        viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
//        viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
//        viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
//        ((TextView)convertView.findViewById(R.id.startUpNameHolder)).setText(dataSet.get(position).getName());

        viewHolder.newPic = convertView.findViewById(R.id.postLogo);
        viewHolder.tvStartUp = convertView.findViewById(R.id.startUpName);
        viewHolder.tvStartUpFounder = convertView.findViewById(R.id.startUpCompanyName);
        viewHolder.tvLocation = convertView.findViewById(R.id.textView3);

        result=convertView;
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.tvStartUp.setText(dataSet.get(position).getName());
        viewHolder.tvStartUpFounder.setText(dataSet.get(position).getFounder());
        viewHolder.tvLocation.setText(dataSet.get(position).getLocation());

//        viewHolder.txtName.setText(dataModel.getName());
//        viewHolder.txtType.setText(dataModel.getType());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

}
