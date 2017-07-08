package xebia.ismail.plnpjb.sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xebia.ismail.plnpjb.R;

/**
 * Created by acer on 6/13/2017.
 */

public class ListAdapter extends ArrayAdapter<SmsData> {

    //List context
    private final Context context;
    //List Value
    private final List<SmsData>smsList;

    public ListAdapter(Context context, List<SmsData> smsList){

        super(context, R.layout.sms_activity, smsList);
        this.context =context;
        this.smsList = smsList;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.sms_activity, parent, false);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.sms_number);
        senderNumber.setText(smsList.get(position).getNumber());

        return rowView;
    }
}
