// Generated by view binder compiler. Do not edit!
package com.example.testreader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.testreader.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button openReaderButton;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final Button selectEpubButton;

  @NonNull
  public final Button selectFileButton;

  @NonNull
  public final Button selectPdfButton;

  @NonNull
  public final ViewPager2 viewPager;

  private FragmentDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button openReaderButton, @NonNull ProgressBar progressBar,
      @NonNull Button selectEpubButton, @NonNull Button selectFileButton,
      @NonNull Button selectPdfButton, @NonNull ViewPager2 viewPager) {
    this.rootView = rootView;
    this.openReaderButton = openReaderButton;
    this.progressBar = progressBar;
    this.selectEpubButton = selectEpubButton;
    this.selectFileButton = selectFileButton;
    this.selectPdfButton = selectPdfButton;
    this.viewPager = viewPager;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.openReaderButton;
      Button openReaderButton = ViewBindings.findChildViewById(rootView, id);
      if (openReaderButton == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.selectEpubButton;
      Button selectEpubButton = ViewBindings.findChildViewById(rootView, id);
      if (selectEpubButton == null) {
        break missingId;
      }

      id = R.id.selectFileButton;
      Button selectFileButton = ViewBindings.findChildViewById(rootView, id);
      if (selectFileButton == null) {
        break missingId;
      }

      id = R.id.selectPdfButton;
      Button selectPdfButton = ViewBindings.findChildViewById(rootView, id);
      if (selectPdfButton == null) {
        break missingId;
      }

      id = R.id.viewPager;
      ViewPager2 viewPager = ViewBindings.findChildViewById(rootView, id);
      if (viewPager == null) {
        break missingId;
      }

      return new FragmentDashboardBinding((ConstraintLayout) rootView, openReaderButton,
          progressBar, selectEpubButton, selectFileButton, selectPdfButton, viewPager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
