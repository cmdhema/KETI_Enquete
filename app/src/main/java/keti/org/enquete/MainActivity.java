package keti.org.enquete;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atermenji.android.iconictextview.IconicTextView;
import com.atermenji.android.iconictextview.icon.EntypoIcon;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.taig.pmc.PopupMenuCompat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Store;

@EActivity(R.layout.activity_main)
public class MainActivity extends OrmLiteBaseActivity<DBHelper> {

    @OrmLiteDao(helper = DBHelper.class, model = Store.class)
    Dao<Store, Long> storeDao;
    @ViewById(R.id.main_logout_tv)
    IconicTextView logoutTv;
    @ViewById(R.id.main_logout_id_tv)
    TextView idTv;
    @ViewById(R.id.main_login_btn)
    ImageView loginBtn;
    @ViewById(R.id.main_logout)
    LinearLayout logoutLayout;

    Store store;

    @AfterViews
    void init() {
        getHelper();

        try {
            List<Store> storeList = storeDao.queryForAll();
            if ( storeList.size() > 0 ) {
                store = storeList.get(0);
                if ( storeList.get(0).login == 1 ) {
                    logoutLayout.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.GONE);
                    logoutTv.setIcon(EntypoIcon.USER);
                    idTv.setText((storeList.get(0).storeId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.iv_chart)
    void showChart() {

        try {
            List<Store> store = storeDao.queryForAll();
            if ( store.size() == 0 ) {
                Toast.makeText(getApplicationContext(), "가입해주세요!", Toast.LENGTH_SHORT).show();
            }  else if ( store.get(0).login == 0 ) {
                Toast.makeText(getApplicationContext(), "로그인해주세요!", Toast.LENGTH_SHORT).show();
            } else if ( store.get(0).question1 == 0 || store.get(0).question2 == 0 ) {
                Toast.makeText(getApplicationContext(), "먼저 설문조사를 등록해주세요!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, BaseActivity_.class);
                intent.putExtra(AppData.FRAGMENT, AppData.CHART);
                startActivity(intent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.main_login_btn)
    void showSignUp() {
        Dialog loginDialog = new LoginDialog(MainActivity.this);
        loginDialog.show();
    }

    @Click(R.id.iv_survey)
    void showSurvey() {

        try {
            List<Store> store = storeDao.queryForAll();
            if ( store.size() == 0 ) {
                Toast.makeText(getApplicationContext(), "가입해주세요!", Toast.LENGTH_SHORT).show();
            } else if ( store.get(0).login == 0 ) {
                Toast.makeText(getApplicationContext(), "로그인해주세요!", Toast.LENGTH_SHORT).show();
            } else if ( store.get(0).question1 == 0 || store.get(0).question2 == 0 ) {
                Toast.makeText(getApplicationContext(), "먼저 설문조사를 등록해주세요!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, SurveyActivity_.class);
                startActivity(intent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Click(R.id.iv_question)
    void showQuestion() {

        try {
            List<Store> store = storeDao.queryForAll();
            if ( store.size() == 0 ) {
                Toast.makeText(getApplicationContext(), "가입해주세요!", Toast.LENGTH_SHORT).show();
            } else if ( store.get(0).login == 0 ) {
                Toast.makeText(getApplicationContext(), "로그인해주세요!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, BaseActivity_.class);
                intent.putExtra(AppData.FRAGMENT, AppData.QUESTION);
                startActivity(intent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class LoginDialog extends Dialog {

        TextView loginTv;
        TextView signUpTv;
        TextView cancelTv;
        TextView findTv;
        EditText idEt;
        EditText passwordEt;

        Context context;

        Store store;

        public LoginDialog(final Context context) {

            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 투명하게..
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.dialog_login);

            loginTv = (TextView) findViewById(R.id.login_ok_btn);
            signUpTv = (TextView) findViewById(R.id.login_signup_tv);
            cancelTv = (TextView) findViewById(R.id.login_cancel_btn);
            findTv = (TextView) findViewById(R.id.login_find_tv);
            idEt = (EditText) findViewById(R.id.login_id_et);
            passwordEt = (EditText) findViewById(R.id.login_password_et);

            Bitmap bkg = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.login_dialog_edittext_background)).getBitmap();
            BitmapDrawable bkgbt = new BitmapDrawable(context.getResources(),bkg);
            idEt.setBackground((Drawable) bkgbt);
            passwordEt.setBackground((Drawable)bkgbt);

            try {
                List<Store> storeList = storeDao.queryForAll();
                if ( storeList.size() > 0 )
                    store = storeList.get(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loginTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = idEt.getText().toString();
                    String password = passwordEt.getText().toString();

                    if ( id.length() > 0 && password.length() > 0 ) {
                        if ( id.equals(store.storeId) && password.equals(store.password) ) {
                            store.login = 1;
                            try {
                                storeDao.update(store);
                                loginBtn.setVisibility(View.GONE);
                                logoutLayout.setVisibility(View.VISIBLE);
                                logoutTv.setIcon(EntypoIcon.USER);
                                idTv.setText(id);

                                dismiss();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context.getApplicationContext(), "아이디와 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            signUpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    context.startActivity(new Intent(context, SignUpActivity_.class));
                }
            });
        }
    }

    @Click(R.id.main_logout)
    void logout() {
        PopupMenuCompat menu = PopupMenuCompat.newInstance(MainActivity.this, findViewById(R.id.main_logout));
        menu.inflate(R.menu.logout);
        menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if ( item.getItemId() == R.id.user_logout) {
                    store.login = 0;
                    try {
                        storeDao.update(store);
                        logoutLayout.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if ( item.getItemId() == R.id.user_modify ) {
                    Intent intent = new Intent(MainActivity.this, SignUpActivity_.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        menu.show();
    }
}
