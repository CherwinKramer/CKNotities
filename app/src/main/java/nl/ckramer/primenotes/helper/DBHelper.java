package nl.ckramer.primenotes.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import nl.ckramer.primenotes.entity.Note;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DBHelper";
    public static final String DB_NAME = "ck.notes.db";
    private static final int DB_VERSION = 1;

    Dao<Note, Long> noteDao;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
        try {
            noteDao = DaoManager.createDao(connectionSource, Note.class);
        } catch (SQLException e) {
            Log.e(TAG, "DBHelper: error occurred", e);
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            // Create Table with given table name with columnName
            TableUtils.createTable(cs, Note.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {

    }

    public List<Note> getAll() throws SQLException {
        return noteDao.queryForAll();
    }

    public Note save(Note note) throws SQLException {
        noteDao.createOrUpdate(note);
        return note;
    }

    public Note getById(long id) throws SQLException {
        return noteDao.queryForId(id);
    }

    public boolean delete(Note note) throws SQLException {
        if(note.getId() != null) {
            int i = noteDao.delete(note);
            if (i >= 1) {
                return true;
            }
            return false;
        }
        return false;
    }

//    public <T> List getAll(Class clazz) throws SQLException {
//        Dao<T, ?> dao = getDao(clazz);
//        return dao.queryForAll();
//    }
//
//    public <T> Object getById(Class clazz, Object aId) throws SQLException {
//        Dao<T, Object> dao = (Dao<T, Object>) getDao(clazz);
//        return dao.queryForId(aId);
//    }
//
//    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
//        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
//        return dao.createOrUpdate(obj);
//    }
//
//    public <T> Object createOrUpdate(T obj) throws SQLException {
//        Dao<T, ?> objectDao = DaoManager.createDao(getConnectionSource(), obj.getClass());
//    }
//
//    public <T> int deleteById(Class clazz, Object aId) throws SQLException {
//        Dao<T, Object> dao = getDao(clazz);
//        return dao.deleteById(aId);
//    }
}
