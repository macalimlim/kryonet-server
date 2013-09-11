package net.dlogic.kryonet.server;

import java.io.IOException;

import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryonetServer {
	private Server server;
	private KryonetServerListener listener;
	public KryonetServer(int writeBufferSize, int objectBufferSize) {
		server = new Server(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(server);
		server.addListener(listener = new KryonetServerListener());
	}
	public void start(int tcpPort, int udpPort) throws IOException {
		server.start();
		server.bind(tcpPort, udpPort);
	}
	public KryonetServerListener getKryonetServerListener() {
		return listener;
	}
	public void addListener(Listener listener) {
		server.addListener(listener);
	}
	public void registerClass(Class type) {
		server.getKryo().register(type);
	}
}
