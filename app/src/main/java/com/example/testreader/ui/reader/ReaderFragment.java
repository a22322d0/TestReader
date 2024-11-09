package com.example.testreader.ui.reader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testreader.R;
import com.example.testreader.utils.ImagePagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReaderFragment extends Fragment {

    private static final int MODE_SINGLE_PAGE_LEFT_TO_RIGHT = 0;
    private static final int MODE_SINGLE_PAGE_RIGHT_TO_LEFT = 1;
    private static final int MODE_SINGLE_PAGE_VERTICAL = 2;
    private static final int MODE_CONTINUOUS_VERTICAL = 3;
    private static final int MODE_SPACED_VERTICAL = 4;


    private int currentMode = MODE_SINGLE_PAGE_LEFT_TO_RIGHT;

    private Button modeSwitchButton;
    private ViewGroup readerContainer; // 用来动态添加 ViewPager 或 RecyclerView

    private List<File> imageFiles; // 用于存储漫画图片文件列表
    private ImagePagerAdapter adapter;

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);

        // 获取根容器，用于动态添加视图
        readerContainer = rootView.findViewById(R.id.readerContainer);
        modeSwitchButton = rootView.findViewById(R.id.modeSwitchButton);

        // 获取漫画图片文件列表
        imageFiles = getComicPages(); // 你可以从某个数据源获取文件列表

        // 初始化适配器
        adapter = new ImagePagerAdapter(requireContext(), imageFiles);

        // 设置初始模式
        setupMode(currentMode);

        // 切换模式按钮点击事件
        modeSwitchButton.setOnClickListener(v -> switchMode());

        // 在 ReaderFragment 的 onCreateView 中，设置全屏模式
        //setFullScreen();
        // 设置全屏
        if (rootView != null) {
            enableFullScreenMode(rootView); // 将视图传递给方法
        }
        //enableFullScreenMode();
        // 点击屏幕中间显示/隐藏模式切换按钮
        //rootView.setOnClickListener(v -> toggleModeSwitchButton());

        return rootView;
    }


    private void enableFullScreenMode(View rootView) {
            View openReaderButton = rootView.findViewById(R.id.openReaderButton);
            if (openReaderButton != null) {
                // 启用全屏的逻辑
                requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );

                // 隐藏按钮
                Button modeSwitchButton = getView().findViewById(R.id.modeSwitchButton);
                modeSwitchButton.setVisibility(View.GONE);
                Button selectFileButton = getView().findViewById(R.id.selectFileButton);
                modeSwitchButton.setVisibility(View.GONE);
//                Button openReaderButton = getView().findViewById(R.id.openReaderButton);
//                modeSwitchButton.setVisibility(View.GONE);
            } else {
                Log.e("ReaderFragment", "未找到全屏按钮！");
            }
        }
        // 启用沉浸式全屏模式，隐藏状态栏和导航栏



    private void disableFullScreenMode() {
        // 退出全屏，显示按钮
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_VISIBLE
        );

        Button modeSwitchButton = getView().findViewById(R.id.modeSwitchButton);
        modeSwitchButton.setVisibility(View.VISIBLE);
    }

//    private void setFullScreen() {
//        // 设置全屏
////        getActivity().getWindow().getDecorView().setSystemUiVisibility(
////                View.SYSTEM_UI_FLAG_FULLSCREEN |
////                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
////                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
////        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
////                View.SYSTEM_UI_FLAG_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
////                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////        );
//        // 设置全屏沉浸模式，隐藏状态栏和导航栏
//        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        );
//    }


    private void toggleModeSwitchButton() {
        // 切换模式切换按钮的可见性
        if (modeSwitchButton.getVisibility() == View.VISIBLE) {
            // 隐藏按钮
            modeSwitchButton.setVisibility(View.INVISIBLE);
        } else {
            // 显示按钮
            modeSwitchButton.setVisibility(View.VISIBLE);

            // 延迟隐藏按钮，模拟自动隐藏的效果
            //modeSwitchButton.postDelayed(() -> modeSwitchButton.setVisibility(View.INVISIBLE), 2000); // 2秒后隐藏
        }
    }


    private void switchMode() {
        // 切换模式
        currentMode = (currentMode + 1) % 5;  // 从 0 到 4 循环切换模式
        setupMode(currentMode);
    }

    private void setupMode(int mode) {
        // 清空当前容器视图
        readerContainer.removeAllViews();

        if (mode == MODE_CONTINUOUS_VERTICAL || mode == MODE_SPACED_VERTICAL) {
            // 垂直滚动模式使用 RecyclerView
            RecyclerView recyclerView = new RecyclerView(requireContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

            // 如果是间隔模式，设置间隔装饰器
            if (mode == MODE_SPACED_VERTICAL) {
                recyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.page_spacing)));
            }

            recyclerView.setAdapter(adapter);
            readerContainer.addView(recyclerView);

        } else {
            // 单页模式使用 ViewPager2
            ViewPager2 viewPager = new ViewPager2(requireContext());

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


    private List<File> getComicPages() {
        // 从 Fragment 的 arguments 中获取传递的数据
        Bundle arguments = getArguments();
        if (arguments != null) {
            return (List<File>) arguments.getSerializable("imageFiles");
        }
        return new ArrayList<>();  // 返回一个空列表，以防没有传递数据
    }

    //======================= ui ================================
    public static class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacingItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            // 创建画笔并设置颜色为黑色
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);  // 使用黑色

            // 获取 RecyclerView 中所有的 child view
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                // 获取子项的底部坐标
                int top = child.getBottom();
                int bottom = top + space;

                // 绘制间隔色块（黑色）
                c.drawRect(child.getLeft(), top, child.getRight(), bottom, paint);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = space; // 设置底部间距
        }
    }
}