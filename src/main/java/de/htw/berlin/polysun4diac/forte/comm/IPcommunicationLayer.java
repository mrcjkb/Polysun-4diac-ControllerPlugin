package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;

/**
 * Middle OSI layer for handling IP communication.
 * Delegates the tasks to TCP or UDP communication layers below.
 * Intended for communication with FORTE CLIENT, SERVER, PUBLISH & SUBSCRIBE function blocks.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class IPcommunicationLayer extends AbstractCommunicationLayer {
	
	
	private static final long serialVersionUID = 7413689289580274445L;

	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		switch (params.getServiceType()) {
		case CLIENT:
			setBelow(new TCPclientCommLayer());
			break;
		case SERVER:
			setBelow(new TCPserverCommLayer());
			break;
		case PUBLISHER:
			setBelow(new UDPpublisherCommLayer());
			break;
		case SUBSCRIBER:
			setBelow(new UDPsubscriberCommLayer());
			break;
		}
		setConnectionState(getBelow().openConnection(params));
		return getConnectionState();
	}
}
