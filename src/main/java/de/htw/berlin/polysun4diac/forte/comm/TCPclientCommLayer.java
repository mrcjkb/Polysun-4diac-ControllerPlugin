package de.htw.berlin.polysun4diac.forte.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Bottom OSI layer for handling TCP/IP communication of a client.
 * Intended for communication with FORTE SERVER function blocks.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class TCPclientCommLayer extends TCPcommunicationLayer {

	private static final long serialVersionUID = 6388891008984581796L;
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		getSocket().connect(params); // Connect to IP and port
		setInputStream(new DataInputStream(getSocket().getInputStream()));
		setOutputStream(new DataOutputStream(getSocket().getOutputStream()));
		return getConnectionState();
	}
}
