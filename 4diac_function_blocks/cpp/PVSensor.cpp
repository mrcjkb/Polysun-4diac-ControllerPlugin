/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: PVSensor
 *** Description: Sensor for receiving data from the PhotovoltaicsSensor plugin controller in Polysun
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#include "PVSensor.h"
#ifdef FORTE_ENABLE_GENERATED_SOURCE_CPP
#include "PVSensor_gen.cpp"
#endif

DEFINE_FIRMWARE_FB(FORTE_PVSensor, g_nStringIdPVSensor)

const CStringDictionary::TStringId FORTE_PVSensor::scm_anDataInputNames[] = {g_nStringIdQI, g_nStringIdID, g_nStringIdTSF};

const CStringDictionary::TStringId FORTE_PVSensor::scm_anDataInputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdBOOL};

const CStringDictionary::TStringId FORTE_PVSensor::scm_anDataOutputNames[] = {g_nStringIdQO, g_nStringIdSTATUS, g_nStringIdPPV, g_nStringIdGFL, g_nStringIdTS};

const CStringDictionary::TStringId FORTE_PVSensor::scm_anDataOutputTypeIds[] = {g_nStringIdBOOL, g_nStringIdWSTRING, g_nStringIdLREAL, g_nStringIdLREAL, g_nStringIdDATE_AND_TIME};

const TForteInt16 FORTE_PVSensor::scm_anEIWithIndexes[] = {0};
const TDataIOID FORTE_PVSensor::scm_anEIWith[] = {0, 1, 2, 255};
const CStringDictionary::TStringId FORTE_PVSensor::scm_anEventInputNames[] = {g_nStringIdINIT};

const TDataIOID FORTE_PVSensor::scm_anEOWith[] = {0, 1, 255, 2, 3, 4, 255};
const TForteInt16 FORTE_PVSensor::scm_anEOWithIndexes[] = {0, 3, -1};
const CStringDictionary::TStringId FORTE_PVSensor::scm_anEventOutputNames[] = {g_nStringIdINITO, g_nStringIdIND};

const SFBInterfaceSpec FORTE_PVSensor::scm_stFBInterfaceSpec = {
  1,  scm_anEventInputNames,  scm_anEIWith,  scm_anEIWithIndexes,
  2,  scm_anEventOutputNames,  scm_anEOWith, scm_anEOWithIndexes,  3,  scm_anDataInputNames, scm_anDataInputTypeIds,
  5,  scm_anDataOutputNames, scm_anDataOutputTypeIds,
  0, 0
};


const SCFB_FBInstanceData FORTE_PVSensor::scm_astInternalFBs[] = {
  {g_nStringIdTSF_1_SERVER, g_nStringIdSERVER_0_3},
  {g_nStringIdTSF_0_SERVER, g_nStringIdSERVER_0_2},
  {g_nStringIdE_SWITCH, g_nStringIdE_SWITCH},
  {g_nStringIdE_SPLIT, g_nStringIdE_SPLIT},
  {g_nStringIdE_SPLIT_1, g_nStringIdE_SPLIT},
  {g_nStringIdINITO_MERGE, g_nStringIdE_MERGE},
  {g_nStringIdIND_MERGE, g_nStringIdE_MERGE},
  {g_nStringIdQO_SEL, g_nStringIdF_SEL},
  {g_nStringIdPPV_SEL, g_nStringIdF_SEL},
  {g_nStringIdTSF_0_GFL, g_nStringIdLREAL2LREAL},
  {g_nStringIdTSF_1_GFL, g_nStringIdLREAL2LREAL},
  {g_nStringIdTSF_0_PPV, g_nStringIdLREAL2LREAL},
  {g_nStringIdTSF_1_PPV, g_nStringIdLREAL2LREAL},
  {g_nStringIdGFL_SEL, g_nStringIdF_SEL},
  {g_nStringIdBOOL2BOOL, g_nStringIdBOOL2BOOL},
  {g_nStringIdF_CONCAT, g_nStringIdF_CONCAT},
};

const SCFB_FBConnectionData FORTE_PVSensor::scm_astEventConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINIT), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEI), 2},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEO0), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdINIT), 1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdEO1), 2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdINIT), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEO1), 3, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdRSP), 1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdIND), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEI), 4},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEO1), 4, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRSP), 0},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEO2), 3, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_GFL, g_nStringIdREQ), 9},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT_1, g_nStringIdEO2), 4, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_GFL, g_nStringIdREQ), 10},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_GFL, g_nStringIdCNF), 9, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PPV, g_nStringIdREQ), 11},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_GFL, g_nStringIdCNF), 10, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PPV, g_nStringIdREQ), 12},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PPV, g_nStringIdCNF), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEI1), 6},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PPV, g_nStringIdCNF), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEI2), 6},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdIND_MERGE, g_nStringIdEO), 6, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdREQ), 8},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdCNF), 8, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdREQ), 13},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdCNF), 13, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdIND), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdCNF), 7, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdREQ), 14},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdIND), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SPLIT, g_nStringIdEI), 3},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdINITO), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEI1), 5},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdINITO), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEI2), 5},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdINITO_MERGE, g_nStringIdEO), 5, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdREQ), 15},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdCNF), 15, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdREQ), 7},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdCNF), 14, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdINITO), -1},
};

const SCFB_FBConnectionData FORTE_PVSensor::scm_astDataConnections[] = {
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQI), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdQI), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdID), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdID), 0},
  {GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdTSF), -1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdE_SWITCH, g_nStringIdG), 2},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdQO), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdIN0), 7},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdQO), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdIN1), 7},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdRD_1), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_GFL, g_nStringIdIN), 9},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRD_1), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_GFL, g_nStringIdIN), 10},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_GFL, g_nStringIdOUT), 9, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdIN0), 8},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_GFL, g_nStringIdOUT), 10, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdIN1), 8},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdRD_2), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PPV, g_nStringIdIN), 11},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRD_2), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PPV, g_nStringIdIN), 12},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdRD_3), 0, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdTS), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_PPV, g_nStringIdOUT), 11, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdIN0), 13},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_PPV, g_nStringIdOUT), 12, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdIN1), 13},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdOUT), 8, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdPPV), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdOUT), 13, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdGFL), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdOUT), 7, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdIN), 14},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdBOOL2BOOL, g_nStringIdOUT), 14, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdQO), -1},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdSTATUS), 1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdIN1), 15},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_1_SERVER, g_nStringIdSTATUS), 0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdIN2), 15},
  {GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdF_CONCAT, g_nStringIdOUT), 15, GENERATE_CONNECTION_PORT_ID_1_ARG(g_nStringIdSTATUS), -1},
};

const SCFB_FBFannedOutConnectionData FORTE_PVSensor::scm_astFannedOutDataConnections[] = {
  {0, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdQI), 1},
  {1, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdTSF_0_SERVER, g_nStringIdID), 1},
  {2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdQO_SEL, g_nStringIdG), 7},
  {2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdPPV_SEL, g_nStringIdG), 8},
  {2, GENERATE_CONNECTION_PORT_ID_2_ARG(g_nStringIdGFL_SEL, g_nStringIdG), 13},
};

const SCFB_FBNData FORTE_PVSensor::scm_stFBNData = {
  16, scm_astInternalFBs,
  22, scm_astEventConnections,
  0, 0,
  21, scm_astDataConnections,
  5, scm_astFannedOutDataConnections,
  0, 0
};


