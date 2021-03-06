package com.evin.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.evin.bean.EvinTime;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EVIN_TIME".
*/
public class EvinTimeDao extends AbstractDao<EvinTime, Long> {

    public static final String TABLENAME = "EVIN_TIME";

    /**
     * Properties of entity EvinTime.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property BcTime = new Property(1, Boolean.class, "bcTime", false, "BC_TIME");
        public final static Property TimeStamp = new Property(2, long.class, "timeStamp", false, "TIME_STAMP");
        public final static Property TimeUpload = new Property(3, long.class, "timeUpload", false, "TIME_UPLOAD");
        public final static Property TimeYear = new Property(4, int.class, "timeYear", false, "TIME_YEAR");
        public final static Property TimeString = new Property(5, String.class, "timeString", false, "TIME_STRING");
    };


    public EvinTimeDao(DaoConfig config) {
        super(config);
    }
    
    public EvinTimeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EVIN_TIME\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BC_TIME\" INTEGER," + // 1: bcTime
                "\"TIME_STAMP\" INTEGER NOT NULL ," + // 2: timeStamp
                "\"TIME_UPLOAD\" INTEGER NOT NULL ," + // 3: timeUpload
                "\"TIME_YEAR\" INTEGER NOT NULL ," + // 4: timeYear
                "\"TIME_STRING\" TEXT);"); // 5: timeString
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EVIN_TIME\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EvinTime entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Boolean bcTime = entity.getBcTime();
        if (bcTime != null) {
            stmt.bindLong(2, bcTime ? 1L: 0L);
        }
        stmt.bindLong(3, entity.getTimeStamp());
        stmt.bindLong(4, entity.getTimeUpload());
        stmt.bindLong(5, entity.getTimeYear());
 
        String timeString = entity.getTimeString();
        if (timeString != null) {
            stmt.bindString(6, timeString);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public EvinTime readEntity(Cursor cursor, int offset) {
        EvinTime entity = new EvinTime( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // bcTime
            cursor.getLong(offset + 2), // timeStamp
            cursor.getLong(offset + 3), // timeUpload
            cursor.getInt(offset + 4), // timeYear
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // timeString
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EvinTime entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBcTime(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setTimeStamp(cursor.getLong(offset + 2));
        entity.setTimeUpload(cursor.getLong(offset + 3));
        entity.setTimeYear(cursor.getInt(offset + 4));
        entity.setTimeString(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(EvinTime entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(EvinTime entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
