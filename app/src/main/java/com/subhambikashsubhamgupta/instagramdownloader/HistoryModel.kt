package com.subhambikashsubhamgupta.instagramdownloader

class HistoryModel {

    private var title: String? = null
    private var link: String? = null
    private var img: String? = null

    fun HistoryModel() {}

    fun HistoryModel(title: String?, link: String?, img: String?) {
        this.title = title
        this.link = link
        this.img = img
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getLink(): String? {
        return link
    }

    fun setLink(link: String?) {
        this.link = link
    }

    fun getImg(): String? {
        return img
    }

    fun setImg(img: String?) {
        this.img = img
    }
}