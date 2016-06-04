package com.evin.bean;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.evin.bean.EvinTime;
import com.evin.bean.EvinImage;
import com.evin.bean.EvinPosition;
import com.evin.bean.EvinUser;
import com.evin.bean.EvinLink;

import com.evin.bean.EvinTimeDao;
import com.evin.bean.EvinImageDao;
import com.evin.bean.EvinPositionDao;
import com.evin.bean.EvinUserDao;
import com.evin.bean.EvinLinkDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig evinTimeDaoConfig;
    private final DaoConfig evinImageDaoConfig;
    private final DaoConfig evinPositionDaoConfig;
    private final DaoConfig evinUserDaoConfig;
    private final DaoConfig evinLinkDaoConfig;

    private final EvinTimeDao evinTimeDao;
    private final EvinImageDao evinImageDao;
    private final EvinPositionDao evinPositionDao;
    private final EvinUserDao evinUserDao;
    private final EvinLinkDao evinLinkDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        evinTimeDaoConfig = daoConfigMap.get(EvinTimeDao.class).clone();
        evinTimeDaoConfig.initIdentityScope(type);

        evinImageDaoConfig = daoConfigMap.get(EvinImageDao.class).clone();
        evinImageDaoConfig.initIdentityScope(type);

        evinPositionDaoConfig = daoConfigMap.get(EvinPositionDao.class).clone();
        evinPositionDaoConfig.initIdentityScope(type);

        evinUserDaoConfig = daoConfigMap.get(EvinUserDao.class).clone();
        evinUserDaoConfig.initIdentityScope(type);

        evinLinkDaoConfig = daoConfigMap.get(EvinLinkDao.class).clone();
        evinLinkDaoConfig.initIdentityScope(type);

        evinTimeDao = new EvinTimeDao(evinTimeDaoConfig, this);
        evinImageDao = new EvinImageDao(evinImageDaoConfig, this);
        evinPositionDao = new EvinPositionDao(evinPositionDaoConfig, this);
        evinUserDao = new EvinUserDao(evinUserDaoConfig, this);
        evinLinkDao = new EvinLinkDao(evinLinkDaoConfig, this);

        registerDao(EvinTime.class, evinTimeDao);
        registerDao(EvinImage.class, evinImageDao);
        registerDao(EvinPosition.class, evinPositionDao);
        registerDao(EvinUser.class, evinUserDao);
        registerDao(EvinLink.class, evinLinkDao);
    }
    
    public void clear() {
        evinTimeDaoConfig.getIdentityScope().clear();
        evinImageDaoConfig.getIdentityScope().clear();
        evinPositionDaoConfig.getIdentityScope().clear();
        evinUserDaoConfig.getIdentityScope().clear();
        evinLinkDaoConfig.getIdentityScope().clear();
    }

    public EvinTimeDao getEvinTimeDao() {
        return evinTimeDao;
    }

    public EvinImageDao getEvinImageDao() {
        return evinImageDao;
    }

    public EvinPositionDao getEvinPositionDao() {
        return evinPositionDao;
    }

    public EvinUserDao getEvinUserDao() {
        return evinUserDao;
    }

    public EvinLinkDao getEvinLinkDao() {
        return evinLinkDao;
    }

}