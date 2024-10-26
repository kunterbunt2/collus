/*
 * Copyright (C) 2024 Abdalla Bushnaq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import java.util.LinkedList;

/**
 * @author kunterbunt
 */
public class GameList extends LinkedList<Game> {
    public static int              GAME_EDIT_MODE_INDEX = 1;
    public static int              GAME_LEGACY_INDEX    = 0;
    public static int              GAME_UI_INDEX        = 2;
    protected     int              SelectedGame         = 0;
    protected     int              SelectedUser         = 0;
    private final LinkedList<User> UserList             = new LinkedList<>();

    public GameList() {
        add(new LegacyGame());
        add(new EditMode());
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