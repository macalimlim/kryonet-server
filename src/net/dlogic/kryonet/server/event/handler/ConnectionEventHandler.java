package net.dlogic.kryonet.server.event.handler;

public abstract class ConnectionEventHandler extends BaseEventHandler {
	public abstract void onConnected();
	public abstract void onDisconnected();
	public abstract void onIdle();
}
