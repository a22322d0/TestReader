package com.example.testreader.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testreader.R;
import com.example.testreader.utils.CbzExtractor;
import com.example.testreader.utils.FilePicker;
import com.example.testreader.utils.ImagePagerAdapter;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testreader.R;
import com.example.testreader.utils.CbzExtractor;
import com.example.testreader.utils.FilePicker;
import com.example.testreader.utils.ImagePagerAdapter;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private List<File> imageFiles; // 用于存储解压的图片文件
    private ViewPager2 viewPager;  // 使用类级别的变量
    private ImagePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到 ViewPager2 和按钮并设置点击监听器
        viewPager = view.findViewById(R.id.viewPager); // 确保在你的 XML 布局中有对应的 ViewPager2
        if (viewPager == null) {
            Log.e("HomeFragment", "ViewPager2 is null");
        }

        // 找到按钮并设置点击监听器
        Button selectFileButton = view.findViewById(R.id.selectFileButton);

        // 初始化适配器并设置给 ViewPager2
        adapter = new ImagePagerAdapter(getContext(), new ArrayList<>());
        viewPager.setAdapter(adapter);

        selectFileButton.setOnClickListener(v -> openFilePicker());



        return view;
    }

    private void openFilePicker() {
        // 从 Fragment 中调用 FilePicker
        FilePicker.openFilePicker(this);
    }

    // 处理文件选择结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = FilePicker.handleFileResult(requestCode, resultCode, data);
        if (uri != null) {
            // 在这里处理选择的文件（例如，解压）
            File destinationDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CBZImages");
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            // 使用 CbzExtractor 解压文件
            imageFiles = CbzExtractor.extract(uri, destinationDir , getContext());

            // 设置适配器并更新 ViewPager
            adapter = new ImagePagerAdapter(getContext(), imageFiles);
            viewPager.setAdapter(adapter);



        }
    }
}


/*
public class HomeFragment extends Fragment {

    private List<File> imageFiles; // 用于存储解压的图片文件
    private ViewPager2 viewPager;
    private ImagePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到 ViewPager2 和按钮并设置点击监听器
        //ViewPager2 viewPager = view.findViewById(R.id.viewPager); // 确保在你的 XML 布局中有对应的 ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        if (viewPager == null) {
            Log.e("HomeFragment", "ViewPager2 is null");
        }

        // 找到按钮并设置点击监听器
        Button selectFileButton = view.findViewById(R.id.selectFileButton);

        // 初始化适配器并设置给 ViewPager2
        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), new ArrayList<>());
        viewPager.setAdapter(adapter);

        selectFileButton.setOnClickListener(v -> openFilePicker());

        return view;
    }

    private void openFilePicker() {
        // 从 Fragment 中调用 FilePicker
        FilePicker.openFilePicker(this);
    }

    // 处理文件选择结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = FilePicker.handleFileResult(requestCode, resultCode, data);
        if (uri != null) {
            // 在这里处理选择的文件（例如，解压）
            File destinationDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CBZImages");
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
            // 使用 CbzExtractor 解压文件
            imageFiles = CbzExtractor.extract(uri, destinationDir , getContext());

            // 设置适配器并更新 ViewPager
            adapter = new ImagePagerAdapter(getContext(), imageFiles);
            viewPager.setAdapter(adapter);
        }
    }
}
*/


/*
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testreader.R;
import com.example.testreader.databinding.FragmentHomeBinding;
import com.example.testreader.utils.FilePicker;

public class HomeFragment extends Fragment {

//    private FragmentHomeBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到按钮并设置点击监听器
        Button selectFileButton = view.findViewById(R.id.selectFileButton);
        selectFileButton.setOnClickListener(v -> {
            // 处理按钮点击事件，例如调用文件选择
            openFilePicker();
        });

        return view;
    }

    private void openFilePicker() {
        // 执行文件选择的逻辑，例如调用 FilePicker.openFilePicker(getActivity());


    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 找到选择文件的按钮并设置点击监听器
//        Button selectFileButton = findViewById(R.id.selectFileButton);
//        selectFileButton.setOnClickListener(v -> FilePicker.openFilePicker(this));
//    }
}

//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//public class DashboardFragment extends Fragment {
//
//
//}

*/