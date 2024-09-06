/*
 * Created on 13.08.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

/**
 * @author kunterbunt
 */
public class User {
	public String Name;

	public User() {
		Name = null;// ---Game name
	}

	public User(String aName) {
		Name = aName;// ---Game name
	}

	public String getName() {
		return Name;
	}

	public void setName(String aName) {
		Name = aName;
	}
}