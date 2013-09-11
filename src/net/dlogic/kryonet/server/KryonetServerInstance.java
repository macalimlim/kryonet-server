package net.dlogic.kryonet.server;

import java.io.IOException;

public class KryonetServerInstance {
	private static KryonetServer server;
	public static void initialize(int writeBufferSize, int objectBufferSize) throws IOException, KryonetServerException {
		if (server != null) {
			throw new KryonetServerException(KryonetServerException.ALREADY_INITIALIZED);
		}
		server = new KryonetServer(writeBufferSize, objectBufferSize);
	}
	public static KryonetServer getInstance() throws KryonetServerException {
		if (server == null) {
			throw new KryonetServerException(KryonetServerException.NOT_INITIALIZED);
		}
		return server;
	}
}
