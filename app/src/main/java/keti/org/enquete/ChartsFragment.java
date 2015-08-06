package keti.org.enquete;


import android.app.Fragment;
import android.os.Bundle;
import android.widget.GridView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Question;
import keti.org.enquete.model.Survey;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@EFragment(R.layout.fragment_charts)
public class ChartsFragment extends Fragment {

    public static ChartsFragment newInstance() {
        ChartsFragment fragment = new ChartsFragment_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChartsFragment() {
        // Required empty public constructor
    }

    @OrmLiteDao(helper = DBHelper.class, model = Survey.class)
    Dao<Survey, Long> surveyDao;
    @OrmLiteDao(helper = DBHelper.class, model = Question.class)
    Dao<Question, Long> questionDao;
    @ViewById(R.id.chart_grid)
    GridView gridView;

    List<Survey> surveys;

    Map<Integer, ArrayList<Integer>> surveyMap;
    ArrayList<Survey> surveyDataList;

    @AfterViews
    void init() {

        try {
            List<Question> questions = questionDao.queryForAll();
            surveyDataList = new ArrayList<>();
            surveyMap = new HashMap<>();

            try {
                surveys = surveyDao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (Survey survey : surveys) {
                ArrayList<Integer> scoreList = surveyMap.get(survey.question);
                if (scoreList == null) {
                    scoreList = new ArrayList<>();
                }
                scoreList.add(survey.score);
                surveyMap.put(survey.question, scoreList);
            }

            for ( Map.Entry<Integer, ArrayList<Integer>> entry : surveyMap.entrySet() ) {
                Survey survey = new Survey(entry.getKey(), entry.getValue());
                surveyDataList.add(survey);
            }

            ChartsAdapter adapter = new ChartsAdapter(getActivity(), surveyDataList, (ArrayList<Question>)questions);
            gridView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
