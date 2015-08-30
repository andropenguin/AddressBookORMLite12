package com.sarltokyo.addressbookormlite12.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.sarltokyo.addressbookormlite12.App;
import com.sarltokyo.addressbookormlite12.adapter.AdapterEx;
import com.sarltokyo.addressbookormlite12.asynctask.PersonLoader;
import com.sarltokyo.addressbookormlite12.db.OpenHelper;
import com.sarltokyo.addressbookormlite12.entity.Person;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osabe on 15/08/21.
 */
public class PersonListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Person>> {
    private final static String TAG = PersonListFragment.class.getSimpleName();

    private List<Person> mItems;
    private ListView mListView;
    private AdapterEx mPersonsAdapter;
    private ProgressDialog mProgressDialog;
    private OpenHelper mOpenHelper;

    @Inject
    public String mDatabaseName;

    // アイテムがタップされたときのリスナー
    public interface OnListItemClickListener {
        public void onListItemClick(int position, Person item);
    }

    // アイテムがタップされたときのリスナー
    private OnListItemClickListener mOnListItemClickListener;

    // アイテムがタップされたときのリスナーをセット
    public void setOnListItemClickListener(OnListItemClickListener l) {
        mOnListItemClickListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mDatabaseName = MainActivity.DATABASE_NAME;
        App.getInstance().component().inject(this);

        mOpenHelper = new OpenHelper(getActivity(), mDatabaseName);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        showProgress();
        LoaderManager manager = getActivity().getSupportLoaderManager();
        if (manager.getLoader(0) != null) {
            manager.destroyLoader(0);
        }
        manager.initLoader(0, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        mItems = new ArrayList<Person>();
        mPersonsAdapter = new AdapterEx(getActivity(), mItems);

        // アダプタをリストビューにセットする
        setListAdapter(mPersonsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.flagment_main, container, false);
        mListView = (ListView)view.findViewById(android.R.id.list);
        return view;
    }

    // リストのアイテムがタップされたときに呼び出される
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        showDetail(position);
    }

    private void showDetail(int position) {
        // タップされたときのリスナーのメソッドを呼び出す
        if (mOnListItemClickListener != null) {
            Person item = (Person)(mPersonsAdapter.getItem(position));
            mOnListItemClickListener.onListItemClick(position, item);
        }
    }

    private void showProgress() {
        Log.d(TAG, "showProcess()");
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("running");
        mProgressDialog.show();
    }

    private void dismissProgress() {
        Log.d(TAG, "dismissProgress()");
        mProgressDialog.dismiss();
    }

    @Override
    public Loader<List<Person>> onCreateLoader(int id, Bundle args) {
        return new PersonLoader(getActivity(), mDatabaseName);
    }

    @Override
    public void onLoadFinished(Loader<List<Person>> loader, List<Person> data) {
        Log.d(TAG, "PersonLoader finished");
        dismissProgress();

        if (data != null) {
            mPersonsAdapter = new AdapterEx(getActivity(), data);
            mListView.setAdapter(mPersonsAdapter);

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return deletePerson(view);
                }
            });
        } else {
            // todo
            Toast.makeText(getActivity(), "Some error occured.", Toast.LENGTH_LONG).show();;
        }

        LoaderManager manager = getActivity().getSupportLoaderManager();
        // 既にローダーがある場合は破棄
        if (manager.getLoader(0) != null) {
            manager.destroyLoader(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Person>> loader) {
        // nop
    }

    private boolean deletePerson(View view) {
        int positon;

        if (!(view.getTag() instanceof Integer)) {
            return true;
        }
        positon = (Integer)view.getTag();
        if (positon < 0 | mPersonsAdapter.getCount() <= positon) {
            return true;
        }

        Person person = mPersonsAdapter.getItem(positon);
        try {
            boolean isDeleted = mOpenHelper.deletePerson(person.getName());
            if (isDeleted) {
                Toast.makeText(getActivity(), person.getName() + " was deleted.", Toast.LENGTH_LONG).show();

                List<Person> list = mOpenHelper.findPerson();
                mPersonsAdapter = new AdapterEx(getActivity(), list);
                mListView.setAdapter(mPersonsAdapter);
                mPersonsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), person.getName() + " cannot be deleted.", Toast.LENGTH_LONG).show();
            }
            return true;
        } catch (SQLException e) {
            Toast.makeText(getActivity(), person.getName() + " cannot be deleted.", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
            return true;
        }
    }
}
