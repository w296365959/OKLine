package com.ICE.VOIP.ui;

public class SIGNALSTATE {
	public final static String TAG = "SIGNALSTATE";


///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////	
//必须 与SDK中 VoIPCore 工程的 SignalState.h 内的值保持一致   ---Lijs 2013.12.06
///////////////////////////////////////////////////////////////////////////////////////		
public static final int SIGNALSTATE_COMMONBASE = 0  ;       //!< The common base event info.      No.0
public static final int SIGNALSTATE_DESTROY = 1  ;          //!< Notify application to destroy.
public static final int SIGNALSTATE_LOG_ING = 2  ;          //!< Loging to server 
public static final int SIGNALSTATE_LOGON = 3  ;            //!< Logined success
public static final int SIGNALSTATE_LOGONFAIL = 4  ;        //!< Login Failed
public static final int SIGNALSTATE_CALL_UNKNOW = 5  ;      //!< Know call status yet.
public static final int SIGNALSTATE_CALL_FREE = 6  ;        //!< Free call status
public static final int SIGNALSTATE_CALL_GW = 7  ;          //!< Call through GW status
public static final int SIGNALSTATE_CALL_PSTN = 8  ;        //!< PSTN call status
public static final int SIGNALSTATE_INFOREADY = 9  ;        //!< Information is ready.
public static final int SIGNALSTATE_ASSOFORWARD = 10 ;      //!< Whether forward PSTN call by ASSO endpoint.   No. 10
public static final int SIGNALSTATE_GWFORWARDLOCAL = 11 ;   //!< Whether forward local PSTN from gateway.
public static final int SIGNALSTATE_LOCALGATEWAY = 12 ;     //!< Current is a local city GW forward.
public static final int SIGNALSTATE_CIDRECEIVED = 13 ;      //!< Whether CID has been received.
public static final int SIGNALSTATE_ASSOCIATE = 14 ;        //!< Associate state notify.
public static final int SIGNALSTATE_WELCOMEINFO = 15 ;      //!< The operation welcome information
public static final int SIGNALSTATE_STARTCRC = 16 ;         //!< Jitter buffer has started CRC mechanism.
public static final int SIGNALSTATE_INTERVALCALL = 17 ;     //!< Sync associate interval call.
public static final int SIGNALSTATE_3NCALL = 18 ;           //!< Sync associate 3n call.
public static final int SIGNALSTATE_PSTNCALL = 19 ;         //!< Sync associate pstn call.
public static final int SIGNALSTATE_NONECALL = 20 ;         //!< Sync associate none call.  No. 20                    
public static final int SIGNALSTATE_INITPWD = 21 ;          //!< Init 600 pass word.
public static final int SIGNALSTATE_SYNCAREACODE = 22 ;     //!< Sync associate areacode.
public static final int SIGNALSTATE_SMSRECV = 23 ; 		    //!< Sms show.
public static final int SIGNALSTATE_SMSSENDOVER = 24 ;	    //!< one Sms send over.
	  
public static final int SIGNALSTATE_ASSOONLINE = 25 ;       //!< 800 online. 
public static final int SIGNALSTATE_ASSOOUTLINE = 26 ;      //!< 800 outline.
	  
public static final int SIGNALSTATE_690BASE = 27 ;          //!< 690 login state(for 690) 
public static final int SIGNALSTATE_LOGSTATE = 28 ;         //!< 690 login state(for 690)
public static final int SIGNALSTATE_CALLSTATE = 29 ;        //!< Call device state(for 690)
public static final int SIGNALSTATE_GETDEVPARAMACK = 30 ;   //!< Device param(for 690)      No. 30
public static final int SIGNALSTATE_SETDEVPARAMACK = 31 ;   //!< Set device param result(for 690)
public static final int SIGNALSTATE_SETVOLUME = 32 ;        //!< Set device's microphone or earphone volume(for 690)
public static final int SIGNALSTATE_GETVOLUME = 33 ;        //!< Get device's microphone or earphone volume(for 690)    
public static final int SIGNALSTATE_3NBASE = 34 ;           //!< The clien3N base event info.
public static final int SIGNALSTATE_SENDINVITATION = 35 ;   //!< Start to send invitation to remote No. 35
public static final int SIGNALSTATE_QUERYGATEWAY = 36 ;     //!< It's querying gateway
public static final int SIGNALSTATE_ACCEPTINVITTION = 37 ;  //!< Remote has accepted current invitiation
public static final int SIGNALSTATE_ACCEPTCONNECTION = 38 ; //!< Remote is accepted the call
public static final int SIGNALSTATE_REMOTERINGING = 39 ;    //!< Remote is ringing
public static final int SIGNALSTATE_CONNECTING = 40 ;       //!< It's connecting with remote       No. 40
public static final int SIGNALSTATE_STARTBILLING = 41 ;     //!< Start billing for talking
public static final int SIGNALSTATE_DISCONNECT = 42 ;       //!< Disconnection                     
public static final int SIGNALSTATE_RESET = 43 ;            //!< Stop session
	  
public static final int SIGNALSTATE_LEDBASE = 44 ;          //!< The LED base event info.        
public static final int SIGNALSTATE_INFO_NEW = 45 ;         //!< New info had been received      
public static final int SIGNALSTATE_INFO_UNREAD = 46 ;      //!< There are some message information have not been read
public static final int SIGNALSTATE_INFO_EMPTY = 47 ;       //!< All message information had been cleared
public static final int SIGNALSTATE_PHONE_INUSE = 48 ;      //!< Phone device is inused
public static final int SIGNALSTATE_PHONE_UNUSE = 49 ;      //!< Phone device is unused
public static final int SIGNALSTATE_PHONE_DTMF = 50 ;       //!< Phone device is dialing          No. 50
public static final int SIGNALSTATE_LINE_PLUGIN = 51 ;      //!< Line has been plugged in
public static final int SIGNALSTATE_LINE_UNPLUG = 52 ;      //!< Line has been unplugged
public static final int SIGNALSTATE_LINE_INUSE = 53 ;       //!< Line is inused
public static final int SIGNALSTATE_LINE_UNUSE = 54 ;       //!< Line is unused
public static final int SIGNALSTATE_VOIP_INUSE = 55 ;       //!< VOIP is inused
public static final int SIGNALSTATE_VOIP_UNUSE = 56 ;       //!< VOIP is unused
public static final int SIGNALSTATE_VOIP_PSTNBEGIN = 57 ;   //!< VOIP PSTN mode begin, for 810 platform
public static final int SIGNALSTATE_VOIP_PSTNEND = 58 ;     //!< VOIP PSTN mode end, for 810 platform
public static final int SIGNALSTATE_3N_INUSE = 59 ;         //!< It's dialing 3N number
public static final int SIGNALSTATE_3N_UNUSE = 60 ;         //!< It's unuse 3N number             No. 60
public static final int SIGNALSTATE_BIND_START = 61 ;       //!< Binding is started
public static final int SIGNALSTATE_BIND_SUCCESS = 62 ;     //!< Bind success
public static final int SIGNALSTATE_BIND_FAIL = 63 ;        //!< Bind failed
public static final int SIGNALSTATE_WAN_PLUGIN = 64 ;       //!< Wan has been plugged in
public static final int SIGNALSTATE_WAN_UNPLUG = 65 ;       //!< Wan has been unplugged
public static final int SIGNALSTATE_LAN_PLUGIN = 66 ;       //!< Lan has been plugged in
public static final int SIGNALSTATE_LAN_UNPLUG = 67 ;       //!< Lan has been unplugged

public static final int SIGNALSTATE_FAILBASE = 68 ;         //!< The base value for fail error. 
public static final int SIGNALSTATE_LESSNUMBERLEN = 69 ;    //!< Dial number has not enought length
public static final int SIGNALSTATE_INVALIDNUMBER = 70 ;    //!< Invalid dial number                No. 70
public static final int SIGNALSTATE_INVITESELF = 71 ;       //!< The dial number is self
public static final int SIGNALSTATE_NOTBINDPSTN = 72 ;      //!< Remote has not binded PSTN
public static final int SIGNALSTATE_NOTREACHABLE = 73 ;     //!< Can't reach to remote
public static final int SIGNALSTATE_NOPERMISSION = 74 ;     //!< No permission to call
public static final int SIGNALSTATE_REMOTEOFFLINE = 75 ;    //!< Remote is off line
public static final int SIGNALSTATE_REMOTEREJECT = 76 ;     //!< Remote has rejected current invitiation
public static final int SIGNALSTATE_REGSERVERBUSY = 77 ;    //!< Register server is busy
public static final int SIGNALSTATE_CONNSERVERBUSY = 78 ;   //!< Connection server is busy
public static final int SIGNALSTATE_DATABASEERROR = 79 ;    //!< Database error
public static final int SIGNALSTATE_CONNECTFAILED = 80 ;    //!< Conncet failed                   No. 80
public static final int SIGNALSTATE_CONNECTFULL = 81 ;      //!< Connection number is full
public static final int SIGNALSTATE_CONNECTTIMEOUT = 82 ;   //!< Connect to remote talking timeout.
public static final int SIGNALSTATE_RINGTIMEOUT = 83 ;      //!< Remote ringing timeout.
public static final int SIGNALSTATE_AUTHTIMEOUT = 84 ;      //!< Remote authemetic timeout.
public static final int SIGNALSTATE_STARTTALKING = 85 ;     //!< Start open sound card and endcode and decode media failed.
public static final int SIGNALSTATE_QUERYRULEFAIL = 86 ;    //!< Query number rule failed.
public static final int SIGNALSTATE_LDCALLNOPREFIX = 87 ;   //!< long distance call without dial prefix 0
public static final int SIGNALSTATE_LOCALLHASPREFIX = 88 ;  //!< Local PSTN call with dial prefix 0        No. 88

public static final int SIGNALSTATE_PREOPENACCOUNT = 89 ;   //!< waiting for user's confirmation by pressing service button
public static final int SIGNALSTATE_OPENACCOUNT = 90 ; 	    //!< openning an account begin                 No. 90  
public static final int SIGNALSTATE_OPENACCOUNTFAIL = 91 ; 	//!< user miss to push the service button

public static final int SIGNALSTATE_GW_TRANSFER = 92 ;      //!< Box as GW transfer
public static final int SIGNALSTATE_GW_TRANSFER_STOP = 93 ; //!< Box stop as GW transfer
public static final int SIGNALSTATE_TRUST_TRANSFER = 94 ;   //!< Box as Trust transfer
public static final int SIGNALSTATE_TRUST_TRANSFER_STOP = 95 ;//!< Box stop as Trust transfer
public static final int SIGNALSTATE_ASSO_FOWARDOUT = 96 ;   //!< Box as asso foward out
public static final int SIGNALSTATE_ASSO_FOWARDIN = 97 ;    //!< Box as asso foward in

public static final int SIGNALSTATE_CHECK_SIMID_RANDOM_CODE = 98 ; //!< Check SIM id random code:param CHECKSIM_PARAM
public static final int SIGNALSTATE_CHECK_SIMID_RESULT = 99 ;      //!< Check SIM id result

public static final int SIGNALSTATE_PREASSOCIATE = 100 ;           //!< Associate state notify.                 No. 100

public static final int SIGNALSTATE_MAX	= 101 ;
///////////////////////////////////////////////////////////////////////////////////////	
//必须 与SDK中 VoIPCore 工程的 SignalState.h 内的值保持一致   ---Lijs 2013.12.06
///////////////////////////////////////////////////////////////////////////////////////		

