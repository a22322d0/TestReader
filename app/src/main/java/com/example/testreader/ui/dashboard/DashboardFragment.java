package com.example.testreader.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;


import com.example.testreader.R;
import com.example.testreader.utils.Epub2Zip;
import com.example.testreader.utils.FilePicker;
import com.example.testreader.utils.FileSelector;
import com.example.testreader.utils.FileUtil;
import com.example.testreader.utils.ImagePagerAdapter;
import com.example.testreader.utils.Pdf2Zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements FileSelector.OnFileSelectedListener {

    private List<File> imageFiles; // 用于存储解压的图片文件
    private ViewPager2 viewPager;  // 使用类级别的变量
    private ImagePagerAdapter adapter;

    private Button selectPdfButton;
    private Button selectEpubButton;
    private ProgressBar progressBar;

    private FileSelector fileSelector;

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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewPager = view.findViewById(R.id.viewPager); // 确保在你的 XML 布局中有对应的 ViewPager2
        if (viewPager == null) {
            Log.e("HomeFragment", "ViewPager2 is null");
        }

        //Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        //((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        Button selectFileButton = view.findViewById(R.id.selectFileButton);
        adapter = new ImagePagerAdapter(getContext(), new ArrayList<>());
        viewPager.setAdapter(adapter);

        selectFileButton.setOnClickListener(v -> openFilePicker());

        // 跳转按钮
        Button openReaderButton = view.findViewById(R.id.openReaderButton);
        openReaderButton.setOnClickListener(v -> openReaderFragment());


        selectPdfButton = view.findViewById(R.id.selectPdfButton);
        selectEpubButton = view.findViewById(R.id.selectEpubButton);
        progressBar = view.findViewById(R.id.progressBar);

        // 初始化 FileSelector
        fileSelector = new FileSelector(this);
        // 设置按钮点击事件
        selectPdfButton.setOnClickListener(v -> fileSelector.selectPdfFile(this));
        selectEpubButton.setOnClickListener(v -> fileSelector.selectEpubFile(this));

        return view;
    }

    private void openFilePicker() {
        // 从 Fragment 中调用 FilePicker
        Intent intent = FilePicker.createFilePickerIntent();
        filePickerLauncher.launch(intent);
    }

    public void onFileSelected(Uri fileUri, String fileType) {
        Context context = getContext();
        if (fileType.equals("pdf")) {
            Pdf2Zip.processPdf(fileUri, progressBar, context);
        } else if (fileType.equals("epub")) {
            Epub2Zip.processEpub(fileUri, progressBar, context);
        }
    }

    private void openReaderFragment() {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // 获取 NavController
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            // 隐藏 HomeFragment 中的按钮
            hideHomeButtons();

            Bundle bundle = new Bundle();
            bundle.putSerializable("imageFiles", new ArrayList<>(imageFiles));  // 将 imageFiles 传递到 ReaderFragment

            // 跳转到 ReaderFragment
            navController.navigate(R.id.readerFragment, bundle);
        } else {
            Log.e("HomeFragment", "No images to display in ReaderFragment");
        }
    }

    private void hideHomeButtons() {
        Button openReaderButton = getView().findViewById(R.id.openReaderButton);
        Button selectFileButton = getView().findViewById(R.id.selectFileButton);
        if (openReaderButton != null && selectFileButton != null ) {
            openReaderButton.setVisibility(View.GONE);  // 隐藏按钮
            selectFileButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileSelector.handleActivityResult(requestCode, resultCode, data, getContext());
    }

}