package com.tensun.multiitemdemo.viewholder;

import android.widget.ImageView;

import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.tensun.multiitemdemo.R;
import com.tensun.multiitemdemo.bean.ImageDragBean;

/**
 * @author free46000  2017/03/17
 * @version v1.0
 */
public class ImageManager2 extends BaseViewHolderManager<ImageDragBean> {

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_recycleview_card2;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, ImageDragBean imageDragBean) {
        ImageView imageView = getView(holder, R.id.image2);
        imageView.setImageResource(imageDragBean.getImage());
    }
}
