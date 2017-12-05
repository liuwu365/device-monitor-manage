package com.device.manage.core.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description Json 工具类
 */
public class JsonUtil {

	private static Gson gson = new Gson();

	private static Gson gsonFormat = new GsonBuilder().setPrettyPrinting().create();

	private static Gson serializeNulls = new GsonBuilder().serializeNulls().create();

	public static String toJson(Object src) {
		return toJson(src, null);
	}

	public static String toJson(Object src, JsonNull jsonNull) {
		if (jsonNull != null) {
			return serializeNulls.toJson(src);
		} else {
			return gson.toJson(src);
		}
	}


	public static <T> T fromJson(String src, Class<T> clazz) {
		final T t = serializeNulls.fromJson(src, clazz);
		return gson.fromJson(src, clazz);
	}

	/**
	 * 封装将json对象转换为java集合对象
	 *
	 * @param <T>
	 * @param clazz
	 * @param json
	 * @return
	 */
	private static <T> List<T> getJavaCollection(T clazz, String json) {
		List<T> objs = null;
		JsonParser parser = new JsonParser();

		JsonElement element = parser.parse(json);
		JsonArray jsonArray = element.getAsJsonArray();
		if (jsonArray.isJsonArray()) {
			objs = new ArrayList<T>();
			for (Object o : jsonArray) {
				T O1 = gson.fromJson(gson.toJson(o), (Type) clazz);
				objs.add(O1);
			}
		}
		return objs;
	}

}
