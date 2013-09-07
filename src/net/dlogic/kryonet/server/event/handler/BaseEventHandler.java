package net.dlogic.kryonet.server.event.handler;

import com.esotericsoftware.kryonet.Connection;

public abstract class BaseEventHandler {
	protected Connection connection;
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
