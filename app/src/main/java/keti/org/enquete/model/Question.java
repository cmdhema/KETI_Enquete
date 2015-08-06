package keti.org.enquete.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by kjw on 2015-04-03.
 */
@DatabaseTable(tableName = "question")
public class Question {

    public static final String QUESTION_FIELD_NAME = "question";
    public static final String QUESTION_FIELD_CATEGORY = "category";

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public int category;
    @DatabaseField
    public String question;

    public Question() {

    }

    public Question(int category, String question) {
        this.question = question;
        this.category = category;
    }
}
