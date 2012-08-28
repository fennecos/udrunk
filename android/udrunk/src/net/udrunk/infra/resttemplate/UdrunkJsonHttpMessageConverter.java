package net.udrunk.infra.resttemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import android.util.Log;

import com.google.gson.Gson;

public class UdrunkJsonHttpMessageConverter extends GsonHttpMessageConverter {

	private Gson gson;
	
	public UdrunkJsonHttpMessageConverter(Gson gson) {
		super(gson);
		this.gson = gson;
	}
	
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Field[] fileds = object.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>();
		for(Field field:fileds)
		{
			try {
				field.setAccessible(true);
				if(field.getType().getName().contains("net.udrunk.domain"))
				{
					if(field.get(object) != null)
					{
						Log.d("UdrunkJsonHttpMessageConverter", "Replace json property with api value :" + field.get(object).getClass().getName());
						Field idField = field.get(object).getClass().getDeclaredField("id");
						idField.setAccessible(true);
						map.put(field.getName(), "/api/v1/" + field.getName() + "/" + idField.get(field.get(object)) + "/");
					}
				}
				else
				{
					map.put(field.getName(), field.get(object));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		
		Log.d("UdrunkJsonHttpMessageConverter", gson.toJson(map));
		outputMessage.getBody().write(gson.toJson(map).getBytes());
	}

}
