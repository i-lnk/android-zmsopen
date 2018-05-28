package com.rl.geye.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.rl.geye.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicky on 2016/9/12.
 * 图片组、视频组(按日期分组)
 */

public class RecordGroup extends AbstractExpandableItem<CloudRecord> implements MultiItemEntity, Parcelable {

    public static final Creator<RecordGroup> CREATOR = new Creator<RecordGroup>() {
        @Override
        public RecordGroup createFromParcel(Parcel source) {
            return new RecordGroup(source);
        }

        @Override
        public RecordGroup[] newArray(int size) {
            return new RecordGroup[size];
        }
    };
//    private String desc;
    private String date;

    public RecordGroup() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (obj.getClass() != RecordGroup.class) return false;

        RecordGroup recordGroup = (RecordGroup)obj;
        return TextUtils.equals(date,recordGroup.date);
    }

    protected RecordGroup(Parcel in) {
        this.date = in.readString();
        this.mExpandable = in.readByte() != 0;
        this.mSubItems = new ArrayList<CloudRecord>();
        in.readList(this.mSubItems, CloudRecord.class.getClassLoader());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return Constants.LevelType.GROUP;
    }

    public List<CloudRecord> getSelectedDatas() {
        List<CloudRecord> list = new ArrayList<>();
        if (mSubItems != null) {
            for (CloudRecord child : mSubItems) {
                if (child.isChecked())
                    list.add(child);
            }
        }
        return list;
    }

    public List<CloudRecord> getUSelectedDatas() {
        List<CloudRecord> list = new ArrayList<>();
        if (mSubItems != null) {
            for (CloudRecord child : mSubItems) {
                if (!child.isChecked())
                    list.add(child);
            }
        }
        return list;
    }

    public int getSelectedCount() {
        if (mSubItems == null)
            return 0;
        int count = 0;
        for (CloudRecord child : mSubItems) {
            if (child.isChecked())
                count++;
        }
        return count;
    }

    public boolean isSelectAll() {
        if (mSubItems == null)
            return false;
        for (CloudRecord child : mSubItems) {
            if (!child.isChecked())
                return false;
        }
        return true;
    }

    public void selectAll() {
        if (mSubItems == null)
            return;
        for (CloudRecord child : mSubItems) {
            child.setChecked(true);
        }
    }

    public void deselectAll() {
        if (mSubItems == null)
            return;
        for (CloudRecord child : mSubItems) {
            child.setChecked(false);
        }
    }

    public void toggleSelectAll() {
        if (mSubItems == null)
            return;
        if (isSelectAll())
            deselectAll();
        else
            selectAll();
    }

    public int getChildrenCount() {
        return mSubItems == null ? 0 : mSubItems.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeByte(this.mExpandable ? (byte) 1 : (byte) 0);
        dest.writeList(this.mSubItems);
    }

    @Override
    public String toString() {
        return "RecordGroup{" +
                "date='" + date + '\'' +
                "} " + super.toString();
    }


}
