package org.techtown.moment;

public class ToDoData {
    int _id;
    String createDateStr;
    String contents;
    int max, now;
    String subject;
    String finishDate;

    public ToDoData(int _id, String finishDate, String contents, String subject ,int max ,int now,String createDateStr){
        this._id =_id;
        this.finishDate=finishDate;
        this.createDateStr=createDateStr;
        this.contents=contents;
        this.subject=subject;
        this.max=max;
        this.now=now;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