	// 一下是并机器状态
	public static final int ASSOEVENT_REQUEST = 0; // !< Association request.
	public static final int ASSOEVENT_RELIEVE = 1; // !< Disassociation request.
	public static final int ASSOEVENT_INFO = 2; // !< Association information.

	public static final int ASSORESULT_NULL = 0;
	public static final int ASSORESULT_REQSUCCESS = 1; // !< Request association
														// success.
	public static final int ASSORESULT_RELSUCCESS = 2; // !< Relieve association
														// success.
	public static final int ASSORESULT_OFFLINE = 3; // !< Associate peer offline
	public static final int ASSORESULT_PEERASSOED = 4; // !< Peer has already be
														// associated.
	public static final int ASSORESULT_NUMERROR = 5; // !<
														// Association/Disassociation
														// number error.
	public static final int ASSORESULT_REQREFUSE = 6; // !< Associate requestion
														// be refused.
	public static final int ASSORESULT_RINGTIMEOUT = 7; // !< Associate peer
														// ringing timeout
	public static final int ASSORESULT_OTHERTIMEOUT = 8; // !< Associate other
															// timeout

	public static final int ASSORESULT_REQFAILED = 9; // !< Request association
														// failed.
	public static final int ASSORESULT_SELFASSOED = 10; // !< Self has already
														// be associated.
	public static final int ASSORESULT_NOASSOED = 11; // !< Self is no
														// associated yet.
	public static final int ASSORESULT_OTHERFAIL = 12; // !< Associate other
														// fail reason

	public static final int APPCHECKVERSION = 0; // wangjian 检查APK版本号码，升级

}
