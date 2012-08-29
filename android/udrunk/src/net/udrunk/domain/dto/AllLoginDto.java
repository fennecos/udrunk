package net.udrunk.domain.dto;

import java.util.ArrayList;

import net.udrunk.domain.Login;

public class AllLoginDto {
	public ArrayList<Login> objects;
	
	public Login getLogin()
	{
		if(objects != null && objects.size() > 0)
		{
			return objects.get(0);
		}
		return null;
	}
}
