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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testreader.R;
import com.example.testreader.ui.reader.model.ReaderModeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReaderFragment extends Fragment {

    private Button modeSwitchButton;
    private ViewGroup readerContainer;
    private List<File> imageFiles;
    private ReaderModeManager modeManager;

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

        // 初始化 ReaderModeManager
        modeManager = new ReaderModeManager(requireContext(), readerContainer, imageFiles);

        // 设置初始模式
        modeManager.setupMode(ReaderModeManager.MODE_SINGLE_PAGE_LEFT_TO_RIGHT);

        // 切换模式按钮点击事件
        modeSwitchButton.setOnClickListener(v -> modeManager.switchMode());

        return rootView;
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
