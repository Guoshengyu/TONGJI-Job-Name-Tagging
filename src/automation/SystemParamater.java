package automation;

public class SystemParamater {

	public static String cluster_name = "apollo";
	public static String data_date = "0324";
	public static String SOURCE_FILE_NAME = "C:\\Users\\Richard\\Desktop\\EBAY DATA\\"
			+ cluster_name + "\\" + cluster_name + "_" + data_date + ".xlsx";

	public static String NON_SQL_FILE_NAME = "C:\\Users\\Richard\\Desktop\\EBAY DATA\\"
			+ cluster_name
			+ "\\"
			+ cluster_name
			+ "_non_sql_"
			+ data_date
			+ ".xlsx";

	public static String NORM_FILE_NAME = "C:\\Users\\Richard\\Desktop\\EBAY DATA\\"
			+ cluster_name
			+ "\\"
			+ cluster_name
			+ "_norm_"
			+ data_date
			+ ".xlsx";

	public static String TAG_FILE_NAME = "C:\\Users\\Richard\\Desktop\\EBAY DATA\\"
			+ cluster_name
			+ "\\"
			+ cluster_name
			+ "_no_numeric_tag_"
			+ data_date + ".xlsx";

	public static final String TOKEN_SPLIT_SYMBOL = "$~:[()-].', ;/<>=+{}@*&^#!~";
	public static boolean NUMERIC_TOKEN = false;
	public static boolean NORMALIZATION_REQUEST = true;
	public static boolean FIRST_TOKEN_AS_TAG = true;

	public static enum TAG_SELECTION_METHOD {
		THRESHOLD, TOP_RANK
	};

	public static final double TAG_VALUE_THRESHOLD = 0.01;
	public static final int MAX_TOKEN_FOR_UNTAG_JOB = 5;

}
