package com.cdeledu.openplatform.baidu;

import java.util.Map;

import com.cdeledu.openplatform.baidu.model.LatitudeInfo;
import com.cdeledu.openplatform.baidu.util.GeocodingUtil;
import com.cdeledu.openplatform.baidu.util.PlaceSuggestionUtil;
import com.cdeledu.openplatform.baidu.util.PlaceUtil;

/**
 * @ClassName: BaiduMap
 * @Description: 百度地图工具类(调用百度地图接口)
 * @author: 独泪了无痕
 * @date: 2015年7月17日 下午1:37:25
 * @version: V1.0
 * @since: JDK 1.7
 * @see <a
 *      href="http://developer.baidu.com/map/index.php?title=webapi">百度地图Web服务API
 *      </a>
 */
@SuppressWarnings("rawtypes")
public class BaiduMap {
	/**
	 * @Title: SearchLocalByGeocoding_API
	 * @Description: 通过 GEocoding API 根据经纬度查询周边信息
	 * @author: 独泪了无痕
	 * @param ak
	 * @param lng
	 *            经度值
	 * @param lat
	 *            纬度值
	 * @return
	 */
	public static Map<String, Object> SearchLocalByGeocoding_API(String ak,
			float lng, float lat) {
		return GeocodingUtil.SearchLocalByGeocodingAPI(ak, lng, lat);
	}

	/**
	 * @Title: SearchLatitudeByGeocoding_API
	 * @Description: 发送一个地址请求，返回该地址对应的地理坐标(经纬度)
	 * @author: 独泪了无痕
	 * @param ak
	 * @param address
	 * @param city
	 * @return
	 */
	public static LatitudeInfo SearchLatitudeByGeocoding_API(String ak,
			String address, String city) {
		return GeocodingUtil.SearchLatitudeByGeocodingAPI(ak, address, city);
	}

	/**
	 * @Title: SearchInfoByQueryKeyword
	 * @Description: 城市内检索关键字
	 * @author: 独泪了无痕
	 * @param ak
	 * @param query
	 * @param page_num
	 * @param region
	 * @return
	 */
	public static Map SearchInfoByQueryKeyword(String ak, String query,
			String page_num, String region) {
		return PlaceUtil.SearchInfoByQueryKeyword(ak, query, page_num, region);
	}

	/**
	 * @Title: SearchInfoByLocationAndRadius
	 * @Description: 根据经纬度查询周围半径1000米范围之内的信息<br>
	 *               每次最多查询20个
	 * @author: 独泪了无痕
	 * @param ak
	 * @param query
	 * @param page_num
	 * @param lng
	 * @param lat
	 * @param radius
	 * @return
	 */
	public static Map SearchInfoByLocationAndRadius(String ak, String query,
			String page_num, float lng, float lat, int radius) {
		return PlaceUtil.SearchInfoByLocationAndRadius(ak, query, page_num,
				lng, lat, radius);
	}

	/**
	 * @Title: SearchInfoByPlaceDetail
	 * @Description: 查询POI唯一标识查询
	 * @author: 独泪了无痕
	 * @param ak
	 * @param uid
	 * @return
	 */
	public static Map SearchInfoByPlaceDetail(String ak, String uid) {
		return PlaceUtil.SearchInfoByPlaceDetail(ak, uid);
	}

	/**
	 * @Title: SearchAuxiliaryInfoByQueryKeyword
	 * @Description: 匹配用户输入关键字辅助信息
	 * @author: 独泪了无痕
	 * @param ak
	 * @param query
	 * @param region
	 * @return
	 */
	public static Map SearchAuxiliaryInfoByQueryKeyword(String ak,
			String query, String region) {
		return PlaceSuggestionUtil.SearchInfoByQueryKeyword(ak, query, region);
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}
