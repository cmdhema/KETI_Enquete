package keti.org.enquete.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by kjw on 2015-04-03.
 */
@DatabaseTable(tableName = "store")
public class Store {

    public static final String STORE_FIELD_NAME = "name";
    public static final String STORE_FIELD_PASSWORD = "password";
    public static final String STORE_FIELD_STOREID = "storeId";
    public static final String STORE_FIELD_ADDR = "addr";
    public static final String STORE_FIELD_PHONENUMBER = "num";
    public static final String STORE_FIELD_QUESTION1 = "question1";
    public static final String STORE_FIELD_QUESTION2 = "question2";
    public static final String STORE_FIELD_LOGIN = "login";

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String password;
    @DatabaseField
    public String storeId;
    @DatabaseField
    public String addr;
    @DatabaseField
    public String num;
    @DatabaseField
    public int question1;
    @DatabaseField
    public int question2;
    @DatabaseField
    public int login;

    public Store() {

    }
}
