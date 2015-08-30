package com.sarltokyo.addressbookormlite12;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;
import com.sarltokyo.addressbookormlite12.db.OpenHelper;
import com.sarltokyo.addressbookormlite12.entity.Address;
import com.sarltokyo.addressbookormlite12.entity.Person;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osabe on 15/08/30.
 */
public class InjectedInstrumentationTest extends InstrumentationTestCase {
    private final static String TAG = InjectedInstrumentationTest.class.getSimpleName();

    @Inject
    public String mockDatabaseName;

    public OpenHelper mOpenHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        App app =
                (App)getInstrumentation().getTargetContext().getApplicationContext();
        app.setMockMode(true);
        app.component().inject(this);

        Context context = getInstrumentation().getTargetContext();
        mOpenHelper = new OpenHelper(context, mockDatabaseName);

        cleanData();
    }

    @Override
    protected void tearDown() throws Exception {
        App.getInstance().setMockMode(false);
        mOpenHelper.close();
        mOpenHelper = null;
    }

    public void cleanData() {
        Log.d(TAG, "cleanData");
        List<Person> persons = null;
        try {
            persons = mOpenHelper.findPerson();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }

        for (Person person : persons) {
            try {
                mOpenHelper.deletePerson(person.getName());
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public List<Person> createTestData(int personsNum) {
        Log.d(TAG, "createTestData");
        List<Person> persons = new ArrayList<Person>();
        // Creating dummy data.
        for (int i = 0; i < personsNum; i++) {
            Person person = new Person();
            Address address = new Address();
            address.setZipcode("123-456" + i);
            address.setPrefecture("Tokyo");
            address.setCity("Shinjyuku-ku");
            address.setOther("Higashi-shinjyuku 1-2-" + i);
            person.setName("Hoge" + i);
            person.setAddress(address);
            try {
                mOpenHelper.registerPerson(person);
                persons.add(person);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return persons;
    }
}
