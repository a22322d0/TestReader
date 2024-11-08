package com.example.testreader.ui.reader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private static final int MODE_SINGLE_PAGE_HORIZONTAL = 0;
    private static final int MODE_SINGLE_PAGE_VERTICAL = 1;
    private static final int MODE_CONTINUOUS_VERTICAL = 2;
    private static final int MODE_SPACED_VERTICAL = 3;

    private int currentMode = MODE_SINGLE_PAGE_HORIZONTAL;

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

        return rootView;
    }

    private void switchMode() {
        // 切换模式
        currentMode = (currentMode + 1) % 4;  // 从 0 到 3 循环切换模式
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
            viewPager.setOrientation(mode == MODE_SINGLE_PAGE_VERTICAL ? ViewPager2.ORIENTATION_VERTICAL : ViewPager2.ORIENTATION_HORIZONTAL);
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