package com.ptithcm.quizapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;


import com.ptithcm.quizapp.model.Answer;
import com.ptithcm.quizapp.model.Level;
import com.ptithcm.quizapp.model.NumbAccountsByDay;
import com.ptithcm.quizapp.model.Question;
import com.ptithcm.quizapp.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "QuizAppDB.db";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public ArrayList<Question> getListQuestion(String currentSort, String levelID) {
        Question qs = null;
        ArrayList<Question> qsList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Questions WHERE questionLevel = " + levelID + " ORDER BY " + currentSort, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            qs = new Question(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null);
            qsList.add(qs);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return qsList;
    }

    public boolean deleteQuestions(ArrayList<String> ids) {
        openDatabase();
        try {
            mDatabase.execSQL("DELETE FROM Answers WHERE questionID IN (" + TextUtils.join(", ", ids) + ")");
            mDatabase.execSQL("DELETE FROM Questions WHERE questionID IN (" + TextUtils.join(", ", ids) + ")");
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
            return false;
        }

        return true;
    }

    public boolean addQuestion_Answer(Question qs, ArrayList<Answer> answers) {
        boolean check = true;
        openDatabase();

        byte[] imageInPytes = null;
        try {
            Bitmap bitmap = qs.getQuestionImage();
            imageInPytes = getBytes(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("questionContent", qs.getQuestionContent().toString());
            contentValues.put("questionLevel", Integer.valueOf(qs.getQuestionLevel()));
            contentValues.put("exactAnswer", qs.getExactAnswer().toString());
            contentValues.put("questionImage", imageInPytes);

            mDatabase.insert("Questions", null, contentValues);
            String sql;
//            sql = "INSERT INTO Questions VALUES(null," + "'" + qs.getQuestionContent().toString() +
//                    "'," + qs.getQuestionType().toString() +
//                    "," + qs.getQuestionLevel().toString() +
//                    ",'" +qs.getExactAnswer().toString() + "',null)";
//            mDatabase.execSQL(sql);
            String qsID = getMaxIDQuestion();
            for (Answer ans : answers) {
                sql = "INSERT INTO Answers VALUES(null,'" + ans.getAnswerContent() + "'," + qsID + ")";
                mDatabase.execSQL(sql);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            check = false;
        }
        closeDatabase();
        return check;
    }

    public String getMaxIDQuestion() {
        String sql = "SELECT MAX(QuestionID) FROM Questions";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        String qsID = cursor.getString(0).toString();
        cursor.close();
        return qsID;
    }

    public boolean updateQuestion_Answer(Question qs, ArrayList<Answer> ans_new) {
        boolean check = true;
        byte[] imageInPytes = null;
        try {
            Bitmap bitmap = qs.getQuestionImage();
            imageInPytes = getBytes(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ArrayList<Answer> ans_old = new ArrayList<>(getAnswersByQuestionID(qs.getQuestionID()));
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("questionContent", qs.getQuestionContent().toString());
            contentValues.put("questionLevel", Integer.valueOf(qs.getQuestionLevel()));
            contentValues.put("exactAnswer", qs.getExactAnswer().toString());
            contentValues.put("questionImage", imageInPytes);
            mDatabase.update("Questions", contentValues, "questionID = " + qs.getQuestionID().toString(), null);
            String id;
            if (ans_old.size() <= ans_new.size()) {
                for (int i = 0; i < ans_old.size(); i++) {
                    contentValues = new ContentValues();
                    id = ans_old.get(i).getAnswerID();
                    contentValues.put("answerID", id);
                    contentValues.put("answerContent", ans_new.get(i).getAnswerContent());
                    contentValues.put("questionID", qs.getQuestionID());
                    mDatabase.update("Answers", contentValues, "answerID = " + id, null);
                }
                for (int i = ans_old.size(); i < ans_new.size(); i++) {
                    contentValues = new ContentValues();
                    id = ans_new.get(i).getAnswerID();
                    contentValues.put("answerID", id);
                    contentValues.put("answerContent", ans_new.get(i).getAnswerContent());
                    contentValues.put("questionID", qs.getQuestionID());
                    mDatabase.insert("Answers", null, contentValues);
                }
            } else {
                for (int i = 0; i < ans_new.size(); i++) {
                    contentValues = new ContentValues();
                    id = ans_old.get(i).getAnswerID();
                    contentValues.put("answerID", id);
                    contentValues.put("answerContent", ans_new.get(i).getAnswerContent());
                    contentValues.put("questionID", qs.getQuestionID());
                    mDatabase.update("Answers", contentValues, "answerID = " + id, null);
                }
                for (int i = ans_new.size(); i < ans_old.size(); i++) {
                    mDatabase.delete("Answers", "answerID = " + ans_old.get(i).getAnswerID(), null);
                }
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            check = false;
        }
        closeDatabase();
        return check;
    }


    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Question getQuestionByID(String questionID) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Questions WHERE questionID = " + questionID, null);
        Question qs = new Question();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] byteArray = cursor.getBlob(4);
            if (byteArray != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                qs = new Question(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), bm);

            } else {
                qs = new Question(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null);

            }

            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return qs;
    }

    public ArrayList<Answer> getAnswersByQuestionID(String questionID) {
        openDatabase();
        ArrayList<Answer> list = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Answers WHERE questionID = " + questionID, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Answer(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return list;
    }

    public ArrayList<NumbAccountsByDay> getCountAccByDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        openDatabase();
        ArrayList<NumbAccountsByDay> list = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT SUBSTR(creAt, 9) AS day, COUNT(*) AS count FROM Accounts  Where creAt >= DATE('now', 'start of month') GROUP BY CAST(day AS INTEGER)", null);
        cursor.moveToFirst();
        int sum_count = 0;
        int day_cur = 0;
        while (!cursor.isAfterLast()) {
            day_cur = cursor.getInt(0);
            sum_count = sum_count + cursor.getInt(1);
            list.add(new NumbAccountsByDay(day_cur, sum_count));
            cursor.moveToNext();
        }
        if(day_cur < day)
            list.add(new NumbAccountsByDay(day, sum_count));
        cursor.close();
        closeDatabase();
        return list;
    }

    public ArrayList<Level> getLevelByType(String type) {
        openDatabase();
        ArrayList<Level> list = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Levels  Where type = " + type, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Level(cursor.getInt(0), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return list;
    }

    public boolean addLevel(String type, String title) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("title", title);

        long result = mDatabase.insert("Levels", null, values);
        closeDatabase();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateLevel(String id, String title) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put("title", title);

        long result = mDatabase.update("Levels", values, "id = " + id, null);
        closeDatabase();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteLevel(String id) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Questions  Where questionLevel = " + id, null);
        cursor.moveToFirst();
        try {
            cursor.getString(0);
            closeDatabase();
            return false;
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            String sql = "DELETE FROM Levels where id = " + id;
            mDatabase.execSQL(sql);
            closeDatabase();
            return true;
        }
    }

    public Level getLevelByID(String id) {
        Level level = new Level();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Levels  Where id = " + id, null);
        cursor.moveToFirst();
        level.setId(cursor.getInt(0));
        level.setTitle(cursor.getString(2));
        level.setType(cursor.getInt(1));
        cursor.close();
        closeDatabase();

        return level;
    }

    public void updateContentQuestion(Question qs) {
        openDatabase();
        byte[] imageInPytes = null;
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("questionContent", qs.getQuestionContent().toString());
            contentValues.put("exactAnswer", qs.getExactAnswer().toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (qs.getQuestionImage() != null) {
            try {
                Bitmap bitmap = qs.getQuestionImage();
                imageInPytes = getBytes(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            contentValues.put("questionImage", imageInPytes);
        } else {
            contentValues.put("questionImage", (String) null);
        }
        mDatabase.update("Questions", contentValues, "questionID = " + qs.getQuestionID().toString(), null);
        closeDatabase();
    }

    public boolean addQuestion(Question qs) {
        boolean check = true;
        openDatabase();
        byte[] imageInPytes = null;
        try {
            Bitmap bitmap = qs.getQuestionImage();
            imageInPytes = getBytes(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("questionContent", qs.getQuestionContent());
            contentValues.put("questionLevel", Integer.valueOf(qs.getQuestionLevel()));
            contentValues.put("exactAnswer", qs.getExactAnswer());
            contentValues.put("questionImage", imageInPytes);
            mDatabase.insert("Questions", null, contentValues);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            check = false;
        }
        closeDatabase();
        return check;
    }

    public User getUserByID(String receivedID) {
        User user = new User();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Users  Where userId = " + receivedID, null);
        cursor.moveToFirst();
        user.setUserName(cursor.getString(1));
        user.setPhoneNumber(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        cursor.close();
        closeDatabase();

        return user;
    }
    public boolean updateUser(User user, String id) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", user.getUserName().toString());
        contentValues.put("phoneNumber", user.getPhoneNumber().toString());
        try {
            mDatabase.update("Users", contentValues, "userId = " + id, null);
            closeDatabase();
            return true;
        }
        catch (SQLiteConstraintException e)  {
            e.printStackTrace();
            closeDatabase();
            return false;
        }
    }
}
