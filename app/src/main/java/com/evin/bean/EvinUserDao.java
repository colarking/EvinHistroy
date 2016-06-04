package com.evin.bean;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.evin.bean.EvinUser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EVIN_USER".
*/
public class EvinUserDao extends AbstractDao<EvinUser, Long> {

    public static final String TABLENAME = "EVIN_USER";

    /**
     * Properties of entity EvinUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Country = new Property(2, String.class, "country", false, "COUNTRY");
        public final static Property Nation = new Property(3, String.class, "nation", false, "NATION");
        public final static Property Religion = new Property(4, String.class, "religion", false, "RELIGION");
        public final static Property Education = new Property(5, String.class, "education", false, "EDUCATION");
        public final static Property BirthPlace = new Property(6, String.class, "birthPlace", false, "BIRTH_PLACE");
        public final static Property Desc = new Property(7, String.class, "desc", false, "DESC");
        public final static Property DescLinkStr = new Property(8, String.class, "descLinkStr", false, "DESC_LINK_STR");
    };

    private DaoSession daoSession;

    private Query<EvinUser> evinUser_RelativeTopsQuery;
    private Query<EvinUser> evinUser_RelativeBottomsQuery;
    private Query<EvinUser> evinUser_RelativeMatesQuery;
    private Query<EvinUser> evinUser_RelativeFriendsQuery;

    public EvinUserDao(DaoConfig config) {
        super(config);
    }
    
    public EvinUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EVIN_USER\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"COUNTRY\" TEXT," + // 2: country
                "\"NATION\" TEXT," + // 3: nation
                "\"RELIGION\" TEXT," + // 4: religion
                "\"EDUCATION\" TEXT," + // 5: education
                "\"BIRTH_PLACE\" TEXT," + // 6: birthPlace
                "\"DESC\" TEXT," + // 7: desc
                "\"DESC_LINK_STR\" TEXT);"); // 8: descLinkStr
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EVIN_USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EvinUser entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(3, country);
        }
 
        String nation = entity.getNation();
        if (nation != null) {
            stmt.bindString(4, nation);
        }
 
        String religion = entity.getReligion();
        if (religion != null) {
            stmt.bindString(5, religion);
        }
 
        String education = entity.getEducation();
        if (education != null) {
            stmt.bindString(6, education);
        }
 
        String birthPlace = entity.getBirthPlace();
        if (birthPlace != null) {
            stmt.bindString(7, birthPlace);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(8, desc);
        }
 
        String descLinkStr = entity.getDescLinkStr();
        if (descLinkStr != null) {
            stmt.bindString(9, descLinkStr);
        }
    }

    @Override
    protected void attachEntity(EvinUser entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public EvinUser readEntity(Cursor cursor, int offset) {
        EvinUser entity = new EvinUser( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // country
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nation
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // religion
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // education
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // birthPlace
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // desc
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // descLinkStr
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EvinUser entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCountry(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNation(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setReligion(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEducation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBirthPlace(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDesc(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDescLinkStr(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(EvinUser entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(EvinUser entity) {
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
    
    /** Internal query to resolve the "relativeTops" to-many relationship of EvinUser. */
    public List<EvinUser> _queryEvinUser_RelativeTops(Long id) {
        synchronized (this) {
            if (evinUser_RelativeTopsQuery == null) {
                QueryBuilder<EvinUser> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                evinUser_RelativeTopsQuery = queryBuilder.build();
            }
        }
        Query<EvinUser> query = evinUser_RelativeTopsQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    /** Internal query to resolve the "relativeBottoms" to-many relationship of EvinUser. */
    public List<EvinUser> _queryEvinUser_RelativeBottoms(Long id) {
        synchronized (this) {
            if (evinUser_RelativeBottomsQuery == null) {
                QueryBuilder<EvinUser> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                evinUser_RelativeBottomsQuery = queryBuilder.build();
            }
        }
        Query<EvinUser> query = evinUser_RelativeBottomsQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    /** Internal query to resolve the "relativeMates" to-many relationship of EvinUser. */
    public List<EvinUser> _queryEvinUser_RelativeMates(Long id) {
        synchronized (this) {
            if (evinUser_RelativeMatesQuery == null) {
                QueryBuilder<EvinUser> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                evinUser_RelativeMatesQuery = queryBuilder.build();
            }
        }
        Query<EvinUser> query = evinUser_RelativeMatesQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    /** Internal query to resolve the "relativeFriends" to-many relationship of EvinUser. */
    public List<EvinUser> _queryEvinUser_RelativeFriends(Long id) {
        synchronized (this) {
            if (evinUser_RelativeFriendsQuery == null) {
                QueryBuilder<EvinUser> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                evinUser_RelativeFriendsQuery = queryBuilder.build();
            }
        }
        Query<EvinUser> query = evinUser_RelativeFriendsQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getEvinPositionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getEvinTimeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getEvinTimeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getEvinUserDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T4", daoSession.getEvinUserDao().getAllColumns());
            builder.append(" FROM EVIN_USER T");
            builder.append(" LEFT JOIN EVIN_POSITION T0 ON T.\"ID\"=T0.\"ID\"");
            builder.append(" LEFT JOIN EVIN_TIME T1 ON T.\"ID\"=T1.\"ID\"");
            builder.append(" LEFT JOIN EVIN_TIME T2 ON T.\"ID\"=T2.\"ID\"");
            builder.append(" LEFT JOIN EVIN_USER T3 ON T.\"ID\"=T3.\"ID\"");
            builder.append(" LEFT JOIN EVIN_USER T4 ON T.\"ID\"=T4.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected EvinUser loadCurrentDeep(Cursor cursor, boolean lock) {
        EvinUser entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        EvinPosition birthPosition = loadCurrentOther(daoSession.getEvinPositionDao(), cursor, offset);
        entity.setBirthPosition(birthPosition);
        offset += daoSession.getEvinPositionDao().getAllColumns().length;

        EvinTime timeBirth = loadCurrentOther(daoSession.getEvinTimeDao(), cursor, offset);
        entity.setTimeBirth(timeBirth);
        offset += daoSession.getEvinTimeDao().getAllColumns().length;

        EvinTime timeDeath = loadCurrentOther(daoSession.getEvinTimeDao(), cursor, offset);
        entity.setTimeDeath(timeDeath);
        offset += daoSession.getEvinTimeDao().getAllColumns().length;

        EvinUser father = loadCurrentOther(daoSession.getEvinUserDao(), cursor, offset);
        entity.setFather(father);
        offset += daoSession.getEvinUserDao().getAllColumns().length;

        EvinUser mother = loadCurrentOther(daoSession.getEvinUserDao(), cursor, offset);
        entity.setMother(mother);

        return entity;    
    }

    public EvinUser loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<EvinUser> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<EvinUser> list = new ArrayList<EvinUser>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<EvinUser> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<EvinUser> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}