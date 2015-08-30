package com.sarltokyo.addressbookormlite12.asynctask;

import android.content.Context;
import com.sarltokyo.addressbookormlite12.InjectedInstrumentationTest;
import com.sarltokyo.addressbookormlite12.entity.Address;
import com.sarltokyo.addressbookormlite12.entity.Person;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by osabe on 15/08/30.
 */
public class PersonLoaderTest extends InjectedInstrumentationTest {

    private Context mContext;
    private PersonLoader mPersonLoader;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mPersonLoader = new PersonLoader(mContext, mockDatabaseName);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mPersonLoader = null;
    }

    public void testSuccess() throws Exception {
        createTestData(200);

        List<Person> actual = mPersonLoader.getPersons();

        for (int i =0; i < actual.size(); i++) {
            Person actualPerson = actual.get(i);
            String actuallName = actualPerson.getName();
            Address actualAddress = actualPerson.getAddress();
            String actualZipcode = actualAddress.getZipcode();
            String actualPrefecture = actualAddress.getPrefecture();
            String acutalCity = actualAddress.getCity();
            String actualOther = actualAddress.getOther();

            assertThat(actualZipcode, is("123-456" + i));
            assertThat(actualPrefecture, is(("Tokyo")));
            assertThat(acutalCity, is(("Shinjyuku-ku")));
            assertThat(actualOther, is("Higashi-shinjyuku 1-2-" + i));
            assertThat(actuallName, is("Hoge" + i));
        }
    }

    public void testEmptyData() throws Exception {
        cleanData();

        List<Person> actual = mPersonLoader.getPersons();
        assertTrue(actual.isEmpty());
    }
}
