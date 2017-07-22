package com.tensun.multiitemdemo.bean;

import com.freelib.multiitem.item.BaseItemData;
import com.freelib.multiitem.item.ItemDrag;

/**
 * 此处可以继承BaseItemData也可以实现ItemData{}
 * Created by free46000 on 2017/4/3.
 */
public class ImageDragBean extends BaseItemData implements ItemDrag {
    private int imgage;
    private boolean isCanMove = true;
    private boolean isCanChangeRecycler = true;
    private boolean isCanDrag = true;

    public ImageDragBean(int imgage) {
        this.imgage = imgage;
    }

    public ImageDragBean(int imgage, boolean isCanMove) {
        this.imgage = imgage;
        this.isCanMove = isCanMove;
    }

    public ImageDragBean(int imgage, boolean isCanMove, boolean isCanChangeRecycler) {
        this.imgage = imgage;
        this.isCanMove = isCanMove;
        this.isCanChangeRecycler = isCanChangeRecycler;
    }

    public ImageDragBean(int imgage, boolean isCanMove, boolean isCanChangeRecycler, boolean isCanDrag) {
        this.imgage = imgage;
        this.isCanMove = isCanMove;
        this.isCanChangeRecycler = isCanChangeRecycler;
        this.isCanDrag = isCanDrag;
    }

    public int getImage() {
        return imgage;
    }

    public void setImage(int imgage) {
        this.imgage = imgage;
    }

    @Override
    public boolean isCanMove() {
        return isCanMove;
    }

    public void setCanMove(boolean canMove) {
        isCanMove = canMove;
    }

    @Override
    public boolean isCanChangeRecycler() {
        return isCanChangeRecycler;
    }

    public void setCanChangeRecycler(boolean canChangeRecycler) {
        isCanChangeRecycler = canChangeRecycler;
    }

    @Override
    public boolean isCanDrag() {
        return isCanDrag;
    }

    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
    }
}
