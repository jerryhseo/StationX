package com.sx.constant;

public enum RepositoryTypes {
	USER_HOME("repository"),
	USER_JOBS("jobs"),
	PROVENANCE("provenance"),
	ICECAP("icecap"),
	ICEBUG("icebug"),
	MERIDIAN("meridian"),
	ZODIAC("zodiac"),
	IGLOO("igloo"),
	SCIENCEAPP("scienceapp"),
	SPYGLASS("spyglass"),
	SERVLET("servlet");
	
	private RepositoryTypes(String value) {
		this.value = value;
	}
	
	public boolean equal( String type ){
		return this.value.equalsIgnoreCase(type);
	}
	
	public String value(){
		return this.value;
	}
	
	private String value;
}
