package net.dlogic.kryonet.server;

import java.io.IOException;

public class KryonetServerInstance {
	private static KryonetServer server;
	public static void initialize(int writeBufferSize, int objectBufferSize, int tcpPort, int udpPort) throws IOException, KryonetServerInstanceException {
		if (server != null) {
			throw new KryonetServerInstanceException(KryonetServerInstanceException.ALREADY_INITIALIZED);
		}
		server = new KryonetServer(writeBufferSize, objectBufferSize, tcpPort, udpPort);
	}
	public static KryonetServer getInstance() throws KryonetServerInstanceException {
		if (server == null) {
			throw new KryonetServerInstanceException(KryonetServerInstanceException.NOT_INITIALIZED);
		}
		return server;
	}
}
