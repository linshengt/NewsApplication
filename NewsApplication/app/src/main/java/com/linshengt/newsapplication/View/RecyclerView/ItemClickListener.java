package com.linshengt.newsapplication.View.RecyclerView;

import android.view.View;

/**
 * item点击回调接口
 * 
 * @author wen_er
 * 
 */
public interface ItemClickListener {

	/**
	 * Item 普通点击
	 */

	public void onItemClick(View view, int postion);

	/**
	 * Item 长按
	 */

	public void onItemLongClick(View view, int postion);

}
