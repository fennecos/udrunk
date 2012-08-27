package net.udrunk.infra;

import java.lang.reflect.Type;

import net.udrunk.domain.Point;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PointSerializer implements JsonSerializer<Point> {

	@Override
	public JsonElement serialize(Point arg0, Type arg1,
			JsonSerializationContext arg2) {
		return new JsonPrimitive("POINT(" + arg0.x + " " + arg0.y + ")");
	}

}
