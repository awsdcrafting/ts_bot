package io.github.awsdcrafting.utils;
import java.util.ArrayList;
import java.util.regex.Pattern;
/**
 * Created by scisneromam on 06.12.2017.
 */
public class RegexMatcher
{

	private ArrayList<Pattern> patterns = new ArrayList<>();

	public boolean addPattern(String regex){
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		return patterns.add(p);
	}

	public boolean removePattern(String regex){
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		return patterns.remove(p);
	}

	public boolean removePattern(Pattern pattern){
		return patterns.remove(pattern);
	}

	public boolean matchesAPattern(String word){

		for(Pattern p : patterns){
			if(p.matcher(word).matches()||p.matcher(word).lookingAt()){
				return true;
			}
		}
		return false;

	}


}
