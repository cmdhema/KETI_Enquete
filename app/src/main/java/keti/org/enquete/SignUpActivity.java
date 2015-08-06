package keti.org.enquete;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Store;

@EActivity(R.layout.activity_signup)
public class SignUpActivity extends BaseActivity {

    @ViewById(R.id.signup_addr_et)
    EditText addrEt;
    @ViewById(R.id.signup_id_et)
    EditText idEt;
    @ViewById(R.id.signup_name_et)
    EditText nameEt;
    @ViewById(R.id.signup_password_et)
    EditText passwordEt;
    @ViewById(R.id.signup_num_et)
    EditText numberEt;

    @OrmLiteDao(helper = DBHelper.class, model = Store.class)
    Dao<Store, Long> storeDao;

    List<Store> storeList;
    Store store;

    @AfterViews
    void init() {
        try {
            storeList = storeDao.queryForAll();

            if ( storeList.size() > 0 ) {
                store = storeList.get(0);
                nameEt.setText(store.name);
                idEt.setText(store.storeId);
                addrEt.setText(store.addr);
                numberEt.setText(store.num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.signup_ok_btn)
    void signUp() {

        if ( store == null ) {
            Store store = new Store();
            store.storeId = idEt.getText().toString();
            store.password = passwordEt.getText().toString();
            store.name = nameEt.getText().toString();
            store.addr = addrEt.getText().toString();
            store.num = addrEt.getText().toString();
            try {
                int flag = storeDao.create(store);
                if ( flag == 1)
                    Toast.makeText(getApplicationContext(), "등록됐습니다.", Toast.LENGTH_SHORT).show();
                storeDao.refresh(store);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            try {
                store.storeId = idEt.getText().toString();
                store.password = passwordEt.getText().toString();
                store.name = nameEt.getText().toString();
                store.addr = addrEt.getText().toString();
                store.num = numberEt.getText().toString();

                if ( storeDao.update(store) == 1)
                    Toast.makeText(getApplicationContext(), "수정됐습니다..", Toast.LENGTH_SHORT).show();
                storeDao.refresh(store);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        finish();
    }

    @Click(R.id.signup_cancel_btn)
    void cancel() {
        finish();
    }
}
