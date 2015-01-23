package tag;

import instance.TokenInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IUFCalculator {
	private Map<String, List<String>> userTokenList;

	public IUFCalculator(Map<String, List<String>> tokenListInput) {
		userTokenList = tokenListInput;

	}

	public Map<String, Double> getIUFResult() {
		Map<String, Double> result = new HashMap<String, Double>();

		for (String user : userTokenList.keySet()) {
			for (String token : userTokenList.get(user)) {
				if (!result.keySet().contains(token)) {
					result.put(
							token,
							  log((double) userTokenList.keySet().size()/(double) tokenOccurenceForAll(token), 10));
				}
			}
		}
		return result;
	}

	private double log(double value, double base) {
	    return Math.log(value) / Math.log(base);
	}
	private int tokenOccurenceForAll(String token) {
		int result = 0;
		for (String user : userTokenList.keySet()) {
			if (userTokenList.get(user).contains(token))
				result += 1;
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
