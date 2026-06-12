package cn.yanque.models.teaching.schedule.service;

import cn.hutool.core.date.DateUtil;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.schedule.pojo.info.HolidayInfo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HolidayService {

    private static final String HOLIDAY_YEAR_URL = "https://timor.tech/api/holiday/year/";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final Cache<Integer, Map<String, HolidayInfo>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .maximumSize(20)
            .build();

    public HolidayInfo getHolidayInfo(Date date) {
        Integer year = getYear(date);
        Map<String, HolidayInfo> yearHolidayMap = cache.getIfPresent(year);
        if (yearHolidayMap == null) {
            yearHolidayMap = loadYearHoliday(year);
            cache.put(year, yearHolidayMap);
        }
        return yearHolidayMap.get(formatDate(date));
    }

    private Map<String, HolidayInfo> loadYearHoliday(Integer year) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HOLIDAY_YEAR_URL + year))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            //okHttp3
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject body = JSON.parseObject(response.body());
            if (body == null || body.getIntValue("code") != 0) {
                throw BusinessException.DateError.newInstance("节假日信息获取失败");
            }

            JSONObject holiday = body.getJSONObject("holiday");
            Map<String, HolidayInfo> result = new HashMap<>();
            if (holiday == null) {
                return result;
            }

            for (String key : holiday.keySet()) {
                JSONObject item = holiday.getJSONObject(key);
                if (item == null) {
                    continue;
                }
                String dateText = item.getString("date");
                String date = dateText == null ? year + "-" + key : dateText;
                result.put(date, new HolidayInfo(item.getBoolean("holiday"), item.getString("name")));
            }
            return result;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.DateError.newInstance("节假日信息获取失败");
        }
    }

    private Integer getYear(Date date) {
        return DateUtil.year(date);
    }

    private String formatDate(Date date) {
        return DateUtil.format(date, DATE_PATTERN);
    }
}
