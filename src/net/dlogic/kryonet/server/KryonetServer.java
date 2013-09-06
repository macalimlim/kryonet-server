package net.dlogic.kryonet.server;

import java.io.IOException;

import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Server;

public class KryonetServer {
	private static Server server;
	public KryonetServer(int writeBufferSize, int objectBufferSize, int tcpPort, int udpPort) throws IOException {
		server = new Server(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(server);
		server.start();
		server.bind(tcpPort, udpPort);
		server.addListener(new KryonetServerListener());
	}
}
