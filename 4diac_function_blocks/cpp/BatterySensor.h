/*************************************************************************
 *** FORTE Library Element
 ***
 *** This file was generated using the 4DIAC FORTE Export Filter V1.0.x!
 ***
 *** Name: BatterySensor
 *** Description: Sensor for receiving data from the BatterySensor plugin controller in Polysun
 *** Version: 
 ***     1.0: 2017-07-21/Marc Jakobi - HTW Berlin - 
 *************************************************************************/

#ifndef _BATTERYSENSOR_H_
#define _BATTERYSENSOR_H_

#include <cfb.h>
#include <typelib.h>
#include <forte_date_and_time.h>
#include <forte_bool.h>
#include <forte_wstring.h>
#include <forte_lreal.h>

class FORTE_BatterySensor: public CCompositeFB{
  DECLARE_FIRMWARE_FB(FORTE_BatterySensor)

private:
  static const CStringDictionary::TStringId scm_anDataInputNames[];
  static const CStringDictionary::TStringId scm_anDataInputTypeIds[];
  CIEC_BOOL &QI() {
    return *static_cast<CIEC_BOOL*>(getDI(0));
  };

  CIEC_WSTRING &ID() {
    return *static_cast<CIEC_WSTRING*>(getDI(1));
  };

  CIEC_BOOL &TSF() {
    return *static_cast<CIEC_BOOL*>(getDI(2));
  };

  static const CStringDictionary::TStringId scm_anDataOutputNames[];
  static const CStringDictionary::TStringId scm_anDataOutputTypeIds[];
  CIEC_BOOL &QO() {
    return *static_cast<CIEC_BOOL*>(getDO(0));
  };

  CIEC_WSTRING &STATUS() {
    return *static_cast<CIEC_WSTRING*>(getDO(1));
  };

  CIEC_LREAL &SOC() {
    return *static_cast<CIEC_LREAL*>(getDO(2));
  };

  CIEC_LREAL &PB() {
    return *static_cast<CIEC_LREAL*>(getDO(3));
  };

  CIEC_DATE_AND_TIME &TS() {
    return *static_cast<CIEC_DATE_AND_TIME*>(getDO(4));
  };

  static const TEventID scm_nEventINITID = 0;
  static const TForteInt16 scm_anEIWithIndexes[];
  static const TDataIOID scm_anEIWith[];
  static const CStringDictionary::TStringId scm_anEventInputNames[];

  static const TEventID scm_nEventINITOID = 0;
  static const TEventID scm_nEventINDID = 1;
  static const TForteInt16 scm_anEOWithIndexes[];
  static const TDataIOID scm_anEOWith[];
  static const CStringDictionary::TStringId scm_anEventOutputNames[];

  static const SFBInterfaceSpec scm_stFBInterfaceSpec;

   FORTE_FB_DATA_ARRAY(2, 3, 5, 0);

  static const SCFB_FBInstanceData scm_astInternalFBs[];

  static const SCFB_FBConnectionData scm_astEventConnections[];

  static const SCFB_FBConnectionData scm_astDataConnections[];

  static const SCFB_FBFannedOutConnectionData scm_astFannedOutDataConnections[];
  static const SCFB_FBNData scm_stFBNData;

public:
  COMPOSITE_FUNCTION_BLOCK_CTOR(FORTE_BatterySensor){
  };

  virtual ~FORTE_BatterySensor(){};

};

#endif //close the ifdef sequence from the beginning of the file

