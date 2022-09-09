package com.dspread.demoui.activities.serialprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dspread.demoui.R;

import java.util.List;

public class PrintAdapter extends BaseAdapter {

    private LayoutInflater LayoutInflater;
    private Context context;

    private List<PrintSettingBean> list;


    public PrintAdapter(Context context, List<PrintSettingBean> list) {
        this.context = context;
        this.list = list;
        this.LayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.inflate(R.layout.item_printsetting,null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.item_id);
            holder.type = convertView.findViewById(R.id.item_end);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getValue()==null?"":list.get(position).getValue());
        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView type;
    }
}
