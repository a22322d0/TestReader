package com.example.testreader.ui.reader.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testreader.R;
import com.example.testreader.ui.reader.ReaderFragment;
import com.example.testreader.utils.ImagePagerAdapter;

import java.io.File;
import java.util.List;

public class ReaderModeManager {

    public static final int MODE_SINGLE_PAGE_LEFT_TO_RIGHT = 0;
    public static final int MODE_SINGLE_PAGE_RIGHT_TO_LEFT = 1;
    public static final int MODE_SINGLE_PAGE_VERTICAL = 2;
    public static final int MODE_CONTINUOUS_VERTICAL = 3;
    public static final int MODE_SPACED_VERTICAL = 4;

    private Context context;
    private ViewGroup readerContainer;
    private List<File> imageFiles;
    private ImagePagerAdapter adapter;
    private int currentMode = MODE_SINGLE_PAGE_LEFT_TO_RIGHT;

    public ReaderModeManager(Context context, ViewGroup readerContainer, List<File> imageFiles) {
        this.context = context;
        this.readerContainer = readerContainer;
        this.imageFiles = imageFiles;
        this.adapter = new ImagePagerAdapter(context, imageFiles);
    }

    public void switchMode() {
        // 切换模式
        currentMode = (currentMode + 1) % 5;
        setupMode(currentMode);
        showModeToast(currentMode);
    }

    private void showModeToast(int mode) {
        String modeMessage = "";
        switch (mode) {
            case MODE_SINGLE_PAGE_LEFT_TO_RIGHT: modeMessage = "模式1: 从左至右"; break;
            case MODE_SINGLE_PAGE_RIGHT_TO_LEFT: modeMessage = "模式2: 从右至左"; break;
            case MODE_SINGLE_PAGE_VERTICAL: modeMessage = "模式3: 单页垂直滚动"; break;
            case MODE_CONTINUOUS_VERTICAL: modeMessage = "模式4: 连续垂直滚动"; break;
            case MODE_SPACED_VERTICAL: modeMessage = "模式5: 间隔垂直滚动"; break;
        }
        Toast.makeText(context, modeMessage, Toast.LENGTH_SHORT).show();
    }

    public void setupMode(int mode) {
        // 清空当前容器视图
        readerContainer.removeAllViews();

        if (mode == MODE_CONTINUOUS_VERTICAL || mode == MODE_SPACED_VERTICAL) {
            // 垂直滚动模式使用 RecyclerView
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

            // 如果是间隔模式，设置间隔装饰器
            if (mode == MODE_SPACED_VERTICAL) {
                recyclerView.addItemDecoration(new ReaderModeManager.SpacingItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.page_spacing)));
            }

            recyclerView.setAdapter(adapter);
            readerContainer.addView(recyclerView);

        } else {
            // 单页模式使用 ViewPager2
            ViewPager2 viewPager = new ViewPager2(context);

            // 设置方向为垂直或水平
            viewPager.setOrientation(mode == MODE_SINGLE_PAGE_VERTICAL ? ViewPager2.ORIENTATION_VERTICAL : ViewPager2.ORIENTATION_HORIZONTAL);

            // 如果是右至左阅读模式，设置 ViewPager2 的布局方向
            if (mode == MODE_SINGLE_PAGE_RIGHT_TO_LEFT) {
                viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

            viewPager.setAdapter(adapter);
            readerContainer.addView(viewPager);
        }

    }

    //======================= ui ================================

    // The SpacingItemDecoration class for adding space between items in RecyclerView
    public static class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacingItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            // Create a paint object and set the color to black
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            // Get all the child views of RecyclerView
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                // Calculate the bottom of the child view
                int top = child.getBottom();
                int bottom = top + space;

                // Draw a black rectangle to create spacing
                c.drawRect(child.getLeft(), top, child.getRight(), bottom, paint);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = space; // Set the bottom space between items
        }
    }
}


