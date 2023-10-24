package application.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Mod {
	public final String name, url, checksum;
	
	public Mod(String name, String url, String checksum) {
		this.name = name;
		this.url = url;
		this.checksum = checksum;
	}
	
	public Mod(JsonObject json) throws JsonSyntaxException {
		if(!json.has("name") || !json.has("url") || !json.has("checksum")) {
			throw new JsonSyntaxException("Missing parameter");
		}
		this.name = json.get("name").getAsString();
		this.url = json.get("url").getAsString();
		this.checksum = json.get("checksum").getAsString();
	}
}
