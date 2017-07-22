package de.htw.berlin.polysun4diac.forte.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Bottom OSI layer for handling TCP/IP communication of a server.
 * Intended for communication with FORTE CLIENT function blocks.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class TCPserverCommLayer extends TCPcommunicationLayer {

	private static final long serialVersionUID = 9093178573162069160L;
	
	/** Server socket */
	private ServerSocket mServer = null;
	
	/**
	 * Default constructor.
	 */
	public TCPserverCommLayer() {
		try {
			mServer = new ServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		getServer().bind(params); // Open service on IP & port
		setSocket(getServer().accept()); // Listen for connection to be made and accept
		setInputStream(new DataInputStream(getSocket().getInputStream()));
		setOutputStream(new DataOutputStream(getSocket().getOutputStream()));
		return getConnectionState();
	}
	
	@Override
	public boolean closeConnection() throws IOException {
		super.closeConnection();
		getServer().close();
		return getConnectionState();
	}
	
	/** @return the server socket */
	private ServerSocket getServer() {
		return mServer;
	}
}
