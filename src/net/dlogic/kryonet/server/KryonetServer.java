package net.dlogic.kryonet.server;

import java.io.IOException;

import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class KryonetServer {
	public Server endpoint;
	public KryonetServerListener listener;
	public KryonetServer(int writeBufferSize, int objectBufferSize) {
		Log.info("KryonetServer(" + writeBufferSize +  ", " + objectBufferSize + ")");
		endpoint = new Server(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(endpoint);
		endpoint.addListener(listener = new KryonetServerListener());
	}
	public void start(int tcpPort, int udpPort) throws IOException {
		Log.info("KryonetServer.start(" + tcpPort +  ", " + udpPort + ")");
		endpoint.start();
		endpoint.bind(tcpPort, udpPort);
	}
}
