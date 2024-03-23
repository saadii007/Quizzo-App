package com.project.quizzo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.quizzo.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AwesomeQuiz.db";
    public static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " + ")";

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT," +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" + ")";

        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillCategoriesTable();
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("HTML");
        insertCategory(c1);

        Category c2 = new Category("Java");
        insertCategory(c2);

    }

    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionTable() {
        // HTML
        Question q1 = new Question("Which of the following is used to create Web Pages ?",
                "C++", "Java", "HTML", 3, Question.DIFFICULTY_EASY , Category.HTML);
        insertQuestion(q1);

        Question q2 = new Question("HTML stands for ","Hyper Text Makeup Language",
                "Hyper Tech Markup Language", "none of the above", 1, Question.DIFFICULTY_EASY , Category.HTML);
        insertQuestion(q2);

        Question q3 = new Question("PC running with Windows 3.x will have _____ extension for HTML page.",".html",
                "hml", "htm", 1, Question.DIFFICULTY_MEDIUM , Category.HTML);
        insertQuestion(q3);

        Question q4 = new Question("HTML was firstly proposed in year","1990",
                "1995", "1197", 2, Question.DIFFICULTY_MEDIUM , Category.HTML);
        insertQuestion(q4);

        Question q5 = new Question("HTML tags are surrounded by _____ brackets.","Squart",
                "Angle", "Round", 1, Question.DIFFICULTY_HARD , Category.HTML);
        insertQuestion(q5);

        Question q6 = new Question("HTML program can be read and rendered by","Compiler",
                "Browser", "Server", 2, Question.DIFFICULTY_HARD , Category.HTML);
        insertQuestion(q6);

        // JAVA
        Question q10 = new Question("Which of these keywords is used to define packages in Java ?",
                "Package", "package", "pkg", 1, Question.DIFFICULTY_EASY , Category.JAVA);
        insertQuestion(q10);

        Question q20 = new Question("Which of the following is a keyword in Java ?","this",
                "that", "it", 1, Question.DIFFICULTY_EASY , Category.JAVA);
        insertQuestion(q20);

        Question q30 = new Question(" Which of the following is a valid declaration of an object of class Box ?","obj = new Box();",
                "Box obj = new Box;", "Box obj = new Box();", 3, Question.DIFFICULTY_MEDIUM , Category.JAVA);
        insertQuestion(q30);

        Question q40 = new Question("Which of these operators is used to allocate memory for an object ?","give",
                "new", "alloc", 2, Question.DIFFICULTY_MEDIUM , Category.JAVA);
        insertQuestion(q40);

        Question q50 = new Question("What will be the return type of a method that not returns any value ?","double",
                "void", "int", 2, Question.DIFFICULTY_HARD , Category.JAVA);
        insertQuestion(q50);

        Question q60 = new Question("Which of the modifier can not be used for constructors ?","static",
                "private", "public", 3, Question.DIFFICULTY_HARD , Category.JAVA);
        insertQuestion(q60);

    }

    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(CategoriesTable._ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestion() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(cursor.getInt(cursor.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        String selection = QuestionTable.COLUMN_CATEGORY_ID + "= ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ?";

        String[] selectionArgs = new String[] {String.valueOf(categoryID), difficulty};

        Cursor cursor = db.query(QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,null,null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(cursor.getInt(cursor.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }
}