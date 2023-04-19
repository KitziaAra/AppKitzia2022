package com.example.myappkitzia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myappkitzia.Recursos.EncripBitMap;
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.R;

import java.io.Serializable;
import java.util.List;

public class MiAdapter extends BaseAdapter implements Serializable {
    private List<MyData> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public MiAdapter(List<MyData> list, Context context)
    {
        this.list = list;
        this.context = context;
        if( context != null)
        {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    public boolean isEmptyOrNull( )
    {
        return list == null || list.size() == 0;
    }

    @Override
    public int getCount()
    {
        if(isEmptyOrNull())
        {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        if(isEmptyOrNull())
        {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView textView1 = null;
        TextView textView2 = null;
        TextView textView3 = null;
        ImageView imageView = null;
        view = layoutInflater.inflate(R.layout.activity_listita_listita, null );
        textView1 = view.findViewById(R.id.textViewLVA1);
        textView2 = view.findViewById(R.id.textViewLVA2);
        textView3 = view.findViewById(R.id.textViewLVA3);
        imageView = view.findViewById(R.id.imageViewLVAUser);
        textView1.setText(list.get(i).getNombre());
        textView2.setText("Lat: " + list.get(i).getLocation().getLatitude());
        textView3.setText("Lon: " + list.get(i).getLocation().getLongitude());
        if(list.get(i).isTipo() != true) {
            imageView.setImageResource(list.get(i).getImage());
        }else{
            EncripBitMap EBM = new EncripBitMap();
            imageView.setImageBitmap(EBM.desCifrar(list.get(i).getImageP()));
        }

        return view;


    }
}

