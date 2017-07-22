package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Abstract Bottom OSI layer for handling UDP/IP communication.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public abstract class UDPcommunicationLayer extends AbstractCommunicationLayer {

	private static final long serialVersionUID = 5885138813433553399L;
	
	@Override
	public boolean closeConnection() throws IOException {
		if (getSocket() != null) {
			getSocket().close();
		}
		return getConnectionState();
	}
	
	@Override
	public boolean getConnectionState() {
		if (getSocket() == null) {
			return false;
		}
		return !getSocket().isClosed() && super.getConnectionState();
	}
	
	/** @return the internal socket object */
	protected abstract DatagramSocket getSocket();
	
	/** Sets the internal socket object and binds it to a port */
	protected abstract void makeSocket(int port) throws IOException;
}
