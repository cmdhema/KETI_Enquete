package keti.org.enquete;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Question;
import keti.org.enquete.model.Store;
import keti.org.enquete.model.Survey;


@EActivity(R.layout.activity_survey)
public class SurveyActivity extends Activity {

    @OrmLiteDao(helper = DBHelper.class, model = Survey.class)
    Dao<Survey, Long> surveyDao;
    @OrmLiteDao(helper = DBHelper.class, model = Store.class)
    Dao<Store, Long> storeDao;
    @OrmLiteDao(helper = DBHelper.class, model = Question.class)
    Dao<Question, Long> questionDao;

    @ViewById(R.id.question1)
    TextView question1Tv;
    @ViewById(R.id.question2)
    TextView question2Tv;
    @ViewById(R.id.scoreLayout_heart2)
    LinearLayout heartLayoutBottom;
    @ViewById(R.id.scoreLayout_smile)
    LinearLayout smileLayout;
    @ViewById(R.id.survey_commitBtn)
    Button commitBtn;
    @ViewById(R.id.iv_survey_finish)
    ImageView finishIv;

    Store store;

    @ViewsById({R.id.answer1_1, R.id.answer1_2, R.id.answer1_3, R.id.answer1_4, R.id.answer1_5})
    List<ImageView> question1AnswerImageViewList;
    @ViewsById({R.id.answer2_1, R.id.answer2_2, R.id.answer2_3, R.id.answer2_4, R.id.answer2_5})
    List<ImageView> question2AnswerImageViewList;
    @ViewsById({R.id.answer3_1, R.id.answer3_2})
    List<ImageView> smileAnswerImageViewList;

    int question1;
    int question2;

    int question1Score = 0;
    int question2Score = 0;
    int smileScore = 0;

    @AfterViews
    void init() {
        try {

            store = storeDao.queryForAll().get(0);
            question1 = store.question1;
            question2 = store.question2;

            List<Question> questions = questionDao.queryForAll();
            question1Tv.setText("   " + questions.get(question1-1).question);
            question2Tv.setText("   " + questions.get(question2-1).question);

            if ( question2 == 8 ) {
                heartLayoutBottom.setVisibility(View.GONE);
                smileLayout.setVisibility(View.VISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.answer1_1)
    void answer1_1() {
        setQuestion1Answer(0);
    }
    @Click(R.id.answer1_2)
    void answer1_2() {
        setQuestion1Answer(1);
    }
    @Click(R.id.answer1_3)
    void answer1_3() {
        setQuestion1Answer(2);
    }
    @Click(R.id.answer1_4)
    void answer1_4() {
        setQuestion1Answer(3);
    }
    @Click(R.id.answer1_5)
    void answer1_5() {
        setQuestion1Answer(4);
    }
    @Click(R.id.answer2_1)
    void answer2_1() {
        setQuestion2Answer(0);
    }
    @Click(R.id.answer2_2)
    void answer2_2() {
        setQuestion2Answer(1);
    }
    @Click(R.id.answer2_3)
    void answer2_3() {
        setQuestion2Answer(2);
    }
    @Click(R.id.answer2_4)
    void answer2_4() {
        setQuestion2Answer(3);
    }

    @Click(R.id.answer2_5)
    void answer5_5() {
        setQuestion2Answer(4);
    }

    @Click(R.id.answer3_1)
    void answer3_1() {
        setSmileAnswer(0);
    }

    @Click(R.id.answer3_2)
    void answer3_2() {
        setSmileAnswer(1);
    }

    void setQuestion1Answer(int index) {
        question1Score = 0;
        for ( int i = 0; i < index + 1; i++ ) {
            question1AnswerImageViewList.get(i).setImageResource(R.drawable.survey_heart_click);
            question1Score++;
        }

        for ( int i = index + 1; i < 5; i++ ) {
            question1AnswerImageViewList.get(i).setImageResource(R.drawable.survey_heart);
        }

        if ( (question1Score > 0 && question2Score > 0) ||
                (question2Score > 0 && smileScore > 0) || (question1Score > 0 && smileScore > 0) ) {
            commitBtn.setVisibility(View.VISIBLE);
        }
    }

    void setQuestion2Answer(int index) {

        question2Score = 0;
        for ( int i = 0; i < index+1; i++ ) {
            question2AnswerImageViewList.get(i).setImageResource(R.drawable.survey_heart_click);
            question2Score++;
        }

        for ( int i = index + 1; i < 5; i++ ) {
            question2AnswerImageViewList.get(i).setImageResource(R.drawable.survey_heart);
        }

        if ( (question1Score > 0 && question2Score > 0) ||
                (question2Score > 0 && smileScore > 0) || (question1Score > 0 && smileScore > 0) ) {
            commitBtn.setVisibility(View.VISIBLE);
        }
    }

    void setSmileAnswer(int index) {

        if ( index == 0 ) {
            smileAnswerImageViewList.get(1).setImageResource(R.drawable.survey_bad);
            smileAnswerImageViewList.get(0).setImageResource(R.drawable.survey_good_click);
            smileScore = 1;
        } else if ( index == 1 ) {
            smileAnswerImageViewList.get(0).setImageResource(R.drawable.survey_good);
            smileAnswerImageViewList.get(1).setImageResource(R.drawable.survey_bad_click);
            smileScore = 2;
        }

        if ( (question1Score > 0 && question2Score > 0) ||
                (question2Score > 0 && smileScore > 0) || (question1Score > 0 && smileScore > 0) ) {
            commitBtn.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.iv_home)
    void goHome() {
        startActivity(new Intent(this, MainActivity_.class));
    }

    @Click(R.id.survey_commitBtn)
    void commitSurvey() {
        try {
            long time = System.currentTimeMillis();
            Survey survey = new Survey();
            survey.storeId = store.storeId;
            survey.question = question1;
            survey.score = question1Score;
            survey.date = millToDate(time);
            surveyDao.create(survey);

            survey.question = question2;
            if ( question2 == 8 )
                survey.score = smileScore;
            else
                survey.score = question2Score;
            surveyDao.create(survey);

            showFinishImage();

            initScoreViews();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initScoreViews() {
        smileAnswerImageViewList.get(0).setImageResource(R.drawable.survey_good);
        smileAnswerImageViewList.get(1).setImageResource(R.drawable.survey_bad);
        for ( ImageView iv : question1AnswerImageViewList )
            iv.setImageResource(R.drawable.survey_heart);
        for ( ImageView iv : question2AnswerImageViewList )
            iv.setImageResource(R.drawable.survey_heart);
    }

    private void showFinishImage() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishIv.setVisibility(View.GONE);
                        commitBtn.setVisibility(View.GONE);
                    }
                });
            }
        };

        finishIv.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    public String millToDate(long mills) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String date = (String) formatter.format(new Timestamp(mills));

        return date;
    }

}
