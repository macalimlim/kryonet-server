package net.dlogic.kryonet.server;

import java.io.IOException;

import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class KryonetServer {
	public Server endpoint;
	public final KryonetServerListener listener = new KryonetServerListener();
	public KryonetServer(int writeBufferSize, int objectBufferSize) {
		Log.info("KryonetServer(" + writeBufferSize +  ", " + objectBufferSize + ")");
		endpoint = new Server(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(endpoint);
		endpoint.addListener(listener);
	}
	public void start(int tcpPort, int udpPort) throws IOException {
		Log.info("KryonetServer.start(" + tcpPort +  ", " + udpPort + ")");
		endpoint.start();
		endpoint.bind(tcpPort, udpPort);
	}
}
