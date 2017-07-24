package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Class for setting up a connection between a Polysun PluginController and a 4diac application running on 4diac-RTE (FORTE).
 * An instance of this class is initialized with the parameters (IP socket address, port number) used for building the OSI communication layers. 
 * This class implements an IP Socket Address (IP address + port number) It can also be a pair (hostname + port number), in which case an attempt will be made to resolve the hostname. If resolution fails then the address is said to be unresolved but can still be used on some circumstances like connecting through a proxy. 
 * It provides an immutable object used by sockets for binding, connecting, or as returned values. 
 * The wildcard is a special local IP address. It usually means "any" and can only be used for bind operations.
 * </p>
 * This class can be used to generate an IForteSocket object, which can be used to communicate with a communication service interface function block (CSIFB) on FORTE.
 * Before creating the IForteSocket object, the ForteServiceType should be set (if none is set, the default is ForteServiceType.CLIENT) using the
 * {@link #setServiceType(ForteServiceType)} method.
 * </p>
 * Additionally, the inputs and outputs must be set by calling {@link #addInput(ForteDataType)} and {@link #addOutput(ForteDataType)} (if the numbers of inputs/outputs differ from each other)
 * or by calling {@link #addInputOutput(ForteDataType)} if the numbers of inputs/outputs are the same.
 * To add arrays as inputs or outputs, the following methods can be called: {@link #addInput(ForteDataType, int)}, {@link #addOutput(ForteDataType, int)} and {@link #addInputOutput(ForteDataType, int)}.
 * If no output is added, calling recvData() on the generated IForteSocket will await a response from the connected FORTE CSIFB.
 * Likewise, if no input is added, calling sendData() on the generated IForteSocket will send a response to the connected FORTE CSIFB.
 * </p>
 * Once all the parameters are set, an IForteSocket can be initialized by calling this class's {@link #makeIPSocket()} method.
 * </p>
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.ForteServiceType
 * @see de.htw.berlin.polysun4diac.forte.comm.AbstractCommunicationLayer
 * @see de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class CommLayerParams extends InetSocketAddress {

	private static final long serialVersionUID = -2972598493588554524L;
	/** Default service type */
	private static final ForteServiceType DEFSERVICE = ForteServiceType.CLIENT;
	/** Default array length */
	private static final int DEFARRAYLENGTH = 1;
	
	/**
	 * Used by the IPCommunicationLayer to determine which layer to set up below.
	 */
	private ForteServiceType mServiceType;
	/** ForteDataType enumerations of the data types to be sent to FORTE */
	private List<Enum<?>> mInputDataTypes;
	/** ForteDataType enumerations of the data types to be received from FORTE */
	private List<Enum<?>> mOutputDataTypes;
	/** Array lengths of the input data types */
	private List<Integer> mInputArrayLengths;
	/** Array lengths of the output data types */
	private List<Integer> mOutputArrayLengths;

	/**
	 * Creates a set of communication layer parameters with the default address and a specified port.
	 * @param port the port number. A valid port value is between 0 and 65535. A port number of zero will let the system pick up an ephemeral port in a bind operation. 
	 */
	public CommLayerParams(int port) {
		super(port);
		setServiceType(DEFSERVICE);
		initLists();
	}
	
	/**
	 * Creates a set of communication layer parameters.
	 * @param addr the IP address
	 * @param port the port number. A valid port value is between 0 and 65535. A port number of zero will let the system pick up an ephemeral port in a bind operation. 
	 * @param serviceType one of the 4 IEC 61499 communication service interface function block (CSIFB) types.
	 */
	public CommLayerParams(InetAddress addr, int port) {
		super(addr, port);
		setServiceType(DEFSERVICE);
		initLists();
	}
	
	/**
	 * Creates a set of communication layer parameters.
	 * @param hostname the Host name. An attempt will be made to resolve the hostname into an InetAddress. If that attempt fails, the address will be flagged as unresolved. If there is a security manager, its checkConnect method is called with the host name as its argument to check the permissiom to resolve it. This could result in a SecurityException. 
	 * @param port the port number. A valid port value is between 0 and 65535. A port number of zero will let the system pick up an ephemeral port in a bind operation.
	 * @param serviceType one of the 4 IEC 61499 communication service interface function block (CSIFB) types.
	 */
	public CommLayerParams(String hostname, int port) {
		super(hostname, port);
		setServiceType(DEFSERVICE);
		initLists();
	}
	
	/**
	 * @return A ForteServiceType enum used by the IPCommunicationLayer to determine which layer to set up below
	 */
	public ForteServiceType getServiceType() {
		return mServiceType;
	}
	
	/** 
	 * Sets the ForteServiceType enum used by the IPCommunicationLayer to determine which layer to set up below.
	 */
	public void setServiceType(ForteServiceType type) {
		mServiceType = type;
	}
	
	/**
	 * @return an InetSocketAddress object with this object's IP and port settings.
	 */
	public InetSocketAddress getInetSocketAddress() {
		return (InetSocketAddress) this;
	}
	
	/**
	 * Adds a data input to the set-up parameters.
	 * The inputs are sent to FORTE and their types should correspond with those of the function block on the receiving end.
	 * Inputs are initialized in the order at which they were added to this object.
	 * @param dataType the FORTE data type of the input.
	 */
	public void addInput(ForteDataType dataType) {
		addInput(dataType, DEFARRAYLENGTH);
	}
	
	/**
	 * Adds a data input with a specified array length to the set-up parameters.
	 * The inputs are sent to FORTE and their types should correspond with those of the function block on the receiving end.
	 * Inputs are initialized in the order at which they were added to this object.
	 * @param dataType the FORTE data type of the input.
	 * @param arrayLength length of the array.
	 */
	public void addInput(ForteDataType dataType, int arrayLength) {
		if (!getInputs().isEmpty() && ForteDataType.NONE.equals(getInputs().get(0))) { // First input to be added by client?
			if (ForteDataType.NONE.equals(dataType)) {
				return; // Ignore this method call if NONE has already been added.
			}
			// Clear input lists before adding first input
			getInputs().clear();
			getInputArrayLengths().clear();
		}
		getInputs().add(dataType);
		getInputArrayLengths().add(arrayLength);
	}
	
	/**
	 * Adds a data output to the set-up parameters.
	 * The outputs are received from FORTE and their types should correspond with those of the function block on the sending end.
	 * Outputs are initialized in the order at which they were added to this object.
	 * @param dataType the FORTE data type of the output.
	 */
	public void addOutput(ForteDataType dataType) {
		addOutput(dataType, DEFARRAYLENGTH);
	}
	
	/**
	 * Adds a data output with a specified array length to the set-up parameters.
	 * The outputs are received from FORTE and their types should correspond with those of the function block on the sending end.
	 * Outputs are initialized in the order at which they were added to this object.
	 * @param dataType the FORTE data type of the output
	 * @param arrayLength length of the array
	 */
	public void addOutput(ForteDataType dataType, int arrayLength) {
		if (!getOutputs().isEmpty() && ForteDataType.NONE.equals(getOutputs().get(0))) { // First output to be added by client?
			if (ForteDataType.NONE.equals(dataType)) {
				return; // Ignore this method call if NONE has already been added.
			}
			// Clear output lists before adding first output
			getOutputs().clear();
			getOutputArrayLengths().clear();
		}
		getOutputs().add(dataType);
		getOutputArrayLengths().add(arrayLength);
	}
	
	/**
	 * Adds a data type as an input and as an output at the same time.
	 * @param dataType the FORTE input/output
	 */
	public void addInputOutput(ForteDataType dataType) {
		addInputOutput(dataType, DEFARRAYLENGTH);
	}
	
	/**
	 * Adds a data type with a specified array length as an input and as an output at the same time.
	 * @param dataType the FORTE input/output
	 * @param arrayLength length of the array
	 */
	public void addInputOutput(ForteDataType dataType, int arrayLength) {
		addInput(dataType, arrayLength);
		addOutput(dataType, arrayLength);
	}
	
	/**
	 * @return data types to be sent.
	 */
	public List<Enum<?>> getInputs() {
		return mInputDataTypes;
	}
	
	/**
	 * @return data types to be received.
	 */
	public List<Enum<?>> getOutputs() {
		return mOutputDataTypes;
	}
	
	/**
	 * @return array lengths of the data inputs to be sent to FORTE
	 */
	public List<Integer> getInputArrayLengths() {
		return mInputArrayLengths;
	}
	
	/**
	 * @return array lengths of the data outputs to be received from FORTE
	 */
	public List<Integer> getOutputArrayLengths() {
		return mOutputArrayLengths;
	}
	
	/**
	 * @return an IP server socket that can be used to communicate with an IEC 61499 CLIENT, SERVER, SUBSCRIBE or PUBLISH function block, depending on the parameters set for this object.
	 * The connection is opened by passing this object to the socket's openConnection() method.
	 * @throws IOException 
	 */
	public IForteSocket makeIPSocket() throws IOException {
		if (isSameInputsOutputs()) {
			ForteDataBufferLayer socket = new ForteDataBufferLayer();
			socket.setBelow(new IPcommunicationLayer());
			socket.openConnection(this);
			return socket;
		}
		// Inputs and outputs vary from one another.
		CommFunctionBlockLayer socket = new CommFunctionBlockLayer();
		socket.setBelow(new IPcommunicationLayer());
		socket.openConnection(this);
		return socket;
	}
	
	/**
	 * @return true if the inputs and outputs are the same types, false otherwise
	 */
	private boolean isSameInputsOutputs() {
		if (getInputs().size() != getOutputs().size()) {
			return false;
		}
		for (int i = 0; i < getInputs().size(); i++) {
			Enum<?> in = getInputs().get(i);
			Enum<?> out = getOutputs().get(i);
			int inLength = getInputArrayLengths().get(i);
			int outLength = getOutputArrayLengths().get(i);
			if (!in.equals(out) || inLength != outLength) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Initializes the lists for initializing data types with no inputs/outputs.
	 */
	private void initLists() {
		mInputDataTypes = new ArrayList<Enum<?>>();
		mOutputDataTypes = new ArrayList<Enum<?>>();
		mInputArrayLengths = new ArrayList<Integer>();
		mOutputArrayLengths = new ArrayList<Integer>();
		addInput(ForteDataType.NONE, DEFARRAYLENGTH);
		addOutput(ForteDataType.NONE, DEFARRAYLENGTH);
	}
}
