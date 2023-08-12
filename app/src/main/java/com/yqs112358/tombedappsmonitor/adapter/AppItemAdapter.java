package com.yqs112358.tombedappsmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yqs112358.tombedappsmonitor.R;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;

import java.util.List;

public class AppItemAdapter extends ArrayAdapter<ProcessAndAppInfo> {

    private final int resourceId;

    public AppItemAdapter(@NonNull Context context, int resource, List<ProcessAndAppInfo> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProcessAndAppInfo appInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }
        ImageView appIconImage = convertView.findViewById(R.id.appIcon);
        TextView appNameText = convertView.findViewById(R.id.appName);
        TextView packageNameText = convertView.findViewById(R.id.packageName);

        appIconImage.setImageDrawable(appInfo.getAppIcon());
        boolean isApp = appInfo.getIsApp();
        appNameText.setText(isApp ? appInfo.getAppName() : appInfo.getProcessName());
        packageNameText.setText(appInfo.getPackageName());
        return convertView;
    }
}
