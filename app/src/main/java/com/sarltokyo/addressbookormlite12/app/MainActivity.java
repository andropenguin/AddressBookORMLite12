package com.sarltokyo.addressbookormlite12.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.sarltokyo.addressbookormlite12.App;
import com.sarltokyo.addressbookormlite12.db.OpenHelper;
import com.sarltokyo.addressbookormlite12.entity.Person;

import javax.inject.Inject;
import java.sql.SQLException;

/**
 * Created by osabe on 15/08/21.
 */
public class MainActivity extends AppCompatActivity
        implements PersonListFragment.OnListItemClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private OpenHelper mOpenHelper;

    public final static String TYPE = "type";
    public final static String CREATE_DATA_TYPE = "create";
    public final static String UPDATE_DATA_TYPE = "update";

    public final static String DATABASE_NAME = "addressbook.db";

    @Inject
    public String mDatabaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mDatabaseName = DATABASE_NAME;
        App.getInstance().component().inject(this);

        mOpenHelper = new OpenHelper(this, mDatabaseName);

        FragmentManager manager = getSupportFragmentManager();

        //既にFragmentが作成されているかチェック
        if (manager.findFragmentByTag("list_fragment_tag") == null) {
            Log.d(TAG, "PersonListFragment created");
            FragmentTransaction ft = manager.beginTransaction();
            PersonListFragment fragment = new PersonListFragment();
            ft.add(R.id.my_fragment, fragment, "list_fragment_tag");
            ft.commit();
            // リストのアイテムがタップされたときに呼び出されるリスナーをセット
            fragment.setOnListItemClickListener(this);
        } else {
            PersonListFragment fragment = (PersonListFragment)manager.findFragmentByTag("list_fragment_tag");
            // リストのアイテムがタップされたときに呼び出されるリスナーをセット
            fragment.setOnListItemClickListener(this);
        }
    }

    @Override
    public void onListItemClick(int position, Person person) {
        String name = null;
        try {
            name = mOpenHelper.findPerson(person.getName()).getName();
            Log.d(TAG, "name in updateAddress = " + name);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        if (name == null) return; // todo: 多分、ありえない

        RegisterFragment registerFragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, UPDATE_DATA_TYPE);
        args.putString("name", name);
        registerFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.my_fragment, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.input_add) {
            RegisterFragment registerFragment = new RegisterFragment();
            Bundle args = new Bundle();
            args.putString(TYPE, CREATE_DATA_TYPE);
            registerFragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.my_fragment, registerFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
