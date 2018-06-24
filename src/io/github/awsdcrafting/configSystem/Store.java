package io.github.awsdcrafting.configSystem;
import java.util.ArrayList;
/**
 * Created by scisneromam on 15.11.2017.
 */
public class Store
{

	private String joinMode;
	private ArrayList<String> inviteCodes;

	public Store()
	{
		joinMode = "all";
		inviteCodes = new ArrayList<>();
	}

	public String getJoinMode()
	{
		return joinMode;
	}

	public void setJoinMode(String joinMode)
	{
		this.joinMode = joinMode;
	}

	public ArrayList<String> getInviteCodes()
	{
		return inviteCodes;
	}

	public void addInviteCode(String code)
	{
		inviteCodes.add(code);
	}

	public void removeInviteCode(String code)
	{
		inviteCodes.remove(code);
	}
}
