import java.io.IOException;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.utility.KryonetUtility;
import net.dlogic.kryonet.server.KryonetServer;
import net.dlogic.kryonet.server.KryonetServerException;
import net.dlogic.kryonet.server.KryonetServerInstance;


public class KryonetServerApplication {
	public static void main(String[] args) {
		try {
			int writeBufferSize = Integer.parseInt(args[0]);
			int objectBufferSize = Integer.parseInt(args[1]);
			int tcpPort = Integer.parseInt(args[2]);
			int udpPort = Integer.parseInt(args[3]);
			KryonetServerInstance.initialize(writeBufferSize, objectBufferSize);
			KryonetServer server = KryonetServerInstance.getInstance();
			server.listener.setConnectionEventHandler(MyConnectionEventHandler.class);
			server.listener.setUserEventHandler(MyUserEventHandler.class);
			server.listener.setRoomEventHandler(MyRoomEventHandler.class);
			server.listener.setPersonMessageEventHandler(MyPersonMessageEventHandler.class);
			KryonetUtility.registerClass(server.endpoint, CustomRequest.class);
			KryonetUtility.registerClass(server.endpoint, CustomResponse.class);
			server.endpoint.addListener(new CustomServerListener());
			server.start(tcpPort, udpPort);
			Room room1 = new Room();
			room1.name = "Lobby";
			room1.maxUsers = 16;
			RoomManagerInstance.manager.map.put(room1.name, room1);
			Room room2 = new Room();
			room2.name = "Chat";
			room2.maxUsers = 16;
			RoomManagerInstance.manager.map.put(room2.name, room2);
			Room room3 = new Room();
			room3.name = "Game";
			room3.maxUsers = 16;
			RoomManagerInstance.manager.map.put(room3.name, room3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KryonetServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
