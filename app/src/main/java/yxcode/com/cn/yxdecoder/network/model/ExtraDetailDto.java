package yxcode.com.cn.yxdecoder.network.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by lihaifeng on 16/12/28.
 * Desc :
 */

public class ExtraDetailDto extends ResponseModel{

    Data data;

    public Data getData() {
        return data;
    }

    public static class Data{
        List<ExtraDetail> codeDetailList;

        public List<ExtraDetail> getCodeDetailList() {
            return codeDetailList;
        }
    }

    public static class ExtraDetail{
        String title;
        List<Pair> extraDetailList;

        public String getTitle() {
            return title;
        }

        public List<Pair> getExtraDetailList() {
            return extraDetailList;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setExtraDetailList(List<Pair> extraDetailList) {
            this.extraDetailList = extraDetailList;
        }
    }

    public static class Pair {
        String key;
        String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isEmpty(){
            return TextUtils.isEmpty(key) && TextUtils.isEmpty(value);
        }
    }
}
