package lixiaoqian.bwie.com.lixiaoqian20170504;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * @类的用途：xlistview的适配器类
 * @author: 李晓倩
 * @date: 2017/5/4
 */

public class MyBaseAdapter extends BaseAdapter{

    private List<GsonBean.AppBean> mAppBeanList;
    private Context mContext;

    public MyBaseAdapter(Context mContext,List<GsonBean.AppBean> mAppBeanList){
        this.mContext=mContext;
        this.mAppBeanList=mAppBeanList;
    }

    @Override
    public int getCount() {
        return mAppBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // xlistview优化
       ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext,R.layout.xlv_item,null);
            holder.mTextView= (TextView) convertView.findViewById(R.id.textView);
            holder.mCheckBox= (CheckBox)convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.mTextView.setText(mAppBeanList.get(position).getName()+"");

        return convertView;
    }

    class ViewHolder{
        TextView mTextView;
        CheckBox mCheckBox;
    }
}
