package yxcode.com.cn.yxdecoder.network.model;

import java.util.List;

/**
 * Created by lihaifeng on 16/12/18.
 * Desc :
 */

public class FetchCodeInfo extends ResponseModel {

    Data data;

    public Data getData() {
        return data;
    }

    public static class Data{
        List<CodeDetail> codeDetailList;

        CodeVo codeVo;

        public List<CodeDetail> getCodeDetailList() {
            return codeDetailList;
        }


        public CodeVo getCodeVo() {
            return codeVo;
        }
    }

    public static class CodeVo{
        long ctime;
        int id;
        String content;
        String innerCodeType;
        String keyInfo;
        int userId;
        long utime;

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setInnerCodeType(String innerCodeType) {
            this.innerCodeType = innerCodeType;
        }

        public void setKeyInfo(String keyInfo) {
            this.keyInfo = keyInfo;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }
    }

    public static class  CodeDetail{
        String title;

        CodeDetailMap codeDetailMap;

        public CodeDetailMap getCodeDetailMap() {
            return codeDetailMap;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class CodeDetailMap{
        String 类型;
        String 生成时间;

        public String get生成时间() {
            return 生成时间;
        }

        public String get类型() {
            return 类型;
        }
    }
}
