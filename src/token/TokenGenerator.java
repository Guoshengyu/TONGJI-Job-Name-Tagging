package token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import automation.SystemParamater;

public class TokenGenerator {



	// public static final String REGEX = "\\.{2,}";

	public List<String> generateTokenList(String jobName) {
		List<String> resultList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(jobName,
				SystemParamater.TOKEN_SPLIT_SYMBOL);
		while (st.hasMoreElements()) {
			String token = st.nextToken();

			if (SystemParamater.NUMERIC_TOKEN == false)
				// For numeric strings
				if (!isNumeric(token))
					resultList.add(token);
		}
		return resultList;

	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TokenGenerator test = new TokenGenerator();
		System.out
				.println(test
						.generateTokenList("[6AA29612B4524A0E8B1F4D2BE997700E/6E685383F1964FA0B437F6A2615FE96C] com.ebay.research.items2vecs.MakeRichItemsJob/(3/7)"));
	}

}
