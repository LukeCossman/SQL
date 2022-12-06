package cs333fa22.hfad.demosqlite;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "employees.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
        db.execSQL(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DBContract.EmployeeEntry.DROP_EMP_TABLE_CMD);
        onCreate(db);
    }

    public void saveEmployee(String name, String desig, long dobInMS)
    {
        String insertString = String.format("INSERT INTO %s (%s, %s, %s) " +
                                            "VALUES('%s', %d, '%s')",
                                            DBContract.EmployeeEntry.TABLE_NAME,
                                            DBContract.EmployeeEntry.COLUMN_NAME,
                                            DBContract.EmployeeEntry.COLUMN_DOB,
                                            DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                                            name, dobInMS, desig);
        System.out.println("SAVING " + insertString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertString);

        db.close();
    }

    public ArrayList<Employee> fetchAllEmployees()
    {
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();

        String selectString = String.format("SELECT * FROM %s", DBContract.EmployeeEntry.TABLE_NAME);
        System.out.println("FETCHING ALL " + selectString);

        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor starts at -1
        Cursor cursor = db.rawQuery(selectString, null);

        //Get the positions of columns
        int posID = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_ID);
        int posName = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_NAME);
        int posDesig = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DESIGNATION);
        int posDOB = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DOB);

        //Use positions to request values in columns
        while (cursor.moveToNext())
        {
            //Get info from current record (record = row)
            long id = cursor.getLong(posID);
            String name = cursor.getString(posName);
            String desig = cursor.getString(posDesig);
            long dob = cursor.getLong(posDOB);

            allEmployees.add(new Employee(id, name, dob, desig));
        }

        cursor.close();
        db.close();
        return allEmployees;
    }

    public void updateEmployee(Employee emp)
    {
        String updateString = String.format("UPDATE %s \n" +
                                            "SET %s = '%s', \n" +
                                            "    %s = '%s', \n" +
                                            "    %s = %d \n" +
                                            "WHERE %s = %d;",
                                            DBContract.EmployeeEntry.TABLE_NAME,
                                            DBContract.EmployeeEntry.COLUMN_NAME,
                                            emp.getName(),
                                            DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                                            emp.getDesignation(),
                                            DBContract.EmployeeEntry.COLUMN_DOB,
                                            emp.getDob(),
                                            DBContract.EmployeeEntry.COLUMN_ID,
                                            emp.getId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateString);

        System.out.println(updateString);

    }


}
