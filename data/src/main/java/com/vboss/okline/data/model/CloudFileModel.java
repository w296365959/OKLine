package com.vboss.okline.data.model;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/7 <br/>
 * Summary : 云文件模型
 */
public class CloudFileModel {
    /**
     * 文件Id
     */
    private String fileId;

    /**
     * 文件名
     */
    private String title;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 创建时间
     */
    private String date;

    /**
     * 文件格式
     */
    private String format;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件图标／缩略图
     */
    private String icon;

    /**
     * 文件描述／摘要
     */
    private String desc;

    public CloudFileModel(String fileId, String title, String path, String date, String format, String size, String icon, String desc) {
        this.fileId = fileId;
        this.title = title;
        this.path = path;
        this.date = date;
        this.format = format;
        this.size = size;
        this.icon = icon;
        this.desc = desc;
    }

    public String getFileId() {
        return fileId;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }

    public String getFormat() {
        return format;
    }

    public String getSize() {
        return size;
    }

    public String getIcon() {
        return icon;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "CloudFileModel{" +
                "fileId='" + fileId + '\'' +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", date='" + date + '\'' +
                ", format='" + format + '\'' +
                ", size='" + size + '\'' +
                ", icon='" + icon + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
