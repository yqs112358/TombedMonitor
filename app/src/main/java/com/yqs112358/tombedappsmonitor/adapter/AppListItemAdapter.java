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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yqs112358.tombedappsmonitor.R;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;

import java.util.List;

public class AppListItemAdapter extends RecyclerView.Adapter<AppListItemAdapter.MyViewHolder>{

    // 内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView appName;
        TextView processName;
        TextView appStatus;
        ImageView appIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            appName = (TextView)itemView.findViewById(R.id.appName);
            processName = (TextView)itemView.findViewById(R.id.processName);
            appStatus = (TextView)itemView.findViewById(R.id.appStatus);
            appIcon = (ImageView)itemView.findViewById(R.id.appIcon);
        }
    }

    private Context context;
    private List<ProcessAndAppInfo> dataList;
    private View inflater;

    public AppListItemAdapter(Context context, List<ProcessAndAppInfo> list){
        this.context = context;
        this.dataList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.app_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 更新控件数据
        ProcessAndAppInfo appInfo = dataList.get(position);

        // set icon
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());

        // set name
        boolean isApp = appInfo.getIsApp();
        holder.appName.setText(isApp ? appInfo.getAppName() : appInfo.getProcessName());

        // set processName
        holder.processName.setText(appInfo.getProcessName());

        // set app status
        switch(appInfo.getFrozenType())
        {
            case FreezerV2:
                holder.appStatus.setText(R.string.freeze_status_V2);
                break;
            case FreezerV1:
                holder.appStatus.setText(R.string.freeze_status_V1);
                break;
            case SIGSTOP:
                holder.appStatus.setText(R.string.freeze_status_SIGSTOP);
                break;
            case MaybeV2:
                holder.appStatus.setText(R.string.freeze_status_maybeV2);
                break;
            case SIGTSTP:
                holder.appStatus.setText(R.string.freeze_status_unknown);
                break;
            default:
                holder.appStatus.setText(R.string.freeze_status_unknown);
                break;
        }
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return dataList.size();
    }
}
