package com.example.testreader.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testreader.R;
import com.example.testreader.utils.FilePicker;
import com.example.testreader.utils.FileUtil;
import com.example.testreader.utils.ImagePagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<File> imageFiles; // 用于存储解压的图片文件
    private ViewPager2 viewPager;  // 使用类级别的变量
    private ImagePagerAdapter adapter;

    // 声明 ActivityResultLauncher
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == -1 && result.getData() != null) {
                    Uri uri = FilePicker.handleFileResult(result.getData());
                    if (uri != null) {
                        // 在这里处理选择的文件（例如，解压）
                        imageFiles = FileUtil.extractImagesFromCBZ(uri, getContext());

                        // 设置适配器并更新 ViewPager
                        adapter = new ImagePagerAdapter(getContext(), imageFiles);
                        viewPager.setAdapter(adapter);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager); // 确保在你的 XML 布局中有对应的 ViewPager2
        if (viewPager == null) {
            Log.e("HomeFragment", "ViewPager2 is null");
        }

        Button selectFileButton = view.findViewById(R.id.selectFileButton);
        adapter = new ImagePagerAdapter(getContext(), new ArrayList<>());
        viewPager.setAdapter(adapter);

        selectFileButton.setOnClickListener(v -> openFilePicker());

        // 跳转按钮
        Button openReaderButton = view.findViewById(R.id.openReaderButton);
        openReaderButton.setOnClickListener(v -> openReaderFragment());

        return view;
    }

    private void openFilePicker() {
        // 从 Fragment 中调用 FilePicker
        Intent intent = FilePicker.createFilePickerIntent();
        filePickerLauncher.launch(intent);
    }

    private void openReaderFragment() {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // 获取 NavController
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("imageFiles", new ArrayList<>(imageFiles));  // 将 imageFiles 传递到 ReaderFragment

            // 跳转到 ReaderFragment 并传递数据
            navController.navigate(R.id.readerFragment, bundle);
        } else {
            Log.e("HomeFragment", "No images to display in ReaderFragment");
        }
    }
}
