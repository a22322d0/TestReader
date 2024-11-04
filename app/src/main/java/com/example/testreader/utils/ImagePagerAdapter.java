package com.example.testreader.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testreader.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
    private List<File> imageFiles; // 将 List<File> 设为可更新的
    private final Context context;

    public ImagePagerAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles != null ? imageFiles : new ArrayList<>(); // 确保不为空
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_page, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Picasso.get().load(imageFile).fit().centerInside().into(holder.imageView); // 使用 Picasso 加载图片
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    // 添加一个更新图像列表的方法
    public void updateImages(List<File> newImageFiles) {
        this.imageFiles = newImageFiles != null ? newImageFiles : new ArrayList<>(); // 更新图像文件列表
        notifyDataSetChanged(); // 通知适配器数据已更改
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // 对应 item_image_page.xml 中的 ImageView
        }
    }
}


/*
//public class ImagePagerAdapter {
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testreader.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
    private final List<File> imageFiles;
    private final Context context;

    public ImagePagerAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_page, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Picasso.get().load(imageFile).fit().centerInside().into(holder.imageView); // 使用 Picasso 加载图片
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // 对应 item_image_page.xml 中的 ImageView
        }
    }
}
*/
