package com.yqs112358.tombedappsmonitor.adapter;

import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.FreezerV1;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.FreezerV2;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.MaybeV2;
import static com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo.FrozenType.SIGSTOP;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
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
        if(appInfo == null) {
            Log.e("TombedMonitor", "Null data item??");
            return;
        }

        // set icon
        Drawable icon = appInfo.getAppIcon();
        if(icon != null)
            holder.appIcon.setImageDrawable(icon);

        // set name
        boolean isApp = appInfo.getIsApp();
        holder.appName.setText(isApp ? appInfo.getAppName() : appInfo.getProcessName());

        // set processName
        String processName = appInfo.getProcessName();
        if(processName != null)
            holder.processName.setText(processName);

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

    public void updateData(List<ProcessAndAppInfo> newDataList)
    {
        // Calc diff and update recycleview
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return dataList.size();
            }

            @Override
            public int getNewListSize() {
                return newDataList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // 判断两项是否是同一项
                return dataList.get(oldItemPosition).getProcessName().equals(
                        newDataList.get(newItemPosition).getProcessName());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                // 判断两项的内容是否相同
                ProcessAndAppInfo oldItem = dataList.get(oldItemPosition);
                ProcessAndAppInfo newItem = newDataList.get(newItemPosition);
                return oldItem.getStatus().equals(newItem.getStatus())
                        && oldItem.getFrozenType().equals(newItem.getFrozenType());
            }
        });

        dataList.clear();
        dataList.addAll(newDataList);
        diffResult.dispatchUpdatesTo(this);
    }
}
