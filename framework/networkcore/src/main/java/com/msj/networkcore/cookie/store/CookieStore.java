package com.msj.networkcore.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
/**
 * @author mengxiangcheng
 * @date 2016/10/14 上午9:50
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc CookieStore
 */
public interface CookieStore
{

    void add(HttpUrl uri, List<Cookie> cookie);

    List<Cookie> get(HttpUrl uri);

    List<Cookie> getCookies();

    boolean remove(HttpUrl uri, Cookie cookie);

    boolean removeAll();

}
