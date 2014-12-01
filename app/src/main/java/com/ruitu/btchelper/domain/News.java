package com.ruitu.btchelper.domain;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private static final long serialVersionUID = 1L;
    /**
     * 新闻图片路径
     */
    private String news_icon;
    /**
     * 新闻标题
     */
    private String news_title;
    /**
     * 新闻发布时间
     */
    private String news_time;
    /**
     * 发布作者
     */
    private String news_author;
    /**
     * 链接地址
     */
    private String news_info_url;
    /**
     * 新闻内容
     */
    private String news_content;
    /**
     * 新闻类型
     */
    private int news_type;
    /**
     * 图片
     */
    private Bitmap news_icon_drawable;

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public int getNews_type() {
        return news_type;
    }

    public void setNews_type(int news_type) {
        this.news_type = news_type;
    }

    public String getNews_icon() {
        return news_icon;
    }

    public void setNews_icon(String news_icon) {
        this.news_icon = news_icon;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

    public String getNews_author() {
        return news_author;
    }

    public void setNews_author(String news_author) {
        this.news_author = news_author;
    }

    public String getNews_info_url() {
        return news_info_url;
    }

    public void setNews_info_url(String news_info_url) {
        this.news_info_url = news_info_url;
    }

    public Bitmap getNews_icon_drawable() {
        return news_icon_drawable;
    }

    public void setNews_icon_drawable(Bitmap news_icon_drawable) {
        this.news_icon_drawable = news_icon_drawable;
    }

    public News(String news_icon, String news_title, String news_time,
            String news_author, String news_info_url, String news_content,
            int news_type, Bitmap news_icon_drawable) {
        super();
        this.news_icon = news_icon;
        this.news_title = news_title;
        this.news_time = news_time;
        this.news_author = news_author;
        this.news_info_url = news_info_url;
        this.news_content = news_content;
        this.news_type = news_type;
        this.news_icon_drawable = news_icon_drawable;
    }

    public News(String news_icon, String news_title, String news_time,
            String news_author, String news_info_url, String news_content,
            int news_type) {
        super();
        this.news_icon = news_icon;
        this.news_title = news_title;
        this.news_time = news_time;
        this.news_author = news_author;
        this.news_info_url = news_info_url;
        this.news_content = news_content;
        this.news_type = news_type;
    }

    public News() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.news_icon);
        dest.writeString(this.news_title);
        dest.writeString(this.news_time);
        dest.writeString(this.news_author);
        dest.writeString(this.news_info_url);
        dest.writeString(this.news_content);
        dest.writeInt(this.news_type);
    }

    public static final Creator<News> CREATOR = new Creator<News>() {

        @Override
        public News createFromParcel(Parcel source) {
            return new News(source.readString(), source.readString(),
                    source.readString(), source.readString(),
                    source.readString(), source.readString(), source.readInt());
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

}
