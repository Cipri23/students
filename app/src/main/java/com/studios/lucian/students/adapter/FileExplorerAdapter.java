package com.studios.lucian.students.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studios.lucian.students.R;

import java.io.File;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 11.06.2016.
 */
public class FileExplorerAdapter extends ArrayAdapter {

    private final List<String> paths;
    private List<String> files;
    private Context context;

    public FileExplorerAdapter(Context context, List<String> item, List<String> paths) {
        super(context, R.layout.item_explorer, item);
        this.files = item;
        this.context = context;
        this.paths = paths;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("FileEx", "getView: " + files.get(position));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_explorer, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.file_icon);
            TextView textView = (TextView) view.findViewById(R.id.rowtext);
            TextView textViewBytes = (TextView) view.findViewById(R.id.text_view_bytes);
            TextView textViewDate = (TextView) view.findViewById(R.id.text_view_date);

            File file = new File(paths.get(position));
            if (file.isDirectory()) {
                imageView.setImageResource(R.drawable.directory_icon);
            } else {
                imageView.setImageResource(R.drawable.file_icon);
            }
            textView.setText(files.get(position));
        } else {
            view = convertView;
        }
        return view;
    }
}
