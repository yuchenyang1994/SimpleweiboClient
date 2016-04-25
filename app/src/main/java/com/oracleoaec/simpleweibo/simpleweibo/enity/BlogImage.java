package com.oracleoaec.simpleweibo.simpleweibo.enity;

/**
 * Created by ycy on 16-4-21.
 */
public class BlogImage
{
    private String blogImageUrl;
    private int image_id;

    public BlogImage() {
    }

    public BlogImage(int image_id, String blogImageUrl) {
        this.image_id = image_id;
        this.blogImageUrl = blogImageUrl;
    }

    public BlogImage(String blogImageUrl) {
        this.blogImageUrl = blogImageUrl;
    }

    public String getBlogImageUrl() {
        return blogImageUrl;
    }

    public void setBlogImageUrl(String blogImageUrl) {
        this.blogImageUrl = blogImageUrl;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "BlogImage{" +
                "blogImageUrl='" + blogImageUrl + '\'' +
                ", image_id=" + image_id +
                '}';
    }
}
