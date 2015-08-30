package com.sarltokyo.addressbookormlite12.db;

import com.sarltokyo.addressbookormlite12.InjectedInstrumentationTest;
import com.sarltokyo.addressbookormlite12.entity.Address;
import com.sarltokyo.addressbookormlite12.entity.Person;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by osabe on 15/08/30.
 */
public class OpenHelperTest extends InjectedInstrumentationTest {

    public void testInsertSelect()throws Exception{
        assertEquals(0, mOpenHelper.findPerson().size());

        Person person = new Person();
        Address address = new Address();
        address.setZipcode("123-4567");
        address.setPrefecture("Tokyo");
        address.setCity("Shinjyuku-ku");
        address.setOther("Higashi-shinjyuku 1-2-3");
        person.setName("Foo");
        person.setAddress(address);
        // insert
        mOpenHelper.registerPerson(person);
        // select
        Person sut = mOpenHelper.findPerson("Foo");

        assertThat(sut.getAddress().getZipcode(), is("123-4567"));
        assertThat(sut.getAddress().getPrefecture(), is("Tokyo"));
        assertThat(sut.getAddress().getCity(), is("Shinjyuku-ku"));
        assertThat(sut.getAddress().getOther(), is("Higashi-shinjyuku 1-2-3"));
    }

    public void testDuplicate() throws  Exception {
        assertEquals(0, mOpenHelper.findPerson().size());

        Person person = new Person();
        Address address = new Address();
        address.setZipcode("123-4567");
        address.setPrefecture("Tokyo");
        address.setCity("Shinjyuku-ku");
        address.setOther("Higashi-shinjyuku 1-2-3");
        person.setName("Foo");
        person.setAddress(address);
        // insert
        mOpenHelper.registerPerson(person);
        assertEquals(1, mOpenHelper.findPerson().size());

        Person person2 = new Person();
        Address address2 = new Address();
        address.setZipcode("111-1111");
        address.setPrefecture("Kyoto");
        address.setCity("Kyoto");
        address.setOther("boo 1-2-3");
        person2.setName("Foo"); // duplicate
        person2.setAddress(address2);
        // insert
        // JUnit 3の書き方になってしまう
        try {
            mOpenHelper.registerPerson(person2);
            fail("SQLException is expected");
        } catch (SQLException e) {
        }

        assertEquals(1, mOpenHelper.findPerson().size());
    }

    public void testRegister() throws Exception {
        assertEquals(0, mOpenHelper.findPerson().size());

        // insert
        createTestData(200);

        // select
        List<Person> sut = mOpenHelper.findPerson();
        assertEquals(200, sut.size());
    }

    public void testFind() throws Exception {
        assertEquals(0, mOpenHelper.findPerson().size());

        Person person = new Person();
        Address address = new Address();
        address.setZipcode("123-4567");
        address.setPrefecture("Tokyo");
        address.setCity("Shinjyuku-ku");
        address.setOther("Higashi-shinjyuku 1-2-3");
        person.setName("Foo");
        person.setAddress(address);
        // insert
        mOpenHelper.registerPerson(person);
        // select
        List<Person> sut = mOpenHelper.findPerson();

        assertEquals(1, sut.size());

        assertThat(sut.get(0).getName(), is("Foo"));
        assertThat(sut.get(0).getAddress().getZipcode(), is("123-4567"));
        assertThat(sut.get(0).getAddress().getPrefecture(), is("Tokyo"));
        assertThat(sut.get(0).getAddress().getCity(), is("Shinjyuku-ku"));
        assertThat(sut.get(0).getAddress().getOther(), is("Higashi-shinjyuku 1-2-3"));
    }

    public void testUpdate() throws Exception {
        assertEquals(0, mOpenHelper.findPerson().size());

        Person person = new Person();
        Address address = new Address();
        address.setZipcode("123-4567");
        address.setPrefecture("Tokyo");
        address.setCity("Shinjyuku-ku");
        address.setOther("Higashi-shinjyuku 1-2-3");
        person.setName("Foo");
        person.setAddress(address);
        // insert
        mOpenHelper.registerPerson(person);
        // select
        List<Person> sut = mOpenHelper.findPerson();

        assertEquals(1, sut.size());

        Person person2 = mOpenHelper.findPerson(person.getName());
        Address address2 = person2.getAddress();
        address2.setZipcode("123-0000");
        address2.setPrefecture("Osaka");
        address2.setCity("Osaka");
        address2.setOther("hoge 1-2-3");
        person2.setName("Foo2");
        person2.setAddress(address2);
        // update
        mOpenHelper.updatePerson(person2);
        // select
        assertEquals(1, mOpenHelper.findPerson().size());
        Person sut2 = mOpenHelper.findPerson("Foo2");
        assertThat(sut2.getAddress().getZipcode(), is("123-0000"));
        assertThat(sut2.getAddress().getPrefecture(), is("Osaka"));
        assertThat(sut2.getAddress().getCity(), is("Osaka"));
        assertThat(sut2.getAddress().getOther(), is("hoge 1-2-3"));
        assertThat(sut2.getName(), is("Foo2"));
    }

    public void testDelete() throws SQLException {
        assertEquals(0, mOpenHelper.findPerson().size());

        Person person = new Person();
        Address address = new Address();
        address.setZipcode("123-4567");
        address.setPrefecture("Tokyo");
        address.setCity("Shinjyuku-ku");
        address.setOther("Higashi-shinjyuku 1-2-3");
        person.setName("Foo");
        person.setAddress(address);
        // insert
        mOpenHelper.registerPerson(person);
        // delete
        mOpenHelper.deletePerson(person.getName());
        assertEquals(0, mOpenHelper.findPerson().size());
    }
}
