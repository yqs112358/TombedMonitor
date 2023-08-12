package com.yqs112358.tombedappsmonitor.adapter;

import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.FreezerV1;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.FreezerV2;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.MaybeV2;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.SIGSTOP;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.SIGTSTP;

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

public class AppListItemAdapter extends ArrayAdapter<ProcessAndAppInfo> {

    private final int resourceId;

    public AppListItemAdapter(@NonNull Context context, int resource, List<ProcessAndAppInfo> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProcessAndAppInfo appInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }

        // set icon
        ImageView appIconImage = convertView.findViewById(R.id.appIcon);
        appIconImage.setImageDrawable(appInfo.getAppIcon());

        // set name
        TextView appNameText = convertView.findViewById(R.id.appName);
        boolean isApp = appInfo.getIsApp();
        appNameText.setText(isApp ? appInfo.getAppName() : appInfo.getProcessName());

        // set processName
        TextView processNameText = convertView.findViewById(R.id.processName);
        processNameText.setText(appInfo.getProcessName());

        // set app status
        TextView statusText = convertView.findViewById(R.id.appStatus);
        switch(appInfo.getFrozenType())
        {
            case FreezerV2:
                statusText.setText(R.string.freeze_status_V2);
                break;
            case FreezerV1:
                statusText.setText(R.string.freeze_status_V1);
                break;
            case SIGSTOP:
                statusText.setText(R.string.freeze_status_SIGSTOP);
                break;
            case MaybeV2:
                statusText.setText(R.string.freeze_status_maybeV2);
                break;
            case SIGTSTP:
                statusText.setText(R.string.freeze_status_unknown);
                break;
            default:
                statusText.setText(R.string.freeze_status_unknown);
                break;
        }
        return convertView;
    }
}
