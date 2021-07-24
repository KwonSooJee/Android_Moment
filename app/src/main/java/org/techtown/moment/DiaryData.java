package org.techtown.moment;

public class DiaryData {
    int _id;
    String address;
    String createDateStr;
    String contents;
    String picture;

    public DiaryData(int _id,String address, String contents,String picture,String createDateStr){
        this._id =_id;
        this.address=address;
        this.createDateStr=createDateStr;
        this.contents=contents;
        this.picture=picture;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
