package com.linshengt.newsapplication.View.RecyclerView;

import android.content.Context;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linshengt.newsapplication.Catch.BitmapCache;
import com.linshengt.newsapplication.R;
import com.linshengt.volley.RequestQueue;
import com.linshengt.volley.toolbox.ImageLoader;
import com.linshengt.volley.toolbox.Volley;


/**
 * Created by linshengt on 2016/10/28.
 */
public class LoadMoreAdapter extends BaseLoadingAdapter<DesignItem>{
    private CircularArray<DesignItem> mDesignItems;
    private LayoutInflater mInflater = null;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private Context context;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private ItemClickListener mItemClickListener;
    public LoadMoreAdapter(RecyclerView recyclerView, CircularArray<DesignItem> ts, Context context) {
        super(recyclerView, ts);
        this.mDesignItems = ts;

        this.mInflater = LayoutInflater.from(context);
        mQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache(context));
    }


    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_design, parent, false);
        return new BRViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BRViewHolder viewHolder = (BRViewHolder)holder;
        DesignItem designItem = mDesignItems.get(position);


        ((BRViewHolder) holder).tv_title.setText(designItem.title);
        ((BRViewHolder) holder).tv_author.setText(designItem.author);

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(((BRViewHolder) holder).imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(designItem.imageUrl, listener, 200, 200);

    }

    class BRViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv_title;
        TextView tv_author;
        ImageView imageView;

        public BRViewHolder(final View view)
        {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_author = (TextView) view.findViewById(R.id.tv_author);
            imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onItemClick(view, getPosition());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onItemLongClick(view, getPosition());
                    }
                    return true;
                }
            });
        }
    }
}
