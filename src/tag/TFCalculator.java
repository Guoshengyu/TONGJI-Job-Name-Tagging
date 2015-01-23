package tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFCalculator {

	private List<String> tokenList;
	public TFCalculator(List<String> tokenListInput){
		tokenList = tokenListInput;
		
	}
	public  Map<String, Double> getTFResult(){
		Map<String, Double> result = new HashMap<String, Double>();
		
		for(String token: tokenList){			
			if(!result.keySet().contains(token)){
				int occurence = Collections.frequency(tokenList, token);
				result.put(token, ((double)occurence / (double)tokenList.size()));
			}
		}
		 return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
