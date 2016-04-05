package org.kiwix.kiwixmobile.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import org.kiwix.kiwixmobile.R;

public class BookmarksActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {

  private ArrayList<String> contents;
  private ListView bookmarksList;
  private ArrayAdapter adapter;
  private ArrayList<String> selected;
  private int numOfSelected;
  SparseBooleanArray sparseBooleanArray;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bookmarks);
    setUpToolbar();
    contents = getIntent().getStringArrayListExtra("bookmark_contents");
    selected = new ArrayList<>();
    bookmarksList = (ListView) findViewById(R.id.bookmarks_list);
    adapter = new ArrayAdapter(getApplicationContext(), R.layout.bookmarks_row, R.id.bookmark_title,
        contents);
    bookmarksList.setAdapter(adapter);
    bookmarksList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    bookmarksList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
      @Override public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
          boolean checked) {
        // TODO: make item greyed out when checked (selected)
        if (checked) {
          selected.add(contents.get(position));
          numOfSelected++;
          mode.setTitle(numOfSelected + " Selected");
        } else if (selected.contains(contents.get(position))) {
          selected.remove(contents.get(position));
          numOfSelected--;
          mode.setTitle(numOfSelected + " Selected");
        }
        //sparseBooleanArray = bookmarksList.getCheckedItemPositions();
        //adapter.setSparse(sparseBooleanArray);
        //adapter.notifyDataSetChanged();

      }

      @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_bookmarks, menu);
        numOfSelected = 0;
        return true;
      }

      @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
      }

      @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {
          case R.id.menu_bookmarks_delete:
            deleteSelectedItems();
            mode.finish();
            return true;
          default:
            return false;
        }
      }

      @Override public void onDestroyActionMode(ActionMode mode) {

      }
    });
    bookmarksList.setOnItemClickListener(this);
  }

  private void deleteSelectedItems() {
    sparseBooleanArray = bookmarksList.getCheckedItemPositions();
    for (int i = sparseBooleanArray.size() - 1; i >= 0; i--)
      contents.remove(sparseBooleanArray.keyAt(i));

    adapter.notifyDataSetChanged();
  }

  private void setUpToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getResources().getString(R.string.menu_bookmarks_list));
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("bookmarks_array_list", contents);
        intent.putExtra("bookmarkClicked", false);
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent();
    intent.putExtra("choseX", contents.get(position));
    intent.putExtra("bookmarks_array_list", contents);
    intent.putExtra("bookmarkClicked", true);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override public void onBackPressed() {
    Intent intent = new Intent();
    intent.putExtra("bookmarks_array_list", contents);
    intent.putExtra("bookmarkClicked", false);
    setResult(RESULT_OK, intent);
    finish();
    super.onBackPressed();
  }
}