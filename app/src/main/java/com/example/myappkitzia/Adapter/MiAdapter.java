package com.example.myappkitzia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView textNom = null;
        TextView textContra = null;
        ImageView imageView = null;
        view = layoutInflater.inflate(R.layout.activity_listita, null );
        textNom = view.findViewById(R.id.textView5);
        textContra = view.findViewById(R.id.textView6);

        textNom.setText(list.get(i).getNombre());
        textContra.setText(list.get(i).getPswd());

        imageView = view.findViewById(R.id.imgAvatar);
        imageView.setImageResource(list.get(i).getLogo());
        return view;
    }
}

