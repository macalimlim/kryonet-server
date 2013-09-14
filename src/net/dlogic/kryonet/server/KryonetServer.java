package net.dlogic.kryonet.server;

import java.io.IOException;

import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class KryonetServer {
	private Server server;
	private KryonetServerListener listener;
	public KryonetServer(int writeBufferSize, int objectBufferSize) {
		Log.info("KryonetServer(" + writeBufferSize +  ", " + objectBufferSize + ")");
		server = new Server(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(server);
		server.addListener(listener = new KryonetServerListener());
	}
	public void start(int tcpPort, int udpPort) throws IOException {
		Log.info("KryonetServer.start(" + tcpPort +  ", " + udpPort + ")");
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
	public Server getServer() {
		return server;
	}
}
