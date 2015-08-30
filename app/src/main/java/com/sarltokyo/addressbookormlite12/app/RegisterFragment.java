package com.sarltokyo.addressbookormlite12.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sarltokyo.addressbookormlite12.App;
import com.sarltokyo.addressbookormlite12.db.OpenHelper;;
import com.sarltokyo.addressbookormlite12.entity.Address;
import com.sarltokyo.addressbookormlite12.entity.Person;

import javax.inject.Inject;
import java.sql.SQLException;

/**
 * Created by osabe on 15/08/21.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = RegisterFragment.class.getSimpleName();

    private OpenHelper mOpenHelper;
    private Button mBtn;
    private String mType;
    private String mName;

    private EditText mNameEt;
    private EditText mZipCodeEt;
    private EditText mPrefectureEt;
    private EditText mCityEt;
    private EditText mOtherEt;

    @Inject
    public String mDatabaseName;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mBtn = (Button)view.findViewById(R.id.btn);
        mBtn.setOnClickListener(this);

        mNameEt = ((EditText)view.findViewById(R.id.nameEt));
        mZipCodeEt = ((EditText)view.findViewById(R.id.zipcodeEt));
        mPrefectureEt = ((EditText)view.findViewById(R.id.prefectureEt));
        mCityEt = ((EditText)view.findViewById(R.id.cityEt));
        mOtherEt = ((EditText)view.findViewById(R.id.otherEt));

        Resources resources = getResources();
        mType = getArguments().getString(MainActivity.TYPE);
        if (mType.equals(MainActivity.CREATE_DATA_TYPE)) {
            mBtn.setText(resources.getString(R.string.register));
        } else if (mType.equals(MainActivity.UPDATE_DATA_TYPE)) {
            mName = getArguments().getString("name");
            mBtn.setText(resources.getString(R.string.update));
            showAddress(mName);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn) {
            if (mType.equals(MainActivity.CREATE_DATA_TYPE)) {
                registerAddressBook();
            } else if (mType.equals(MainActivity.UPDATE_DATA_TYPE)) {
                updateAddressBook(mName);
            }
        }
    }

    public void registerAddressBook() {
        String name = mNameEt.getText().toString();
        String zipcode = mZipCodeEt.getText().toString();
        String prefecture = mPrefectureEt.getText().toString();
        String city = mCityEt.getText().toString();
        String other = mOtherEt.getText().toString();

        if (!checkData(name, zipcode, prefecture, city, other)) {
            return;
        }

        Address address = new Address();
        address.setZipcode(zipcode);
        address.setPrefecture(prefecture);
        address.setCity(city);
        address.setOther(other);
        Person person = new Person();
        person.setName(name);
        person.setAddress(address);
        try {
            mOpenHelper.registerPerson(person);
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (SQLException e) {
            Toast.makeText(getActivity(), "cannot register address.", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }
    }


    public void updateAddressBook(String name) {

        String newName = mNameEt.getText().toString();
        String zipcode = mZipCodeEt.getText().toString();
        String prefecture = mPrefectureEt.getText().toString();
        String city = mCityEt.getText().toString();
        String other = mOtherEt.getText().toString();


        if (!checkData(name, zipcode, prefecture, city, other)) {
            return;
        }

        try {
            Person person = mOpenHelper.findPerson(name);
            Address address = person.getAddress();
            address.setZipcode(zipcode);
            address.setPrefecture(prefecture);
            address.setCity(city);
            address.setOther(other);
            person.setName(newName);
            person.setAddress(address);
            mOpenHelper.updatePerson(person);
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (SQLException e) {
            Toast.makeText(getActivity(), "cannot update address.", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }
    }


    public void showAddress(String name) {
        Person person;
        Address address;

        try {
            person = mOpenHelper.findPerson(name);

        } catch (SQLException e) {
            // ありえない
            Log.e(TAG, e.getMessage());
            return;
        }

        address = person.getAddress();

        String zipcode = address.getZipcode();
        String prefecture = address.getPrefecture();
        String city = address.getCity();
        String other = address.getOther();

        mNameEt.setText(name);
        mZipCodeEt.setText(zipcode);
        mPrefectureEt.setText(prefecture);
        mCityEt.setText(city);
        mOtherEt.setText(other);
    }

    public boolean checkData(String name, String zipcode, String prefecture,
                             String city, String other) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "name is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(getActivity(), "zipcode is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(prefecture)) {
            Toast.makeText(getActivity(), "prefecture is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getActivity(), "city is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(other)) {
            Toast.makeText(getActivity(), "other is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
