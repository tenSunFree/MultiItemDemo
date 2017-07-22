package com.tensun.multiitemdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.freelib.multiitem.adapter.BaseItemAdapter;
import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.freelib.multiitem.helper.ItemDragHelper;
import com.freelib.multiitem.helper.ViewScaleHelper;
import com.freelib.multiitem.item.UniqueItemManager;
import com.freelib.multiitem.listener.OnItemDragListener;
import com.freelib.multiitem.listener.OnItemLongClickListener;
import com.tensun.multiitemdemo.bean.ImageDragBean;
import com.tensun.multiitemdemo.viewholder.ImageManager;
import com.tensun.multiitemdemo.viewholder.ImageManager2;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tensun.multiitemdemo.R.id.recyclerView;

/**
 * Q: 拖動效果的實現流程?
 * A:
 */

@EActivity(R.layout.activity_panel)
public class PanelActivity extends AppCompatActivity {
    @ViewById(R.id.panel_content)
    protected View contentView;

    public static final int NONE = -1;
    private RecyclerView fatherRecyclerView;
    private BaseItemAdapter adapter;
    private ItemDragHelper dragHelper;
    private ViewScaleHelper scaleHelper;

    public static void startActivity(Context context) {
        PanelActivity_.intent(context).start();                                                                           // 記得在ManiFest 註冊PanelActivity_, 紅字沒關係, 再執行Rebuild Project即可
    }

    @AfterViews
    protected void initView() {

        fatherRecyclerView = (RecyclerView) findViewById(recyclerView);
        fatherRecyclerView.setLayoutManager(                                                                            // 把父RecyclerView 設成垂直方向
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new BaseItemAdapter();
        adapter.addDataItems(
                Arrays.asList(
                        new UniqueItemManager(new RecyclerViewManager(6)),                                              // 實現了上層 子RecyclerView
                        new UniqueItemManager(new RecyclerViewManager2(2))));                                           // 實現了下層 子RecyclerView

        fatherRecyclerView.setAdapter(adapter);                                                                         // 賦予 父RecyclerView 內容

        dragHelper = new ItemDragHelper(fatherRecyclerView);                                                            // ItemDragHelper 需要傳入 父RecyclerView
        dragHelper.setOnItemDragListener(new OnBaseDragListener());

        scaleHelper = new ViewScaleHelper();

        scaleHelper.setContentView(contentView);                                                                        // 設置最外層的內容視圖

        scaleHelper.setHorizontalView(fatherRecyclerView);                                                              // 設置 父Recycler的列表視圖
    }

    /**
     * 需要把觸摸事件傳給dragHelper, true 表示消耗掉事件
     * 需要保證在活動或者外層的ViewGroup中或可以攔截觸摸事件的地方回調都可以
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return dragHelper.onTouch(ev) || super.dispatchTouchEvent(ev);
    }

    class OnBaseDragListener extends OnItemDragListener {

        @Override
        public float getScale() {
            return scaleHelper.isInScaleMode() ? scaleHelper.getScale() : super.getScale();
        }

        @Override
        public void onDragFinish(RecyclerView recyclerView, int itemRecyclerPos, int itemPos) {                         // 拖動完, 想要做些什麼?
            super.onDragFinish(recyclerView, itemRecyclerPos, itemPos);
        }
    }

    /** 關於上層 子RecyclerView */
    class RecyclerViewManager extends BaseViewHolderManager<UniqueItemManager> {

        private int length;

        RecyclerViewManager(int length) {
            this.length = length;
        }

        @Override
        protected void onCreateViewHolder(@NonNull BaseViewHolder holder) {
            super.onCreateViewHolder(holder);
            View view = holder.itemView;
            view.getLayoutParams().width = -1;

            scaleHelper.addVerticalView(view);

            final DiscreteScrollView recyclerView = getView(view, R.id.item_group_recycler);
            recyclerView.setOrientation(Orientation.HORIZONTAL);                                                      // 把子RecycleView 設成水平方向

            /** 實現當前圖片放大的效果 */
            recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.75f)
                    .build());

            final BaseItemAdapter baseItemAdapter = new BaseItemAdapter();
            baseItemAdapter.register(ImageDragBean.class, new ImageManager());                                          // 幫Bean數據源 註冊ViewHolder管理類
            baseItemAdapter.setDataItems(getItemList(length));
            recyclerView.setAdapter(baseItemAdapter);

            baseItemAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                protected void onItemLongClick(BaseViewHolder viewHolder) {
                    dragHelper.startDrag(viewHolder);
                }
            });
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull UniqueItemManager data) {

        }

        @Override
        protected int getItemLayoutId() {                                                                               // 設置 子RecycleView的Layout
            return R.layout.item_recycler_view;
        }

        private List<Object> getItemList(int length) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (i == 0) {                                                                                           // 實現 子RecycleView, Item 0 的呈現
                    list.add(new ImageDragBean(
                            R.drawable.e_01, false, false, false)
                    );
                }
                list.add(new ImageDragBean(R.drawable.a_03));                                                           // 實現 子RecycleView, Item 1~i 的呈現, 可隨意拖動 無限制
            }
            return list;
        }
    }

    /** 關於下層 子RecyclerView */
    class RecyclerViewManager2 extends BaseViewHolderManager<UniqueItemManager> {

        private int length;

        RecyclerViewManager2(int length) {
            this.length = length;
        }

        @Override
        protected void onCreateViewHolder(@NonNull BaseViewHolder holder) {
            super.onCreateViewHolder(holder);
            View view = holder.itemView;
            view.getLayoutParams().width = -1;

            scaleHelper.addVerticalView(view);

            final DiscreteScrollView recyclerView2 = getView(view, R.id.item_group_recycler2);
            recyclerView2.setOrientation(Orientation.HORIZONTAL);                                                     // 把子RecycleView 設成水平方向

            final BaseItemAdapter baseItemAdapter = new BaseItemAdapter();
            baseItemAdapter.register(ImageDragBean.class, new ImageManager2());                                         // 幫Bean數據源 註冊ViewHolder管理類
            baseItemAdapter.setDataItems(getItemList(length));
            recyclerView2.setAdapter(baseItemAdapter);

            baseItemAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                protected void onItemLongClick(BaseViewHolder viewHolder) {
                    dragHelper.startDrag(viewHolder);
                }
            });
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull UniqueItemManager data) {

        }

        @Override
        protected int getItemLayoutId() {                                                                               // 設置 子RecyclerView的Layout
            return R.layout.item_recycler_view2;
        }

        private List<Object> getItemList(int length) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (i == 0) {                                                                                           // 實現 子RecyclerView, Item 0 的呈現
                    list.add(new ImageDragBean(
                            R.drawable.e_02, false, false, false)
                    );
                }

                list.add( new ImageDragBean(R.drawable.a_05, true, false, true));                                       // 實現 子RecyclerView, Item 1~i 的呈現, 可隨意拖動 不可跨RecyclerView
            }
            return list;
        }
    }
}