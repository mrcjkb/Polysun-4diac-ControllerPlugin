/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: BatteryActor
 *** Description: Actor for sending data to the BatteryActor plugin controller in Polysun.
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#include "BatteryActor.h"
#ifdef FORTE_ENABLE_GENERATED_SOURCE_CPP
#include "BatteryActor_gen.cpp"
#endif

DEFINE_FIRMWARE_FB(FORTE_BatteryActor, g_nStringIdBatteryActor)

const CStringDictionary::TStringId FORTE_BatteryActor::scm_anDataInputNames[] = {g_nStringIdQI, g_nStringIdID, g_nStringIdPSET, g_nStringIdCM};

const CStringDictionary::TStringId FORTE_BatteryActor::scm_anDataInputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdLREAL, g_nStringIdBOOL};

const CStringDictionary::TStringId FORTE_BatteryActor::scm_anDataOutputNames[] = {g_nStringIdQO, g_nStringIdSTATUS};

const CStringDictionary::TStringId FORTE_BatteryActor::scm_anDataOutputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING};

const TForteInt16 FORTE_BatteryActor::scm_anEIWithIndexes[] = {0, 3};
const TDataIOID FORTE_BatteryActor::scm_anEIWith[] = {0, 1, 255, 2, 3, 0, 255};
const CStringDictionary::TStringId FORTE_BatteryActor::scm_anEventInputNames[] = {g_nStringIdINIT, g_nStringIdRSP};

const TDataIOID FORTE_BatteryActor::scm_anEOWith[] = {0, 1, 255};
const TForteInt16 FORTE_BatteryActor::scm_anEOWithIndexes[] = {0, -1};
const CStringDictionary::TStringId FORTE_BatteryActor::scm_anEventOutputNames[] = {g_nStringIdINITO};

const SFBInterfaceSpec FORTE_BatteryActor::scm_stFBInterfaceSpec = {
  2,  scm_anEventInputNames,  scm_anEIWith,  scm_anEIWithIndexes,
  1,  scm_anEventOutputNames,  scm_anEOWith, scm_anEOWithIndexes,  4,  scm_anDataInputNames, scm_anDataInputTypeIds,
  2,  scm_anDataOutputNames, scm_anDataOutputTypeIds,
  0, 0
};


const SCFB_FBInstanceData FORTE_BatteryActor::scm_astInternalFBs[] = {
  {g_nStringIdSERVER_2, g_nStringIdSERVER_2},
};

const SCFB_FBConnectionData FORTE_BatteryActor::scm_astEventConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINIT), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdINIT), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdRSP), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdRSP), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdINITO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINITO), -1},
};

const SCFB_FBConnectionData FORTE_BatteryActor::scm_astDataConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQI), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdQI), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdID), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdID), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdPSET), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdSD_1), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdCM), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdSD_2), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdQO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_2, g_nStringIdSTATUS), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdSTATUS), -1},
};

const SCFB_FBNData FORTE_BatteryActor::scm_stFBNData = {
  1, scm_astInternalFBs,
  3, scm_astEventConnections,
  0, 0,
  6, scm_astDataConnections,
  0, 0,
  0, 0
};


