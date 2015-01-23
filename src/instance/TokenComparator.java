package instance;

import java.util.Comparator;

public class TokenComparator implements Comparator<TokenInfo> {
	
    @Override
    public int compare(TokenInfo o1, TokenInfo o2) {
        
        return Double.compare(o1.getValue(), o2.getValue());
    }   
}
