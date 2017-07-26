/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: SGReadyHeatPumpAdapter
 *** Description: Actor for sending data to the SG Ready Heat Pump Adapter plugin controller in Polysun.
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#include "SGReadyHeatPumpAdapter.h"
#ifdef FORTE_ENABLE_GENERATED_SOURCE_CPP
#include "SGReadyHeatPumpAdapter_gen.cpp"
#endif

DEFINE_FIRMWARE_FB(FORTE_SGReadyHeatPumpAdapter, g_nStringIdSGReadyHeatPumpAdapter)

const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anDataInputNames[] = {g_nStringIdQI, g_nStringIdID, g_nStringIdCS1, g_nStringIdCS2};

const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anDataInputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdBOOL, g_nStringIdBOOL};

const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anDataOutputNames[] = {g_nStringIdQO, g_nStringIdSTATUS};

const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anDataOutputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING};

const TForteInt16 FORTE_SGReadyHeatPumpAdapter::scm_anEIWithIndexes[] = {0, 3};
const TDataIOID FORTE_SGReadyHeatPumpAdapter::scm_anEIWith[] = {0, 1, 255, 2, 0, 3, 255};
const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anEventInputNames[] = {g_nStringIdINIT, g_nStringIdRSP};

const TDataIOID FORTE_SGReadyHeatPumpAdapter::scm_anEOWith[] = {0, 1, 255};
const TForteInt16 FORTE_SGReadyHeatPumpAdapter::scm_anEOWithIndexes[] = {0, -1};
const CStringDictionary::TStringId FORTE_SGReadyHeatPumpAdapter::scm_anEventOutputNames[] = {g_nStringIdINITO};

const SFBInterfaceSpec FORTE_SGReadyHeatPumpAdapter::scm_stFBInterfaceSpec = {
  2,  scm_anEventInputNames,  scm_anEIWith,  scm_anEIWithIndexes,
  1,  scm_anEventOutputNames,  scm_anEOWith, scm_anEOWithIndexes,  4,  scm_anDataInputNames, scm_anDataInputTypeIds,
  2,  scm_anDataOutputNames, scm_anDataOutputTypeIds,
  0, 0
};


const SCFB_FBInstanceData FORTE_SGReadyHeatPumpAdapter::scm_astInternalFBs[] = {
  {g_nStringIdSERVER_1, g_nStringIdSERVER_2},
  {g_nStringIdCS1, g_nStringIdBOOL2BOOL},
  {g_nStringIdCS2, g_nStringIdBOOL2BOOL},
};

const SCFB_FBConnectionData FORTE_SGReadyHeatPumpAdapter::scm_astEventConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINIT), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdINIT), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdINITO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINITO), -1},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdRSP), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS1, g_nStringIdREQ), 1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS1, g_nStringIdCNF), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS2, g_nStringIdREQ), 2},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS2, g_nStringIdCNF), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdRSP), 0},
};

const SCFB_FBConnectionData FORTE_SGReadyHeatPumpAdapter::scm_astDataConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQI), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdQI), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdID), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdID), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdQO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdSTATUS), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdSTATUS), -1},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdCS1), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS1, g_nStringIdIN), 1},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdCS2), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS2, g_nStringIdIN), 2},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS1, g_nStringIdOUT), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdSD_1), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdCS2, g_nStringIdOUT), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdSD_2), 0},
};

const SCFB_FBNData FORTE_SGReadyHeatPumpAdapter::scm_stFBNData = {
  3, scm_astInternalFBs,
  5, scm_astEventConnections,
  0, 0,
  8, scm_astDataConnections,
  0, 0,
  0, 0
};


