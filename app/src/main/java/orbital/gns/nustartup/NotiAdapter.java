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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotiAdapter extends ArrayAdapter<NotiModel> implements View.OnClickListener{

    private ArrayList<NotiModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView newPic;
        TextView personNameTV;
        TextView companyNameTV;
    }

    public NotiAdapter(ArrayList<NotiModel> data, Context context) {
        super(context, R.layout.notification_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotiModel notiModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_item, parent, false);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.newPic = convertView.findViewById(R.id.postLogo);
        viewHolder.personNameTV = convertView.findViewById(R.id.personName);
        //viewHolder.companyNameTV = convertView.findViewById(R.id.companyName);

        result=convertView;
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if(dataSet.get(position).getWorker()){
            viewHolder.personNameTV.setText(dataSet.get(position).getPersonName() + " would like to collaborate with you in "+ dataSet.get(position).getCompanyName());
        } else {
            viewHolder.personNameTV.setText(dataSet.get(position).getPersonName() + " has accepted to collaborate with you in "+ dataSet.get(position).getCompanyName());
        }

        //viewHolder.companyNameTV.setText("Would like to collaborate with you in "+dataSet.get(position).getCompanyName());

        return convertView;
    }

}
