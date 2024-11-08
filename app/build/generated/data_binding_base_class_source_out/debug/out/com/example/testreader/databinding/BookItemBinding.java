// Generated by view binder compiler. Do not edit!
package com.example.testreader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.testreader.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BookItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView bookAuthor;

  @NonNull
  public final ImageView bookCover;

  @NonNull
  public final TextView bookTitle;

  private BookItemBinding(@NonNull LinearLayout rootView, @NonNull TextView bookAuthor,
      @NonNull ImageView bookCover, @NonNull TextView bookTitle) {
    this.rootView = rootView;
    this.bookAuthor = bookAuthor;
    this.bookCover = bookCover;
    this.bookTitle = bookTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static BookItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BookItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.book_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BookItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.book_author;
      TextView bookAuthor = ViewBindings.findChildViewById(rootView, id);
      if (bookAuthor == null) {
        break missingId;
      }

      id = R.id.book_cover;
      ImageView bookCover = ViewBindings.findChildViewById(rootView, id);
      if (bookCover == null) {
        break missingId;
      }

      id = R.id.book_title;
      TextView bookTitle = ViewBindings.findChildViewById(rootView, id);
      if (bookTitle == null) {
        break missingId;
      }

      return new BookItemBinding((LinearLayout) rootView, bookAuthor, bookCover, bookTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
