package com.msj.networkcore.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author mengxiangcheng
 * @date 2016/10/14 上午9:50
 * @copyright ©2016 孟祥程 All Rights Reserved
 * @desc MemoryCookieStore
 */
public class MemoryCookieStore implements CookieStore
{
    private final HashMap<String, List<Cookie>> allCookies = new HashMap<>();

    @Override
    public void add(HttpUrl url, List<Cookie> cookies)
    {
        List<Cookie> oldCookies = allCookies.get(url.host());

        if (oldCookies != null)
        {
            Iterator<Cookie> itNew = cookies.iterator();
            Iterator<Cookie> itOld = oldCookies.iterator();
            while (itNew.hasNext())
            {
                String va = itNew.next().name();
                while (va != null && itOld.hasNext())
                {
                    String v = itOld.next().name();
                    if (v != null && va.equals(v))
                    {
                        itOld.remove();
                    }
                }
            }
            oldCookies.addAll(cookies);
        } else
        {
            allCookies.put(url.host(), cookies);
        }


    }

    @Override
    public List<Cookie> get(HttpUrl uri)
    {
        List<Cookie> cookies = allCookies.get(uri.host());
        if (cookies == null)
        {
            cookies = new ArrayList<>();
            allCookies.put(uri.host(), cookies);
        }
        return cookies;

    }

    @Override
    public boolean removeAll()
    {
        allCookies.clear();
        return true;
    }

    @Override
    public List<Cookie> getCookies()
    {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = allCookies.keySet();
        for (String url : httpUrls)
        {
            cookies.addAll(allCookies.get(url));
        }
        return cookies;
    }


    @Override
    public boolean remove(HttpUrl uri, Cookie cookie)
    {
        List<Cookie> cookies = allCookies.get(uri.host());
        if (cookie != null)
        {
            return cookies.remove(cookie);
        }
        return false;
    }


}
