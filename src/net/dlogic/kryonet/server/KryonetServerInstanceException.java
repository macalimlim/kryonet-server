package net.dlogic.kryonet.server;

public class KryonetServerInstanceException extends Exception {
	private static final long serialVersionUID = -4778222321050185155L;
	public static final String ALREADY_INITIALIZED = "server already initialized";
	public static final String NOT_INITIALIZED = "server not initialized";
	public KryonetServerInstanceException(String message) {
		super(message);
	}
}
