package pers.cs.download.api.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import pers.cs.download.api.Connection;
import pers.cs.download.api.ConnectionManager;

public class ConnectionManagerImpl implements ConnectionManager {

	@Override
	public Connection open(String url) throws MalformedURLException, IOException {
		return new ConnectionImpl(url);
	}

}
