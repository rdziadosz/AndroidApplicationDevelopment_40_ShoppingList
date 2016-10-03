package eu.dziadosz.shoppinglist;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static eu.dziadosz.shoppinglist.DatabaseOpenHelper.DATABASE_TABLE;

/**
 * Created by Radosław on 02.10.2016.
 */

public class LstFragment extends ListFragment {

    private final int DELETE_ID = 0;
    private SQLiteDatabase db;
    private Cursor cursor;
    private final String DATABASE_TABLE = DatabaseOpenHelper.DATABASE_TABLE;
    View mheaderView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment, container, false);
        mheaderView = inflater.inflate(R.layout.list_header, null);

        //  Retain the ListFragment instance across Activity re-creation
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get database instance
        db = (new DatabaseOpenHelper(getActivity())).getWritableDatabase();
        // get data with own made queryData method
        queryData();
    }

    public void addItem(String name, int count, float price) {

        ContentValues values = new ContentValues(3);
        values.put("name", name);
        values.put("count", count);
        values.put("price", price);
        db.insert(DATABASE_TABLE, null, values);

        // get data again
        queryData();
        // show toast
        showTotalPrice();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //long press on item
        registerForContextMenu(getListView());
        //add header
        if (mheaderView != null) getListView().addHeaderView(mheaderView, null, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String[] args = {String.valueOf(info.id)};
                db.delete(DATABASE_TABLE, "_id=?", args);
                queryData();
                showTotalPrice();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void queryData() {
        // get data with query
        String[] resultColumns = new String[]{"_id", "name", "count", "price"};
        cursor = db.query(DATABASE_TABLE, resultColumns, null, null, null, null, "name ASC", null);

        // add data to adapter
        ListAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.row, cursor,
                new String[]{"name", "count", "price"},      // from
                new int[]{R.id.name, R.id.count, R.id.price}    // to
                , 0);  // flags

        // show data in listView
        getListView().setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // close cursor and db connection
        cursor.close();
        db.close();
    }

    public void showTotalPrice() {
        float price = 0f;
        if (cursor.moveToFirst()) {
            do {
                float itemprice = cursor.getFloat(2) * cursor.getFloat(3);
                price += itemprice;
            } while (cursor.moveToNext());
            Toast.makeText(getActivity().getBaseContext(), "Total price: " + price, Toast.LENGTH_SHORT).show();
        }
    }
}