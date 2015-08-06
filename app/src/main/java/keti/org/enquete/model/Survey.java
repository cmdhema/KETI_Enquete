package keti.org.enquete.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by kjw on 2015-04-03.
 */
@DatabaseTable(tableName = "surveys")
public class Survey {

    public static final String SURVEY_FIELD_STOREID = "storeId";
    public static final String SURVEY_FIELD_QUESTION = "question";
    public static final String SURVEY_FIELD_SCORE = "score";
    public static final String SURVEY_FIELD_DATE = "date";

    @DatabaseField(generatedId =  true)
    public long id;
    @DatabaseField
    public String storeId;
    @DatabaseField
    public int question;
    @DatabaseField
    public int score;
    @DatabaseField
    public String date;

    public ArrayList<Integer> scoreList;

    public Survey() {

    }

    public Survey(int question, ArrayList<Integer> list) {
        this.question = question;
        scoreList = list;
    }
}
