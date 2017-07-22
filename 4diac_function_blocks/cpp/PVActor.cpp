/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: PVActor
 *** Description: Actor for sending data to the PhotovoltaicsActor plugin controller in Polysun.
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#include "PVActor.h"
#ifdef FORTE_ENABLE_GENERATED_SOURCE_CPP
#include "PVActor_gen.cpp"
#endif

DEFINE_FIRMWARE_FB(FORTE_PVActor, g_nStringIdPVActor)

const CStringDictionary::TStringId FORTE_PVActor::scm_anDataInputNames[] = {g_nStringIdQI, g_nStringIdID, g_nStringIdDF};

const CStringDictionary::TStringId FORTE_PVActor::scm_anDataInputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdLREAL};

const CStringDictionary::TStringId FORTE_PVActor::scm_anDataOutputNames[] = {g_nStringIdQO, g_nStringIdSTATUS};

const CStringDictionary::TStringId FORTE_PVActor::scm_anDataOutputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING};

const TForteInt16 FORTE_PVActor::scm_anEIWithIndexes[] = {0, 3};
const TDataIOID FORTE_PVActor::scm_anEIWith[] = {0, 1, 255, 2, 0, 255};
const CStringDictionary::TStringId FORTE_PVActor::scm_anEventInputNames[] = {g_nStringIdINIT, g_nStringIdRSP};

const TDataIOID FORTE_PVActor::scm_anEOWith[] = {0, 1, 255};
const TForteInt16 FORTE_PVActor::scm_anEOWithIndexes[] = {0, -1};
const CStringDictionary::TStringId FORTE_PVActor::scm_anEventOutputNames[] = {g_nStringIdINITO};

const SFBInterfaceSpec FORTE_PVActor::scm_stFBInterfaceSpec = {
  2,  scm_anEventInputNames,  scm_anEIWith,  scm_anEIWithIndexes,
  1,  scm_anEventOutputNames,  scm_anEOWith, scm_anEOWithIndexes,  3,  scm_anDataInputNames, scm_anDataInputTypeIds,
  2,  scm_anDataOutputNames, scm_anDataOutputTypeIds,
  0, 0
};


const SCFB_FBInstanceData FORTE_PVActor::scm_astInternalFBs[] = {
  {g_nStringIdSERVER_1, g_nStringIdSERVER_1},
};

const SCFB_FBConnectionData FORTE_PVActor::scm_astEventConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINIT), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdINIT), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdRSP), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdRSP), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdINITO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINITO), -1},
};

const SCFB_FBConnectionData FORTE_PVActor::scm_astDataConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQI), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdQI), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdID), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdID), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdDF), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdSD_1), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdQO), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdSERVER_1, g_nStringIdSTATUS), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdSTATUS), -1},
};

const SCFB_FBNData FORTE_PVActor::scm_stFBNData = {
  1, scm_astInternalFBs,
  3, scm_astEventConnections,
  0, 0,
  5, scm_astDataConnections,
  0, 0,
  0, 0
};


