package com.cdeledu.openplatform.baidu.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cdeledu.network.common.UrlHelper;
import com.cdeledu.openplatform.baidu.model.PlaceApi;

/**
 * @描述:
 * 		<ul>
 *      <li>是一类简单的HTTP接口,用于返回查询某个区域的某类POI数据且提供单个POI的详情查询服务</li>
 *      <li>提供区域检索POI服务、POI详情服务与团购信息检索服务、商家团购详情查询</li>
 *      <li>区域检索POI:城市内检索、矩形检、索圆形区域检索</li>
 *      <li>POI详情服务提供查询单个POI的详情信息，如好评</li>
 *      <li>团购信息检索服务:城市内检索 、矩形检索、矩形检索</li>
 *      <li>商家团购详情查询</li>
 *      </ul>
 * @author: 独泪了无痕
 * @date: 2014年10月02日 下午4:26:24
 * @version: V1.2
 * @history:
 */
public class PlaceUtil {
	/** place区域检索POI服务 */
	private final static String PLACE_SEARCH = "http://api.map.baidu.com/place/v2/search";
	/** POI详情服务 */
	private final static String PLACE_DETAIL = " http://api.map.baidu.com/place/v2/detail";

	/**
	 * 
	 * @Title：SearchInfoByQueryKeyword @Description：
	 *                                 <ul>
	 *                                 <li>区域检索POI服务只之城市内检索</li>
	 *                                 <li></li>
	 *                                 </ul>
	 * @param ak
	 *            用户的访问密钥，必填项
	 * @param query
	 *            <ul>
	 *            <li>检索关键字。是必须的，不能为空</li>
	 *            <li>周边检索和矩形区域内检索支持多个关键字并集检索</li>
	 *            <li>不同关键字间以$符号分隔，最多支持10个关键字检索</li>
	 *            </ul>
	 * @param page_num
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推
	 * @param region
	 *            检索区域，如果取值为“全国”或某省份，则返回指定区域的POI
	 * @return
	 * @return：Map<String,Object> 返回类型
	 */
	public static Map<String, Object> SearchInfoByQueryKeyword(String ak, String query, String page_num,
			String region) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		HttpURLConnection l_connection = null;
		BufferedReader l_reader = null;
		String msg = null;
		String jsonString = "";

		String page_size = "20";

		if (StringUtils.isBlank(page_num)) {
			page_num = "0";
		}
		if (StringUtils.isBlank(region)) {
			region = "全国";
		}

		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("ak", ak);
			paramsMap.put("output", "json");
			paramsMap.put("query", query);
			/** 范围记录数量,默认为10条记录,最大返回20条.多关键字检索时,返回的记录数为关键字个数*page_size */
			paramsMap.put("page_size", page_size);
			paramsMap.put("page_num", page_num);
			/** 检索结果详细程度--1 或空:则返回基本信息;2:返回检索POI详细信息 */
			paramsMap.put("scope", "2");
			// paramsMap.put("scope", "2");
			paramsMap.put("region", region);

			String url = UrlHelper.formatParameters(PLACE_SEARCH, paramsMap);

