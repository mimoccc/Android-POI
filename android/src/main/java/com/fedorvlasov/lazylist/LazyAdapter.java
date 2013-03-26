package com.fedorvlasov.lazylist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poi.R;
import com.poi.dto.PoiDTO;

public class LazyAdapter extends BaseAdapter {
    
    private static final String TAG = "LazyAdapter";
	private Activity activity;
    private List<PoiDTO> listItens;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, List<PoiDTO> listItens) {
        this.activity = a;
        this.listItens = listItens;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return listItens.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView text;
        public ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	if (position < listItens.size()) {
    		return convertView;
    	}
    	
        View vi=convertView;
        ViewHolder holder;
        if(convertView==null){
            vi = inflater.inflate(R.layout.item, null);
            holder=new ViewHolder();
            holder.text=(TextView)vi.findViewById(R.id.text);;
            holder.image=(ImageView)vi.findViewById(R.id.image);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
        Log.d(TAG, "item "+position);
        holder.text.setText("item "+position);
        
        //listItens.get(position).
        /*
        holder.image.setTag(listItens[position]);
        imageLoader.DisplayImage(listItens[position], activity, holder.image);
        */
        return vi;
    }
}