package io.github.awsdcrafting.poll;
import java.util.ArrayList;
/**
 * Created by scisneromam on 21.08.2017.
 */
public class Umfrage
{
	private String name;
	private String question;
	private String[] answers;
	private boolean closed;

	public Umfrage(String name, String question, String[] answers){
		this.name = name;
		this.question = question;
		this.answers = answers;
		closed = false;
	}
	
	public String getName(){
		return name;
	}
	
	public String getQuestion(){
		return question;
	}

	public String[] getAnswers(){
		return answers;
	}

	public boolean getClosed(){
		return closed;
	}


	
}
