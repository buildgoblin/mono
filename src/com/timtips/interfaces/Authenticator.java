package com.timtips.interfaces;

public interface Authenticator {
	public boolean isNetworkAvailable();

	public boolean isAuthenticated();

	public void getAuthentication();

	boolean insertHighscore(String name, int score);
}
