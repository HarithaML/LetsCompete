package com.example.letscompete;

public class Challengesfordisplay {

    String title,duration,description,imageurl;

    public Challengesfordisplay() {

    }


    public Challengesfordisplay(String title,  String duration,String description, String imageurl ) {
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.imageurl = imageurl;

    }

    public String gettitle() {
        return title;
    }

    public void settitle(String course) {
        this.title = course;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getduration() {
        return duration;
    }

    public void setduration(String duration) {
        this.duration = duration;
    }

    public String getimageurl() {
        return imageurl;
    }

    public void setimageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