			URL l_url = new URL(url);
			l_connection = (HttpURLConnection) l_url.openConnection();
			l_connection.connect();
			l_reader = new BufferedReader(new InputStreamReader(l_connection.getInputStream()));
			while ((msg = l_reader.readLine()) != null) {
				jsonString = jsonString + msg;
				jsonString = jsonString.replace(" ", "");
			}
			resultmap = JsonStringToMap(jsonString);
		} catch (Exception e) {
			// e.printStackTrace();
			resultmap.put("status", 1);
		} finally {
			try {
				if (l_reader != null) {
					l_reader.close();
				}
				if (l_connection != null) {
					l_connection.disconnect();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
				resultmap.put("status", 1);
			}
		}

		return resultmap;
	}

	/**
	 * 
	 * @Title：SearchInfoByLocationAndRadius
	 * @Description：区域检索POI服务只之圆形区域检索
	 * @param ak
	 *            用户的访问密钥，必填项
	 * @param query
	 *            <ul>
	 *            <li>检索关键字。是必须的，不能为空</li>
	 *            <li>周边检索和矩形区域内检索支持多个关键字并集检索</li>
	 *            <li>不同关键字间以$符号分隔，最多支持10个关键字检索</li>
	 *            </ul>
	 * @param page_num
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推
	 * @param lng
	 *            经度 (必须)
	 * @param lat
	 *            纬度 (必须)
	 * @param radius
	 *            周边检索半径，单位为米
	 * @return
	 * @return：Map<String,Object> 返回类型
	 */
	public static Map<String, Object> SearchInfoByLocationAndRadius(String ak, String query, String page_num, float lng,
			float lat, int radius) {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		String msg = null;
		String jsonString = "";

		String location = lat + "," + lng;

		Map<String, Object> resultmap = new HashMap<String, Object>();
		// 返回记录数量，默认为10条记录，最大返回结果为20条。{
		String page_size = "20";

		if (StringUtils.isBlank(page_num)) {
			page_num = "0";
		}
		if (radius == 0) {
			radius = 1000;
		}

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("ak", ak);
		paramsMap.put("output", "json");
		paramsMap.put("query", query);
		paramsMap.put("page_size", page_size);
		paramsMap.put("page_num", page_num);
		paramsMap.put("scope", "2");
		paramsMap.put("location", location);
		paramsMap.put("radius", String.valueOf(radius));

		String urlStr = UrlHelper.formatParameters(PLACE_SEARCH, paramsMap);
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((msg = reader.readLine()) != null) {
				jsonString = jsonString + msg;
				jsonString = jsonString.replace(" ", "");
			}
			resultmap = JsonStringToMap(jsonString);
		} catch (Exception e) {
			// e.printStackTrace();
			resultmap.put("status", 1);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
				resultmap.put("status", 1);
			}
		}
		return resultmap;
	}

	/**
	 * 
	 * @Title：SearchInfoByPlaceDetail
	 * @Description：提供查询某个POI点的详情信息，如好评，评价等。 @param ak 用户的访问密钥，必填项。
	 * @param uid
	 *            poi的uid
	 * @return
	 * @return：Map<String,Object> 返回类型
	 */
	public static Map<String, Object> SearchInfoByPlaceDetail(String ak, String uid) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		HttpURLConnection l_connection = null;
		BufferedReader l_reader = null;
		String msg = null;
		String jsonString = "";
		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("ak", ak);
			paramsMap.put("output", "json");
			/** 检索结果详细程度--1 或空:则返回基本信息;2:返回检索POI详细信息 */
			paramsMap.put("scope", "2");
			paramsMap.put("uid", uid);

			String url = UrlHelper.formatParameters(PLACE_DETAIL, paramsMap);

			URL l_url = new URL(url);
			l_connection = (HttpURLConnection) l_url.openConnection();
			l_connection.connect();

			l_reader = new BufferedReader(new InputStreamReader(l_connection.getInputStream()));

			while ((msg = l_reader.readLine()) != null) {
				jsonString = jsonString + msg;
				jsonString = jsonString.replace(" ", "");
			}
			resultmap = JsonStringToMap(jsonString);
		} catch (Exception e) {
			// e.printStackTrace();
			resultmap.put("status", 0);
		} finally {
			try {
				if (l_reader != null) {
					l_reader.close();
				}
				if (l_connection != null) {
					l_connection.disconnect();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
				resultmap.put("status", 0);
			}
		}

		return resultmap;
	}

	/**
	 * 
	 * @title : JsonStringToMap
	 * 
	 * @author : 独泪了无痕
	 * 
	 * @描述 : 将结果解析
	 * 
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 * @throws NumberFormatException
	 */
	private static Map<String, Object> JsonStringToMap(String jsonString) throws JSONException, NumberFormatException {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		List<PlaceApi> resultList = new ArrayList<PlaceApi>();
		JSONObject json = new JSONObject(jsonString);
		if (json.get("status").equals(0)) {
			if (json.has("results")) {
				JSONArray j_result = (JSONArray) json.get("results");
				for (int i = 0; i < j_result.length(); i++) {
					String j_result_str = String.valueOf(j_result.get(i));
					PlaceApi info = baiduMapTag(j_result_str);
					resultList.add(info);
				}
			} else if (json.has("result")) {
				String j_result_str = json.get("result").toString();
				PlaceApi info = baiduMapTag(j_result_str);
				resultList.add(info);
			}
			if (json.has("total")) {
				resultmap.put("total", json.getInt("total"));
			}
			resultmap.put("status", 0);
			resultmap.put("result", resultList);
		} else {
			resultmap.put("status", 1);
		}
		return resultmap;
	}

	private static PlaceApi baiduMapTag(String j_result_str) throws JSONException, NumberFormatException {
		JSONObject result = new JSONObject(j_result_str);
		PlaceApi info = new PlaceApi();
		info.setName(String.valueOf(result.get("name")));
		String location = String.valueOf(result.get("location"));
		JSONObject j_location = new JSONObject(location);
		info.setLng(Float.parseFloat(j_location.get("lng").toString()));
		info.setLat(Float.parseFloat(j_location.get("lat").toString()));

		info.setAddress(String.valueOf(result.get("address")));

		if (result.has("telephone")) {
			info.setTelephone(String.valueOf(result.get("telephone")));
		}
		if (result.has("street_id")) {
			info.setStreet_id(String.valueOf(result.get("street_id")));
		}

		info.setUid(String.valueOf(result.get("uid")));
		String detail_info = String.valueOf(result.get("detail_info"));
		JSONObject j_detail_info = new JSONObject(detail_info);
		// 距离中心点的距离
		if (j_detail_info.has("distance")) {
			info.setDistance(j_detail_info.getInt("distance"));
		}
		// 所属分类，如’hotel’、’cater’
		if (j_detail_info.has("type")) {
			info.setType(j_detail_info.getString("type"));
		}
		// 标签
		if (j_detail_info.has("tag")) {
			info.setTag(j_detail_info.getString("tag"));
		}
		// POI的详情页
		if (j_detail_info.has("detail_url")) {
			info.setDetail_url(j_detail_info.getString("detail_url"));
		}
		// POI的商户的价格
		if (j_detail_info.has("price")) {
			info.setPrice(j_detail_info.getString("price"));
		}
		// 营业时间
		if (j_detail_info.has("shop_hours")) {
			info.setShop_hours(j_detail_info.getString("shop_hours"));
		}
		// 总体评分
		if (j_detail_info.has("overall_rating")) {
			String overall = j_detail_info.getString("overall_rating");
			info.setOverall_rating(overall);
		}
		// 口味评分
		if (j_detail_info.has("taste_rating")) {
			String taste = j_detail_info.getString("taste_rating");
			info.setTaste_rating(taste);
		}
		// 服务评分
		if (j_detail_info.has("service_rating")) {
			String service = j_detail_info.getString("service_rating");
			info.setService_rating(service);
		}
		// 环境评分
		if (j_detail_info.has("environment_rating")) {
			String env = j_detail_info.getString("environment_rating");
			info.setEnvironment_rating(env);
		}
		// 星级（设备）评分
		if (j_detail_info.has("facility_rating")) {
			String fac = j_detail_info.getString("facility_rating");
			info.setFacility_rating(fac);
		}
		// 卫生评分
		if (j_detail_info.has("hygiene_rating")) {
			String hyg = j_detail_info.getString("hygiene_rating");
			info.setHygiene_rating(hyg);
		}
		// 技术评分
		if (j_detail_info.has("technology_rating")) {
			String tec = j_detail_info.getString("technology_rating");
			info.setTechnology_rating(tec);
		}
		// 图片数
		if (j_detail_info.has("image_num")) {
			info.setImage_num(j_detail_info.getString("image_num"));
		}
		// 团购数
		if (j_detail_info.has("groupon_num")) {
			info.setGroupon_num(j_detail_info.getInt("groupon_num"));
		}
		// 优惠数
		if (j_detail_info.has("discount_num")) {
			info.setDiscount_num(j_detail_info.getInt("discount_num"));
		}
		// 评论数
		if (j_detail_info.has("comment_num")) {
			info.setComment_num(j_detail_info.getString("comment_num"));
		}
		// 收藏数
		if (j_detail_info.has("favorite_num")) {
			String fav = j_detail_info.getString("favorite_num");
			info.setFavorite_num(fav);
		}
		// 签到数
		if (j_detail_info.has("checkin_num")) {
			info.setCheckin_num(j_detail_info.getString("checkin_num"));
		}
		return info;
	}
}
