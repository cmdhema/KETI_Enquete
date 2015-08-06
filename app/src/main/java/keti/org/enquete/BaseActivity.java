package keti.org.enquete;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.atermenji.android.iconictextview.IconicTextView;
import com.atermenji.android.iconictextview.icon.EntypoIcon;
import com.j256.ormlite.dao.Dao;
import com.taig.pmc.PopupMenuCompat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Store;

@EActivity(R.layout.activity_base)
public class BaseActivity extends Activity {

    QuestionFragment questionFragment;
    ChartsFragment chartsFragment;

    @Extra
    String fragmentExtra;

    @ViewById(R.id.btn_logout)
    IconicTextView logoutTv;
    @ViewById(R.id.btn_charts)
    TextView chartBtn;
    @ViewById(R.id.btn_question)
    TextView questionBtn;
    @ViewById(R.id.btn_survey)
    TextView surveyBtn;

    @ViewById(R.id.menu_id_tv)
    TextView idTv;

    @OrmLiteDao(helper = DBHelper.class, model = Store.class)
    Dao<Store, Long> storeDao;

    Store store;

    @AfterViews
    void init() {

        if ( questionFragment == null )
            questionFragment = QuestionFragment.newInstance();
        if ( chartsFragment == null )
            chartsFragment = ChartsFragment.newInstance();

        logoutTv.setIcon(EntypoIcon.USER);

        if ( fragmentExtra.equals(AppData.CHART) ) {
            questionBtn.setBackgroundResource(0);
            surveyBtn.setBackgroundResource(0);
            chartBtn.setBackgroundResource(R.drawable.menu_title_click);
            getFragmentManager().beginTransaction().replace(R.id.container, chartsFragment).commit();
        } else if ( fragmentExtra.equals(AppData.QUESTION) ) {
            chartBtn.setBackgroundResource(0);
            surveyBtn.setBackgroundResource(0);
            questionBtn.setBackgroundResource(R.drawable.menu_title_click);
            getFragmentManager().beginTransaction().replace(R.id.container, questionFragment).commit();
        }

        try {
            List<Store> storeList = storeDao.queryForAll();
            store = storeList.get(0);
            idTv.setText(store.storeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.btn_charts)
    void showCharts() {
        questionBtn.setBackgroundResource(0);
        surveyBtn.setBackgroundResource(0);
        chartBtn.setBackgroundResource(R.drawable.menu_title_click);
        getFragmentManager().beginTransaction().replace(R.id.container, chartsFragment).commit();
    }

    @Click(R.id.btn_question)
    void showQuestion() {
        chartBtn.setBackgroundResource(0);
        surveyBtn.setBackgroundResource(0);
        questionBtn.setBackgroundResource(R.drawable.menu_title_click);
        getFragmentManager().beginTransaction().replace(R.id.container, questionFragment).commit();
    }

    @Click(R.id.btn_survey)
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

    @Click(R.id.logout)
    void logout() {
        PopupMenuCompat menu = PopupMenuCompat.newInstance(BaseActivity.this, findViewById(R.id.logout));
        menu.inflate(R.menu.logout);
        menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if ( item.getItemId() == R.id.user_logout) {
                    store.login = 0;
                    try {
                        storeDao.update(store);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(BaseActivity.this, MainActivity_.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if ( item.getItemId() == R.id.user_modify ) {
                    Intent intent = new Intent(BaseActivity.this, SignUpActivity_.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        menu.show();
    }
}
