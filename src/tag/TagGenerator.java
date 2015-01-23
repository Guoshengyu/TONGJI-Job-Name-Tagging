package tag;

import file.FileReader;
import instance.TokenInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import automation.SystemParamater;
import token.TokenGenerator;

public class TagGenerator {


	private TokenGenerator tokenGenerator;

	private Map<String, List<String>> userJobName;
	private Map<String, List<String>> userToken;
	private Map<String, List<TokenInfo>> userTokenInfo;
	private Map<String, List<String>> userTag;

	public TagGenerator(Map<String, List<String>> userJobNameInput) {
		userJobName = new LinkedHashMap<String, List<String>>(userJobNameInput);
		userToken = new LinkedHashMap<String, List<String>>(); // could have
																// duplicate
																// tokens
		userTokenInfo = new LinkedHashMap<String, List<TokenInfo>>();// no
																		// duplication
		userTag = new LinkedHashMap<String, List<String>>();
		tokenGenerator = new TokenGenerator();
	}

	public Map<String, List<String>> getUserTag() {
		//Map<String, List<String>> result = new HashMap<String, List<String>>();

		// 1 generate user token lib by splitting
		// there could be duplicate tokens for one user
		generateUserToken();

		// 2 generate inverse user frequency
		// 3 generate token frequency
		calculateTFandIUF();

		// 4 generate tag lib
		getUserTaglib();
		
		return userTag;
	}

	private void getUserTaglib() {
		List<String> tagList = new ArrayList<String>();
		for (String user : userTokenInfo.keySet()) {
			
			//Tag selection method
			
			for (TokenInfo info : userTokenInfo.get(user)) {
				info.setValue(info.getTF() * info.getIUF());
				if (info.getValue() >= SystemParamater.TAG_VALUE_THRESHOLD) {
					tagList.add(info.getToken());
				}
			}
			
				
			
			userTag.put(user, new ArrayList<String>(tagList));
			tagList.clear();

		};
	}

	private void calculateTFandIUF() {
		IUFCalculator iufc = new IUFCalculator(userToken);
		Map<String, Double> resultTokenIUF = iufc.getIUFResult();

		for (String user : userToken.keySet()) {

			TFCalculator tfc = new TFCalculator(userToken.get(user));

			Map<String, Double> resultTokenTF = tfc.getTFResult();

			List<TokenInfo> tokenInfoList = new ArrayList<TokenInfo>();
			for (String token : resultTokenTF.keySet()) {
				TokenInfo info = new TokenInfo();
				info.setToken(token);
				info.setTF(resultTokenTF.get(token));
				info.setIUF(resultTokenIUF.get(token));
				tokenInfoList.add(new TokenInfo(info));
			}
			userTokenInfo.put(new String(user), new ArrayList<TokenInfo>(
					tokenInfoList));
			tokenInfoList.clear();
		}
	}

	private void generateUserToken() {
		List<String> tokenList = new ArrayList<String>();

		for (String user : userJobName.keySet()) {

			List<String> jobList = userJobName.get(user);
			for (String job : jobList) {
				tokenList.addAll(new ArrayList<String>(tokenGenerator
						.generateTokenList(job)));
			}
			userToken.put(user, new ArrayList<String>(tokenList));
			tokenList.clear();
		}
		return;
	}

	public Map<String, List<TokenInfo>> getUserTokenInfo() {
		return userTokenInfo;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader reader = new FileReader();
		Map<String, List<String>> userJobNameInput = reader.getUserJobNameMap();
		TagGenerator test = new TagGenerator(userJobNameInput);
		test.getUserTag();
	}

}
