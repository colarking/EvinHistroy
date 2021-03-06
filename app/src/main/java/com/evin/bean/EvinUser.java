package com.evin.bean;

import java.util.List;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "EVIN_USER".
 */
public class EvinUser {

    private Long id;
    /**
     * 人物名称
     */
    private String name;
    /**
     * 国籍
     */
    private String country;
    /**
     * 民族
     */
    private String nation;
    /**
     * 宗教
     */
    private String religion;
    /**
     * 教育背景
     */
    private String education;
    /**
     * 籍贯
     */
    private String birthPlace;
    /**
     * 个人简介
     */
    private String desc;
    /**
     * 个人简介超链接跳转  json格式 EvinLink
     */
    private String descLinkStr;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EvinUserDao myDao;

    private EvinPosition birthPosition;
    private Long birthPosition__resolvedKey;

    private EvinTime timeBirth;
    private Long timeBirth__resolvedKey;

    private EvinTime timeDeath;
    private Long timeDeath__resolvedKey;

    private EvinUser father;
    private Long father__resolvedKey;

    private EvinUser mother;
    private Long mother__resolvedKey;

    private List<EvinImage> userImages;
    private List<EvinUser> relativeTops;
    private List<EvinUser> relativeBottoms;
    private List<EvinUser> relativeMates;
    private List<EvinUser> relativeFriends;
    private List<EventAndUser> events;

    public EvinUser() {
    }

    public EvinUser(Long id) {
        this.id = id;
    }

    public EvinUser(Long id, String name, String country, String nation, String religion, String education, String birthPlace, String desc, String descLinkStr) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.nation = nation;
        this.religion = religion;
        this.education = education;
        this.birthPlace = birthPlace;
        this.desc = desc;
        this.descLinkStr = descLinkStr;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEvinUserDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescLinkStr() {
        return descLinkStr;
    }

    public void setDescLinkStr(String descLinkStr) {
        this.descLinkStr = descLinkStr;
    }

    /** To-one relationship, resolved on first access. */
    public EvinPosition getBirthPosition() {
        Long __key = this.id;
        if (birthPosition__resolvedKey == null || !birthPosition__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinPositionDao targetDao = daoSession.getEvinPositionDao();
            EvinPosition birthPositionNew = targetDao.load(__key);
            synchronized (this) {
                birthPosition = birthPositionNew;
            	birthPosition__resolvedKey = __key;
            }
        }
        return birthPosition;
    }

    public void setBirthPosition(EvinPosition birthPosition) {
        synchronized (this) {
            this.birthPosition = birthPosition;
            id = birthPosition == null ? null : birthPosition.getId();
            birthPosition__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public EvinTime getTimeBirth() {
        Long __key = this.id;
        if (timeBirth__resolvedKey == null || !timeBirth__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinTimeDao targetDao = daoSession.getEvinTimeDao();
            EvinTime timeBirthNew = targetDao.load(__key);
            synchronized (this) {
                timeBirth = timeBirthNew;
            	timeBirth__resolvedKey = __key;
            }
        }
        return timeBirth;
    }

    public void setTimeBirth(EvinTime timeBirth) {
        synchronized (this) {
            this.timeBirth = timeBirth;
            id = timeBirth == null ? null : timeBirth.getId();
            timeBirth__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public EvinTime getTimeDeath() {
        Long __key = this.id;
        if (timeDeath__resolvedKey == null || !timeDeath__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinTimeDao targetDao = daoSession.getEvinTimeDao();
            EvinTime timeDeathNew = targetDao.load(__key);
            synchronized (this) {
                timeDeath = timeDeathNew;
            	timeDeath__resolvedKey = __key;
            }
        }
        return timeDeath;
    }

    public void setTimeDeath(EvinTime timeDeath) {
        synchronized (this) {
            this.timeDeath = timeDeath;
            id = timeDeath == null ? null : timeDeath.getId();
            timeDeath__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public EvinUser getFather() {
        Long __key = this.id;
        if (father__resolvedKey == null || !father__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            EvinUser fatherNew = targetDao.load(__key);
            synchronized (this) {
                father = fatherNew;
            	father__resolvedKey = __key;
            }
        }
        return father;
    }

    public void setFather(EvinUser father) {
        synchronized (this) {
            this.father = father;
            id = father == null ? null : father.getId();
            father__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public EvinUser getMother() {
        Long __key = this.id;
        if (mother__resolvedKey == null || !mother__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            EvinUser motherNew = targetDao.load(__key);
            synchronized (this) {
                mother = motherNew;
            	mother__resolvedKey = __key;
            }
        }
        return mother;
    }

    public void setMother(EvinUser mother) {
        synchronized (this) {
            this.mother = mother;
            id = mother == null ? null : mother.getId();
            mother__resolvedKey = id;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EvinImage> getUserImages() {
        if (userImages == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinImageDao targetDao = daoSession.getEvinImageDao();
            List<EvinImage> userImagesNew = targetDao._queryEvinUser_UserImages(id);
            synchronized (this) {
                if(userImages == null) {
                    userImages = userImagesNew;
                }
            }
        }
        return userImages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetUserImages() {
        userImages = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EvinUser> getRelativeTops() {
        if (relativeTops == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            List<EvinUser> relativeTopsNew = targetDao._queryEvinUser_RelativeTops(id);
            synchronized (this) {
                if(relativeTops == null) {
                    relativeTops = relativeTopsNew;
                }
            }
        }
        return relativeTops;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRelativeTops() {
        relativeTops = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EvinUser> getRelativeBottoms() {
        if (relativeBottoms == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            List<EvinUser> relativeBottomsNew = targetDao._queryEvinUser_RelativeBottoms(id);
            synchronized (this) {
                if(relativeBottoms == null) {
                    relativeBottoms = relativeBottomsNew;
                }
            }
        }
        return relativeBottoms;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRelativeBottoms() {
        relativeBottoms = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EvinUser> getRelativeMates() {
        if (relativeMates == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            List<EvinUser> relativeMatesNew = targetDao._queryEvinUser_RelativeMates(id);
            synchronized (this) {
                if(relativeMates == null) {
                    relativeMates = relativeMatesNew;
                }
            }
        }
        return relativeMates;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRelativeMates() {
        relativeMates = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EvinUser> getRelativeFriends() {
        if (relativeFriends == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EvinUserDao targetDao = daoSession.getEvinUserDao();
            List<EvinUser> relativeFriendsNew = targetDao._queryEvinUser_RelativeFriends(id);
            synchronized (this) {
                if(relativeFriends == null) {
                    relativeFriends = relativeFriendsNew;
                }
            }
        }
        return relativeFriends;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRelativeFriends() {
        relativeFriends = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity.
     */
    public List<EventAndUser> getEvents() {
        if (events == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventAndUserDao targetDao = daoSession.getEventAndUserDao();
            List<EventAndUser> eventsNew = targetDao._queryEvinUser_Events(id);
            synchronized (this) {
                if (events == null) {
                    events = eventsNew;
                }
            }
        }
        return events;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    public synchronized void resetEvents() {
        events = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
