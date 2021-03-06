package com.softwareproject.chandy.teller.Bean;

import java.util.List;

/**
 * Created by Chandy on 2016/12/7.
 */

public class HttpResult{


    /**
     * desc : OK
     * status : 1000
     * data : {"wendu":"-10","ganmao":"昼夜温差极大，且风力较强，极易发生感冒，请特别注意增减衣服保暖防寒。","forecast":[{"fengxiang":"北风","fengli":"3-4级","high":"高温 -2℃","type":"晴","low":"低温 -8℃","date":"7日星期三"},{"fengxiang":"无持续风向","fengli":"3-4级","high":"高温 -4℃","type":"大雪","low":"低温 -12℃","date":"8日星期四"},{"fengxiang":"西北风","fengli":"微风级","high":"高温 -4℃","type":"晴","low":"低温 -12℃","date":"9日星期五"},{"fengxiang":"西南风","fengli":"微风级","high":"高温 -1℃","type":"晴","low":"低温 -12℃","date":"10日星期六"},{"fengxiang":"西南风","fengli":"3-4级","high":"高温 2℃","type":"多云","low":"低温 -4℃","date":"11日星期天"}],"yesterday":{"fl":"3-4级","fx":"西南风","high":"高温 2℃","type":"多云","low":"低温 -11℃","date":"6日星期二"},"aqi":"154","city":"沈阳"}
     */

    private String desc;
    private int status;
    private DataBean data;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * wendu : -10
         * ganmao : 昼夜温差极大，且风力较强，极易发生感冒，请特别注意增减衣服保暖防寒。
         * forecast : [{"fengxiang":"北风","fengli":"3-4级","high":"高温 -2℃","type":"晴","low":"低温 -8℃","date":"7日星期三"},{"fengxiang":"无持续风向","fengli":"3-4级","high":"高温 -4℃","type":"大雪","low":"低温 -12℃","date":"8日星期四"},{"fengxiang":"西北风","fengli":"微风级","high":"高温 -4℃","type":"晴","low":"低温 -12℃","date":"9日星期五"},{"fengxiang":"西南风","fengli":"微风级","high":"高温 -1℃","type":"晴","low":"低温 -12℃","date":"10日星期六"},{"fengxiang":"西南风","fengli":"3-4级","high":"高温 2℃","type":"多云","low":"低温 -4℃","date":"11日星期天"}]
         * yesterday : {"fl":"3-4级","fx":"西南风","high":"高温 2℃","type":"多云","low":"低温 -11℃","date":"6日星期二"}
         * aqi : 154
         * city : 沈阳
         */

        private String wendu;
        private String ganmao;
        private YesterdayBean yesterday;
        private String aqi;
        private String city;
        private List<ForecastBean> forecast;

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            /**
             * fl : 3-4级
             * fx : 西南风
             * high : 高温 2℃
             * type : 多云
             * low : 低温 -11℃
             * date : 6日星期二
             */

            private String fl;
            private String fx;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }

        public static class ForecastBean {
            /**
             * fengxiang : 北风
             * fengli : 3-4级
             * high : 高温 -2℃
             * type : 晴
             * low : 低温 -8℃
             * date : 7日星期三
             */

            private String fengxiang;
            private String fengli;
            private String high;
            private String type;
            private String low;
            private String date;

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
