package com.sx.constant;

public class StationXConstants {
	public static final String CRF_FILE_FOLDER_NAME = "CRF";
	
	public static final String CMD_ADD = "add";
	public static final String CMD_UPDATE = "update";
	
	public static final String ASC = "asc";
	public static final String DSC = "desc";
	
	public static final String DEFAULT_SEARCH_CONTAINER_EVENT_NAME="defaultSearchContainerEventName";

	public static final String NAVIGATION_ALL = "all";
	public static final String NAVIGATION_MINE = "mine";
	public static final String NAVIGATION_GROUP = "group";
	public static final String NAVIGATION_GROUP_MINE = "groupMine";
	
	public static final String VIEW_TYPE_LIST = "descriptive";
	public static final String VIEW_TYPE_CARDS = "icon";
	public static final String VIEW_TYPE_TABLE = "table";
	
	public static final String LIST_DISPLAY_STYLE_SELECT = "select";
	public static final String LIST_DISPLAY_STYLE_RADIO = "radio";
	public static final String LIST_DISPLAY_STYLE_CHECK = "check";

	public static final String DEFAULT_DATE_FORMAT = "yyyy. MM. dd.";
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy. MM. dd. h:m";
	
	public static final int DEFAULT_START = 0;
	public static final int DEFAULT_DELTA = 10;
	public static final int DEFAULT_END = DEFAULT_DELTA - DEFAULT_START ;
	public static final String DEFAULT_NAVIGATION = NAVIGATION_MINE;
	
	public static final String[] NAVIGATION_KEYS() {
		return new String[] { NAVIGATION_ALL, NAVIGATION_GROUP, NAVIGATION_MINE };
	}
}
