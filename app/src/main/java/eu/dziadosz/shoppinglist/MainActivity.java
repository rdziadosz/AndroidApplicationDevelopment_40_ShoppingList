package eu.dziadosz.shoppinglist;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements AddProductDialogFragment.DialogListener {
    private LstFragment lstfragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstfragment = (LstFragment) getSupportFragmentManager().findFragmentByTag("lstfragment");
        if (lstfragment == null) {
            lstfragment = new LstFragment();
            FragmentTransaction transact = getSupportFragmentManager().beginTransaction();
            transact.add(R.id.list_fragment, lstfragment, "lstfragment");
            transact.commit();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int count, float price) {
        lstfragment.addItem(name, count, price);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // do nothing
    }

    public void addButtonClicked(View view) {
        AddProductDialogFragment eDialog = new AddProductDialogFragment();
        eDialog.show(getFragmentManager(), "Add a new product");
    }
}
