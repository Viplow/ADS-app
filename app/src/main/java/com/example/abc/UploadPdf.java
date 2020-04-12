package com.example.abc;

public class UploadPdf {
    public String head;
    public String description;
    public String postedby;
    public String url;
    public String date;
    public String time;

    public UploadPdf() {
    }



    public UploadPdf(String head, String description, String postedby, String url, String date, String time) {
        this.head = head;
        this.description = description;
        this.postedby = postedby;
        this.url = url;
        this.date=date;
        this.time=time;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head=head;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

