package com.rl.geye.db.bean;

import com.rl.geye.util.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity
public class CloudUser {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    @Property(nameInDb = "username")
    private String username; // 用户名

    @Property(nameInDb = "password")
    private String password; // 密码

    @Property(nameInDb = "mailaddr")
    private String mailaddr; // 邮箱地址

    @Property(nameInDb = "phonenum")
    private String phonenum; // 手机号码

    @ToMany(referencedJoinProperty = "ownerid")
    private List<EdwinDevice> devices; //  拥有的设备

    @Property(nameInDb = "actived")
    private boolean actived; // 当前用户是否激活

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1997492396)
    private transient CloudUserDao myDao;

    @Generated(hash = 222067387)
    public CloudUser(Long id, String username, String password, String mailaddr,
            String phonenum, boolean actived) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mailaddr = mailaddr;
        this.phonenum = phonenum;
        this.actived = actived;
    }

    @Generated(hash = 2078768902)
    public CloudUser() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloudUser cu = (CloudUser)o;
        if(username != null) return username.equals(username);
        if(mailaddr != null) return mailaddr.equals(mailaddr);
        if(phonenum != null) return phonenum.equals(phonenum);
        return false;
    }

    @Override
    public String toString() {
        return "CloudUser:{" +
                "username:[" + username + "]," +
                "password:[" + password + "]," +
                "mailaddr:[" + mailaddr + "]," +
                "phonenum:[" + phonenum + "]," +
                "devices:[" + devices.toString() + "]" +
                "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailaddr() {
        return this.mailaddr;
    }

    public void setMailaddr(String mailaddr) {
        this.mailaddr = mailaddr;
    }

    public String getPhonenum() {
        return this.phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public boolean getActived() {
        return this.actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 118422018)
    public List<EdwinDevice> getDevices() {
        if (devices == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EdwinDeviceDao targetDao = daoSession.getEdwinDeviceDao();
            List<EdwinDevice> devicesNew = targetDao._queryCloudUser_Devices(id);
            synchronized (this) {
                if (devices == null) {
                    devices = devicesNew;
                }
            }
        }
        return devices;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1428662284)
    public synchronized void resetDevices() {
        devices = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 547345166)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCloudUserDao() : null;
    }
}
