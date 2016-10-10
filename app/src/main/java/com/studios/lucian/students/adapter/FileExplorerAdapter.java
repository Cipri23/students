package com.studios.lucian.students.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studios.lucian.students.R;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 11.06.2016.
 */
public class FileExplorerAdapter extends ArrayAdapter {

    private final List<String> paths;
    private final List<String> files;
    private final Context context;

    public FileExplorerAdapter(Context context, List<String> item, List<String> paths) {
        super(context, R.layout.item_explorer, item);
        this.files = item;
        this.context = context;
        this.paths = paths;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_explorer, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.icon = (ImageView) rowView.findViewById(R.id.file_icon);
            viewHolder.textName = (TextView) rowView.findViewById(R.id.rowtext);
            viewHolder.textBytes = (TextView) rowView.findViewById(R.id.text_view_bytes);
            viewHolder.textDate = (TextView) rowView.findViewById(R.id.text_view_date);
            rowView.setTag(viewHolder);
        }

        File file = new File(paths.get(position));
        final ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.textName.setText(files.get(position));
        holder.textDate.setText(getLastModified(file));
        if (file.isDirectory()) {
            holder.icon.setImageResource(R.drawable.directory_icon);
            holder.textBytes.setText(getItemsCount(file));
        } else {
            holder.textBytes.setText(file.length() + " Bytes");
            holder.icon.setImageResource(R.drawable.file_icon);
        }
        return rowView;
    }

    private String getLastModified(File file) {
        Date lastModDate = new Date(file.lastModified());
        DateFormat formatter = DateFormat.getDateTimeInstance();
        return formatter.format(lastModDate);
    }

    @NonNull
    private String getItemsCount(File file) {
        File[] fbuf = file.listFiles();
        int buf;
        if (fbuf != null) {
            buf = fbuf.length;
        } else buf = 0;
        String num_item = String.valueOf(buf);
        if (buf == 0) num_item = num_item + " item";
        else num_item = num_item + " items";
        return num_item;
    }

    static class ViewHolder {
        ImageView icon;
        TextView textName;
        TextView textBytes;
        TextView textDate;
    }
}
