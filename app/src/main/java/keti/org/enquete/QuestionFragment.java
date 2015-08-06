package keti.org.enquete;


import android.app.Fragment;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import keti.org.enquete.db.DBHelper;
import keti.org.enquete.model.Question;
import keti.org.enquete.model.Store;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@EFragment(R.layout.fragment_question)
public class QuestionFragment extends Fragment {

    QuestionFragment_
    public static QuestionFragment newInstance() {
        QuestionFragment fragment = new QuestionFragment_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    @OrmLiteDao(helper = DBHelper.class, model = Store.class)
    Dao<Store, Integer> storeDao;

    @ViewsById({R.id.question1_cb, R.id.question2_cb, R.id.question3_cb, R.id.question4_cb, R.id.question5_cb, R.id.question6_cb, R.id.question7_cb, R.id.question8_cb})
    List<CheckBox> checkBoxList;

    int questionCount = 0;
    int question1 = -1;
    int question2 = -1;

    @AfterViews
    void init() {
        for ( CheckBox cb : checkBoxList ) {
            cb.setOnCheckedChangeListener(listener);
        }

        try {
            Store store = storeDao.queryForAll().get(0);
            int question1 = store.question1;
            int question2 = store.question2;
            if ( question1 != 0 && question2 != 0 ) {
                checkBoxList.get(question1-1).setChecked(true);
                checkBoxList.get(question2-1).setChecked(true);
                questionCount = 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Click(R.id.question_clearBtn)
    void clearCheckBox() {
        for ( CheckBox cb : checkBoxList )
            cb.setChecked(false);

        questionCount = 0;
    }

    @Click(R.id.question_commitBtn)
    void commitQuestion() {
        question1 = -1;
        question2 = -1;
        if ( questionCount != 2 )
            Toast.makeText(getActivity(), "질문은 2개만 선택해주세요!", Toast.LENGTH_SHORT).show();
        else {
            for ( int i = 0; i < checkBoxList.size(); i++ ) {
                if ( checkBoxList.get(i).isChecked() ) {
                    if ( question1 == -1 ) {
                        question1 = i+1;
                        continue;
                    } else
                        question2 = i+1;
                }
            }

            try {
                Store store = storeDao.queryForAll().get(0);
                store.question1 = question1;
                store.question2 = question2;
                storeDao.update(store);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Toast.makeText(getActivity(), "설문조사가 등록됐습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    CheckBox.OnCheckedChangeListener listener = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if ( !isChecked )
                questionCount--;
            else {
                questionCount++;
            }
        }
    };
}
