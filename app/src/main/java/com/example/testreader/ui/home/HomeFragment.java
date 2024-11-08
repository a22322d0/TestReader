package com.example.testreader.ui.home;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.testreader.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.bookshelf_gridview);

        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(R.drawable.book1, "黄沙之下","crab"));
        books.add(new Book(R.drawable.book2, "剑来","烽火戏诸侯"));
        books.add(new Book(R.drawable.book3, "我在精神病院学斩神","三九音域"));
        books.add(new Book(R.drawable.book4, "深空彼岸","辰东"));
        books.add(new Book(R.drawable.book5, "一剑独尊","青鸾峰上"));
        books.add(new Book(R.drawable.book6, "仙武帝尊","六界三道"));
        books.add(new Book(R.drawable.book7, "重生回1983当富翁","恩怨各一半"));
        books.add(new Book(R.drawable.book8, "雪中悍刀行","烽火戏诸侯"));


        BookAdapter adapter = new BookAdapter(getActivity(), books);

        gridView.setAdapter(adapter);

        return view;
    }

    public class Book {
        private final int mCover;
        private final String mTitle;

        private final String mAuthor;

        public Book(int cover, String title, String Author) {
            this.mCover = cover;
            this.mTitle = title;
            this.mAuthor = Author;
        }

        public int getCover() {
            return mCover;
        }

        public String getTitle() {
            return mTitle;
        }
        public String getAuthor() {
            return mAuthor;
        }
    }

    public class BookAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<Book> mBooks;

        public BookAdapter(Context context, ArrayList<Book> books) {
            this.mContext = context;
            this.mBooks = books;
        }

        @Override
        public int getCount() {
            return mBooks.size();
        }

        @Override
        public Object getItem(int position) {
            return mBooks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
            }

            ImageView cover = (ImageView) convertView.findViewById(R.id.book_cover);
            TextView title = (TextView) convertView.findViewById(R.id.book_title);
            TextView author = (TextView) convertView.findViewById(R.id.book_author);

            Book book = mBooks.get(position);

            cover.setImageResource(book.getCover());
            title.setText(book.getTitle());
            author.setText(book.getAuthor());

            return convertView;
        }
    }

}