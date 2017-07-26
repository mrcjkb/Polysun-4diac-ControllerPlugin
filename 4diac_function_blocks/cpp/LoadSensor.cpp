/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: LoadSensor
 *** Description: Sensor for receiving data from the LoadSensor plugin controller in Polysun
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#include "LoadSensor.h"
#ifdef FORTE_ENABLE_GENERATED_SOURCE_CPP
#include "LoadSensor_gen.cpp"
#endif

DEFINE_FIRMWARE_FB(FORTE_LoadSensor, g_nStringIdLoadSensor)

const CStringDictionary::TStringId FORTE_LoadSensor::scm_anDataInputNames[] = {g_nStringIdQI, g_nStringIdID, g_nStringIdTSF};

const CStringDictionary::TStringId FORTE_LoadSensor::scm_anDataInputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdBOOL};

const CStringDictionary::TStringId FORTE_LoadSensor::scm_anDataOutputNames[] = {g_nStringIdQO, g_nStringIdSTATUS, g_nStringIdPLD, g_nStringIdTS};

const CStringDictionary::TStringId FORTE_LoadSensor::scm_anDataOutputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdLREAL, g_nStringIdDATE_AND_TIME};

const TForteInt16 FORTE_LoadSensor::scm_anEIWithIndexes[] = {0};
const TDataIOID FORTE_LoadSensor::scm_anEIWith[] = {0, 1, 2, 255};
const CStringDictionary::TStringId FORTE_LoadSensor::scm_anEventInputNames[] = {g_nStringIdINIT};

const TDataIOID FORTE_LoadSensor::scm_anEOWith[] = {0, 1, 255, 2, 3, 255};
const TForteInt16 FORTE_LoadSensor::scm_anEOWithIndexes[] = {0, 3, -1};
const CStringDictionary::TStringId FORTE_LoadSensor::scm_anEventOutputNames[] = {g_nStringIdINITO, g_nStringIdIND};

const SFBInterfaceSpec FORTE_LoadSensor::scm_stFBInterfaceSpec = {
  1,  scm_anEventInputNames,  scm_anEIWith,  scm_anEIWithIndexes,
  2,  scm_anEventOutputNames,  scm_anEOWith, scm_anEOWithIndexes,  3,  scm_anDataInputNames, scm_anDataInputTypeIds,
  4,  scm_anDataOutputNames, scm_anDataOutputTypeIds,
  0, 0
};


const SCFB_FBInstanceData FORTE_LoadSensor::scm_astInternalFBs[] = {
  {g_nStringIdE_SWITCH, g_nStringIdE_SWITCH},
  {g_nStringIdE_SPLIT, g_nStringIdE_SPLIT},
  {g_nStringIdE_SPLIT_1, g_nStringIdE_SPLIT},
  {g_nStringIdINITO_MERGE, g_nStringIdE_MERGE},
  {g_nStringIdIND_MERGE, g_nStringIdE_MERGE},
  {g_nStringIdQO_SEL, g_nStringIdF_SEL},
  {g_nStringIdPPV_SEL, g_nStringIdF_SEL},
  {g_nStringIdTSF_1_PLD, g_nStringIdLREAL2LREAL},
  {g_nStringIdTSF_0_PLD, g_nStringIdLREAL2LREAL},
  {g_nStringIdBOOL2BOOL, g_nStringIdBOOL2BOOL},
  {g_nStringIdF_CONCAT, g_nStringIdF_CONCAT},
  {g_nStringIdTSF_0_SERVER, g_nStringIdSERVER_0_1},
  {g_nStringIdTSF_1_SERVER, g_nStringIdSERVER_0_2},
};

const SCFB_FBConnectionData FORTE_LoadSensor::scm_astEventConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINIT), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEI), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEO2), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PLD, g_nStringIdREQ), 7},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PLD, g_nStringIdCNF), 8, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEI1), 4},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEO), 4, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdREQ), 6},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdCNF), 5, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdREQ), 9},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEO), 3, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdREQ), 10},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdCNF), 10, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdREQ), 5},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdCNF), 9, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINITO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEO0), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdINIT), 11},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEO1), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdRSP), 11},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdINITO), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEI1), 3},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdIND), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEI), 1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEO1), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdINIT), 12},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEO1), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRSP), 12},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdINITO), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEI2), 3},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdIND), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEI), 2},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEO2), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PLD, g_nStringIdREQ), 8},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PLD, g_nStringIdCNF), 7, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEI2), 4},
};

const SCFB_FBConnectionData FORTE_LoadSensor::scm_astDataConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdTSF), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdG), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PLD, g_nStringIdOUT), 7, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdIN1), 6},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdOUT), 6, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdPLD), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdOUT), 5, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdIN), 9},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdOUT), 9, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdOUT), 10, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdSTATUS), -1},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQI), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdQI), 11},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdID), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdID), 11},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdQO), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdIN0), 5},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdSTATUS), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdIN1), 10},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdQO), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdIN1), 5},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdSTATUS), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdIN2), 10},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRD_1), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PLD, g_nStringIdIN), 7},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRD_2), 12, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdTS), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdRD_1), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PLD, g_nStringIdIN), 8},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PLD, g_nStringIdOUT), 8, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdIN0), 6},
};

const SCFB_FBFannedOutConnectionData FORTE_LoadSensor::scm_astFannedOutDataConnections[] = {
  {0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdG), 5},
  {0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdG), 6},
  {6, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdQI), 12},
  {7, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdID), 12},
};

const SCFB_FBNData FORTE_LoadSensor::scm_stFBNData = {
  13, scm_astInternalFBs,
  18, scm_astEventConnections,
  0, 0,
  16, scm_astDataConnections,
  4, scm_astFannedOutDataConnections,
  0, 0
};


