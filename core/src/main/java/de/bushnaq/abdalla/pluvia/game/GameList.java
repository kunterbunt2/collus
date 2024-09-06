/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import java.util.LinkedList;

/**
 * @author kunterbunt
 *
 */
public class GameList extends LinkedList<Game> {
	protected int				SelectedGame	= 0;
	protected int				SelectedUser	= 0;
	private LinkedList<User>	UserList		= new LinkedList<>();

	public GameList() {
		add(new LegacyGame());
		add(new UiGame());
	}

//	public void add(Game aGame) {
//		GameList.add(aGame);
//	}

	public void add(User aUser) {
		boolean exists = false;
		for (int i = 0; i < getUserListSize(); i++) {
			if (aUser.Name == getUser(i).Name) {
				exists = true;
			} else {
			}
		}
		if (exists) {
		} else {
			UserList.add(aUser);
		}
	}

//	public Game getGame(int aIndex) {
//		return GameList.get(aIndex);
//	}

//	public int getGameListSize() {
//		return GameList.size();
//	}

	public int getSelectedGame() {
		return SelectedGame;
	}

	public int getSelectedUser() {
		return SelectedUser;
	}

	public User getUser(int aIndex) {
		return UserList.get(aIndex);
	}

	public int getUserListSize() {
		return UserList.size();
	}

	public void removeUser(int aIndex) {
		UserList.remove(aIndex);
	}

	public void setSelectedGame(int aSelectedGame) {
		SelectedGame = aSelectedGame;
	}

	public void setSelectedUser(int aSelectedUser) {
		SelectedUser = aSelectedUser;
	}

}