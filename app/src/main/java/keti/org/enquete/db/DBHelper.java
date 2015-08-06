package keti.org.enquete.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;

import keti.org.enquete.model.Question;
import keti.org.enquete.model.Store;
import keti.org.enquete.model.Survey;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "enquete.db";
    private static final int DB_VERSION = 1;

    Dao<Question, Integer> questionDao;

    public DBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i("TAG", "onCreate");
            TableUtils.createTable(connectionSource, Store.class);
            TableUtils.createTable(connectionSource, Survey.class);
            TableUtils.createTable(connectionSource, Question.class);

            Question question0 = new Question(0, "직원들이 친절했나요?");
            Question question1 = new Question(0, "서비스 대기 시간은 만족스러우셨나요?");
            Question question2 = new Question(1, "저희 제품이 만족스러우셨나요?");
            Question question3 = new Question(1, "가격은 적절했나요?");
            Question question4 = new Question(2, "매장이 깨끗하고 쾌적했나요?");
            Question question5 = new Question(2, "매장 분위기는 어땠나요?");
            Question question6 = new Question(3, "저희 매장에 대한 전반적인 만족도를 알려주세요.");
            Question question7 = new Question(4, "저희 매장에 다시 찾아 오실 계획이 있으신가요?");

            questionDao = getDao(Question.class);
            questionDao.create(question0);
            questionDao.create(question1);
            questionDao.create(question2);
            questionDao.create(question3);
            questionDao.create(question4);
            questionDao.create(question5);
            questionDao.create(question6);
            questionDao.create(question7);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

}
