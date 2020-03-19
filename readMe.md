You can download to install demo APK above to test with QPOS mpos directly    
For more details, please visit our knowledge base : [dspread.gitlab.io/qpos](dspread.gitlab.io/qpos)

 
----

# QPOS Programming Guide

- TOC
{:toc}


# Change List

Version | Author        | Date       | Description
--------|---------------|------------|----------------
0.1     | Austin Wang   | 2016-05-01 | Initially Added
1.0     | Austin Wang   | 2016-09-01 | Added EMV related function
1.1     | Ausitn Wang   | 2017-03-01 | Merge QPOS standard and EMV Card reader together
1.2     | Austin Wang   | 2017-10-20 | Added UART interface support for GES device

# Introduction

QPOS is a serial of mobile payment devices. It can communicate with the mobile device through audio jack, UART or USB cable. 

QPOS standard, QPOS mini, QPOS Plus, EMV06, EMV08, GEA and GES are all QPOS products, some of them are with PINPAD embedded and some of them are only card readers without PINPAD.

This document aims to help readers for using the Android SDK of QPOS.

# Programming Model

All methods the SDK provided can be devided into three types:
1. Init methods；
2. Interactive methods；
3. Listener methods.

The application use the init method to init the EMV card reader hardware and get an instance of the Card Reader. It then can use the interactive methods to start the communication with the card reader. During the communication process, if any message returned from the Card reader, a listener method will be invoked by the SDK package.

To avoid the application block and improve the speed of  data interaction between the smart terminal and QPOS, the SDK framework is designed to work under asynchronous mode.

# Programming Interface

## Initialization

The Class named ‘QPOSService’ is the core of SDK library. Before the APP create this core instance with the parameter of “CommunicationMode mode”, the APP must register all the sub-functions in ‘QPOSServiceListener’. Below code snipplet shows how to init the SDK.

```java
	private void open(CommunicationMode mode) {
		listener = new MyPosListener();
		pos = QPOSService.getInstance(mode);
		if (pos == null) {
			statusEditText.setText("CommunicationMode unknow");
			return;
		}
		pos.setConext(getApplicationContext());
		Handler handler = new Handler(Looper.myLooper());
		pos.initListener(handler, listener);
	}
```

The CommunicaitonMode can be 

```java
	public static enum CommunicationMode{
		AUDIO,
		BLUETOOTH_VER2,
		UART,
        USB
	}
```
The app should choose appropriate communication mode depend on it's hardware configuration.
Note, in the example above the app should realize the call back methods of MyPosListener.

The code below shows how to open the communication bridge with the open() method descripted above.
```java
		if (//we want to use Audio Jack as communication mode) {
			open(CommunicationMode.AUDIO);
			posType = POS_TYPE.AUDIO;
			pos.openAudio();
		} else if (//we want to use UART as communication mode) {
			if (isUsb) {
				open(CommunicationMode.USB);
				posType = POS_TYPE.UART;
				pos.openUsb();
			}else {
				open(CommunicationMode.UART);
				posType = POS_TYPE.UART;
				pos.openUart();
			}
			
		} else {   //We will use Bluetooth
			open(CommunicationMode.BLUETOOTH_VER2);
			posType = POS_TYPE.BLUETOOTH;
            //...
		}
```

## Get Device Information

The app can get the EMV cardreader information by issuing:

```java
		pos.getQposInfo();
```
Note the pos is the instance of QPOSService, the app get it during the initialization process.

The device information will be returned on the below call back:
```java
		@Override
		public void onQposInfoResult(Hashtable<String, String> posInfoData) {
			String isSupportedTrack1 = posInfoData.get("isSupportedTrack1") == null ? ""
					: posInfoData.get("isSupportedTrack1");
			String isSupportedTrack2 = posInfoData.get("isSupportedTrack2") == null ? ""
					: posInfoData.get("isSupportedTrack2");
			String isSupportedTrack3 = posInfoData.get("isSupportedTrack3") == null ? ""
					: posInfoData.get("isSupportedTrack3");
			String bootloaderVersion = posInfoData.get("bootloaderVersion") == null ? ""
					: posInfoData.get("bootloaderVersion");
			String firmwareVersion = posInfoData.get("firmwareVersion") == null ? ""
					: posInfoData.get("firmwareVersion");
			String isUsbConnected = posInfoData.get("isUsbConnected") == null ? ""
					: posInfoData.get("isUsbConnected");
			String isCharging = posInfoData.get("isCharging") == null ? ""
					: posInfoData.get("isCharging");
			String batteryLevel = posInfoData.get("batteryLevel") == null ? ""
					: posInfoData.get("batteryLevel");
			String hardwareVersion = posInfoData.get("hardwareVersion") == null ? ""
					: posInfoData.get("hardwareVersion");
		}

```
App can knows the hardware , firmware version and hardware configuration based on the returned information.


## Get Device ID

The device ID is use to indentifying one paticular EMV card reader. The app use below method to get the device ID:

```java
		pos.getQposId();
```

The Device ID is returned to the app by below call back.

```java
		@Override
		public void onQposIdResult(Hashtable<String, String> posIdTable) {
			String posId = posIdTable.get("posId") == null ? "" : posIdTable
					.get("posId");
		}

```


## Start Transaction

The app can start a magnatic swipe card transaction, or an EMV chip card transaction, by below method:
```java
		pos.doTrade(60);
```
The only paramter is the time out value in second. If the user is using magnatic swipe card, after timeout seconds, the transaction will be timed out.

## Set Transaction Amount

The transaction amount can be set by:

```java
			pos.setAmount(amount, cashbackAmount, "156",
									TransactionType.GOODS);
```

the setAmount method can be called before start a transaction. If it was not called, a call back will be invoked by the SDK, giving app another chance to enter the transaction amount.

```java
		@Override
		public void onRequestSetAmount() {
			pos.setAmount(amount, cashbackAmount, "156",
									TransactionType.GOODS);
        }
```

The setAmount method has below parameters: 
1. amount : how much money in cents
2. cashbackAmount : reserved for future use 
3. currency code : US Dollar,  CNY, etc
4. transactionType : which kind of transaction to be started. The transaction type can be:

```java

	public static enum TransactionType {
		GOODS, 
		SERVICES, 
		CASH,
		CASHBACK, 
		INQUIRY, 
		TRANSFER, 
		ADMIN,
		CASHDEPOSIT,
		PAYMENT
	}
```
Transaction type is used mainly by the EMV Chip card transaction, for magnetic card, app can always use GOODS.

## Magstripe Card Transaction

Magstripe card transaction is pretty simple. 
After the app start a transaction, if the user use a magnatic card, below callback will be called feeding the app magnatic card related information. The app then use the information returned for further processing.

```java
		@Override
		public void onDoTradeResult(DoTradeResult result,
				Hashtable<String, String> decodeData) {
			if (result == DoTradeResult.NONE) {
				statusEditText.setText(getString(R.string.no_card_detected));
			} else if (result == DoTradeResult.ICC) {
				statusEditText.setText(getString(R.string.icc_card_inserted));
				TRACE.d("EMV ICC Start");
				pos.doEmvApp(EmvOption.START);
			} else if (result == DoTradeResult.NOT_ICC) {
				statusEditText.setText(getString(R.string.card_inserted));
			} else if (result == DoTradeResult.BAD_SWIPE) {
				statusEditText.setText(getString(R.string.bad_swipe));
			} else if (result == DoTradeResult.MCR) {
                String maskedPAN = decodeData.get("maskedPAN");
                String expiryDate = decodeData.get("expiryDate");
                String cardHolderName = decodeData.get("cardholderName");
                String ksn = decodeData.get("ksn");
                String serviceCode = decodeData.get("serviceCode");
                String track1Length = decodeData.get("track1Length");
                String track2Length = decodeData.get("track2Length");
                String track3Length = decodeData.get("track3Length");
                String encTracks = decodeData.get("encTracks");
                String encTrack1 = decodeData.get("encTrack1");
                String encTrack2 = decodeData.get("encTrack2");
                String encTrack3 = decodeData.get("encTrack3");
                String partialTrack = decodeData.get("partialTrack");
                String pinKsn = decodeData.get("pinKsn");
                String trackksn = decodeData.get("trackksn");
                String pinBlock = decodeData.get("pinBlock");
                String encPAN = decodeData.get("encPAN");
                String trackRandomNumber = decodeData
                        .get("trackRandomNumber");
                String pinRandomNumber = decodeData.get("pinRandomNumber");
							+ "\n";
				}
			} else if (result == DoTradeResult.NO_RESPONSE) {
				statusEditText.setText(getString(R.string.card_no_response));
			} else if (result == DoTradeResult.NO_UPDATE_WORK_KEY) {
				statusEditText.setText("not update work key");
			}
		}
```

Below table describes the meaning of each data element SDK returned:

Key         | Description
------------|------------------
maskedPAN	| Masked card number showing at most the first 6 and last 4 digits with in-between digits masked by “X”
expiryDate	| 4-digit in the form of YYMM in the track data
cardHolderName|	The cardholder name as seen on the card. This can be up to 26 characters.
serviceCode	  | 3-digit service code in the track data
track1Length  |	Length of Track 1 data
track2Length  |	Length of Track 2 data
track3Length  |	Length of Track 3 data
encTracks	  | Reserved
encTrack1	  | Encrypted track 1 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK
encTrack2	  | Encrypted track 2 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK
encTrack3	  | Encrypted track 3 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK 
partialTrack  |	Reserved
trackksn	  | KSN of the track data

The track data returned in the hashtable is encrytped. It can be encrypted by Dukpt Data Key Variant 3DES CBC mode, or by Dukpt Data Key 3DES CBC mode. Per ANSI X9.24 2009 version request, The later (Data Key with 3DES CBC mode) is usually a recommanded choice.

### Decoding Track Data Encrypted with Data Key Variant

Below is an example of the data captured during a live magnatic transaction, the track data is encrypted using data key variant, in 3DES CBC mode:

```
01-20 06:58:29.412: D/POS_SDK(3609): decodeData: {track3Length=0, track2Length=32, expiryDate=1011, encTrack3=, encPAN=, encTrack1=744B8A95FF1982CD63FB24D581FCD1A0590E7F6DD12B86ED1B1D26E687EA853A128598C16BE14964A34607452511C4B6CBDCACD72BEB566E32094937C18C2424, pinRandomNumber=, encTrack2=5E7E2D56D3496B2721EBD4C590031EB9D7883B75B97A71FF, trackRandomNumber=, trackksn=00000332100300E0000A, maskedPAN=622526XXXXXX5453, cardholderName=MR.ZHOU CHENG HAO         , partialTrack=, encTracks=5E7E2D56D3496B2721EBD4C590031EB9D7883B75B97A71FF, psamNo=, formatID=30, track1Length=68, pinKsn=, serviceCode=106, ksn=, pinBlock=}
01-20 06:58:29.413: D/POS_SDK(3609): swipe card:Card Swiped:Format ID: 30
01-20 06:58:29.413: D/POS_SDK(3609): Masked PAN: 622526XXXXXX5453
01-20 06:58:29.413: D/POS_SDK(3609): Expiry Date: 1011
01-20 06:58:29.413: D/POS_SDK(3609): Cardholder Name: MR.ZHOU CHENG HAO         
01-20 06:58:29.413: D/POS_SDK(3609): KSN: 
01-20 06:58:29.413: D/POS_SDK(3609): pinKsn: 
01-20 06:58:29.413: D/POS_SDK(3609): trackksn: 00000332100300E0000A
01-20 06:58:29.413: D/POS_SDK(3609): Service Code: 106
01-20 06:58:29.413: D/POS_SDK(3609): Track 1 Length: 68
01-20 06:58:29.413: D/POS_SDK(3609): Track 2 Length: 32
01-20 06:58:29.413: D/POS_SDK(3609): Track 3 Length: 0
01-20 06:58:29.413: D/POS_SDK(3609): Encrypted Tracks: 5E7E2D56D3496B2721EBD4C590031EB9D7883B75B97A71FF
01-20 06:58:29.413: D/POS_SDK(3609): Encrypted Track 1: 744B8A95FF1982CD63FB24D581FCD1A0590E7F6DD12B86ED1B1D26E687EA853A128598C16BE14964A34607452511C4B6CBDCACD72BEB566E32094937C18C2424
01-20 06:58:29.413: D/POS_SDK(3609): Encrypted Track 2: 5E7E2D56D3496B2721EBD4C590031EB9D7883B75B97A71FF
01-20 06:58:29.413: D/POS_SDK(3609): Encrypted Track 3: 
01-20 06:58:29.413: D/POS_SDK(3609): Partial Track: 
01-20 06:58:29.413: D/POS_SDK(3609): pinBlock: 
01-20 06:58:29.413: D/POS_SDK(3609): encPAN: 
01-20 06:58:29.413: D/POS_SDK(3609): trackRandomNumber: 
01-20 06:58:29.413: D/POS_SDK(3609): pinRandomNumber: 
```

The track ksn 00000332100300E0000A can be used to decode the track data:

Track 1 data:
744B8A95FF1982CD63FB24D581FCD1A0590E7F6DD12B86ED1B1D26E687EA853A128598C16BE14964A34607452511C4B6CBDCACD72BEB566E32094937C18C2424

Track 2 data:
5E7E2D56D3496B2721EBD4C590031EB9D7883B75B97A71FF

Below python script demostrate how to decode track data encrypted with DataKey Variant in CBC mode:
```python
def GetDataKeyVariant(ksn, ipek):
    key = GetDUKPTKey(ksn, ipek)
    key = bytearray(key)
    key[5] ^= 0xFF
    key[13] ^= 0xFF
    return str(key)

def TDES_Dec(data, key):
    t = triple_des(key, CBC, padmode=None)
    res = t.decrypt(data)
    return res

def decrypt_card_info(ksn, data):
    BDK = unhexlify("0123456789ABCDEFFEDCBA9876543210")
    ksn = unhexlify(ksn)
    data = unhexlify(data)
    IPEK = GenerateIPEK(ksn, BDK)
    DATA_KEY_VAR = GetDataKeyVariant(ksn, IPEK)
    print hexlify(DATA_KEY_VAR)
    res = TDES_Dec(data, DATA_KEY_VAR)
    return hexlify(res)
```


Using data key variant to decrypt track 1, will get:

```
16259249 54964104 16598554 553FADC8 EEA8BF50 23A25BA7 02886F00 00000000 0003E450 45145059 15D44964 10653590 41041041 F0000000 00000000 00000000
```
Each character in Track 1 is 6 bits in length, 4 characters are packed into 3 bytes. Each character is mapped from 0x20 to 0x5F. So to get the real ASCII value of each charactor, you need to add 0x20 to each decoded 6 bits.

```
For example, the leading 3 bytes of above track 1 data is 16,25,92

Which in binary is: 00010110 00100101 10010010
Unpacked them to 4 bytes: 000101 100010 010110 010010
Which in binary is:05221612
Add 0x20 to each byte:25423632
Which is in ASCII :%B62
```

Using data key variant to decrypt track 2, will get:
```
62252600 06685453 D1011106 17426936 FFFFFFFF FFFFFFFF 
```

Each character in Track 2 & Track 3 is 4 bits in length. 2 characters are packed into 1 byte and padded with zero before encryption

### Decoding Track Data Encrypted with Data Key

Below is another example, the track data is encrypted using data key whith 3DES CBC mode (per ANSI X9.24 2009 version request)

```
01-21 04:46:26.764: D/POS_SDK(30241): decodeData: {track3Length=0, track2Length=32, expiryDate=1011, encTrack3=, encPAN=, encTrack1=22FB2E931F3EFAFC8C3899AB779F3719E75D392365DB748EEA789560EEB7714D84AB7FFA5B2E162C9BD566D03DCD240FC9D316CAC4015B782294365F9062CA0A, pinRandomNumber=, encTrack2=153CEE49576C0B709515946D991CB48368FEA0375837ECA6, trackRandomNumber=, trackksn=00000332100300E00002, maskedPAN=622526XXXXXX5453, cardholderName=MR.ZHOU CHENG HAO         , partialTrack=, encTracks=153CEE49576C0B709515946D991CB48368FEA0375837ECA6, psamNo=, formatID=30, track1Length=68, pinKsn=, serviceCode=106, ksn=, pinBlock=}
01-21 04:46:26.766: D/POS_SDK(30241): swipe card:Card Swiped:Format ID: 30
01-21 04:46:26.766: D/POS_SDK(30241): Masked PAN: 622526XXXXXX5453
01-21 04:46:26.766: D/POS_SDK(30241): Expiry Date: 1011
01-21 04:46:26.766: D/POS_SDK(30241): Cardholder Name: MR.ZHOU CHENG HAO         
01-21 04:46:26.766: D/POS_SDK(30241): KSN: 
01-21 04:46:26.766: D/POS_SDK(30241): pinKsn: 
01-21 04:46:26.766: D/POS_SDK(30241): trackksn: 00000332100300E00002
01-21 04:46:26.766: D/POS_SDK(30241): Service Code: 106
01-21 04:46:26.766: D/POS_SDK(30241): Track 1 Length: 68
01-21 04:46:26.766: D/POS_SDK(30241): Track 2 Length: 32
01-21 04:46:26.766: D/POS_SDK(30241): Track 3 Length: 0
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Tracks: 153CEE49576C0B709515946D991CB48368FEA0375837ECA6
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 1: 22FB2E931F3EFAFC8C3899AB779F3719E75D392365DB748EEA789560EEB7714D84AB7FFA5B2E162C9BD566D03DCD240FC9D316CAC4015B782294365F9062CA0A
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 2: 153CEE49576C0B709515946D991CB48368FEA0375837ECA6
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 3: 
01-21 04:46:26.766: D/POS_SDK(30241): Partial Track: 
01-21 04:46:26.766: D/POS_SDK(30241): pinBlock: 
01-21 04:46:26.766: D/POS_SDK(30241): encPAN: 
01-21 04:46:26.766: D/POS_SDK(30241): trackRandomNumber: 
01-21 04:46:26.766: D/POS_SDK(30241): pinRandomNumber: 
```

Below python script demostrate how to decode track data encrypted with DataKey in CBC mode:

```python
def GetDataKeyVariant(ksn, ipek):
    key = GetDUKPTKey(ksn, ipek)
    key = bytearray(key)
    key[5] ^= 0xFF
    key[13] ^= 0xFF
    return str(key) 

def GetDataKey(ksn, ipek):
    key = GetDataKeyVariant(ksn, ipek)
    return str(TDES_Enc(key,key))

def TDES_Dec(data, key):
    t = triple_des(key, CBC, "\0\0\0\0\0\0\0\0",padmode=None)
    res = t.decrypt(data)
    return res

def decrypt_card_info(ksn, data):
    BDK = unhexlify("0123456789ABCDEFFEDCBA9876543210")
    ksn = unhexlify(ksn)
    data = unhexlify(data)
    IPEK = GenerateIPEK(ksn, BDK)
    DATA_KEY = GetDataKey(ksn, IPEK)
    print hexlify(DATA_KEY)
    res = TDES_Dec(data, DATA_KEY)
    return hexlify(res)

```
The decoded track 1 and track 2 data are the same as the track data we got in previous section.

## Decoding PIN 

The QPOS will also send the encryted PIN to the mobile application:
```
10-07 11:37:49.571: V/vahid(20753): ???? ????? ??:Format ID: 30
10-07 11:37:49.571: V/vahid(20753): Masked PAN: 622106XXXXXX1111
10-07 11:37:49.571: V/vahid(20753): Expiry Date: 1605
10-07 11:37:49.571: V/vahid(20753): Cardholder Name:
10-07 11:37:49.571: V/vahid(20753): KSN:
10-07 11:37:49.571: V/vahid(20753): pinKsn: 00000332100300E000E6
10-07 11:37:49.571: V/vahid(20753): trackksn: 00000332100300E000C6
10-07 11:37:49.571: V/vahid(20753): Service Code: 100
10-07 11:37:49.571: V/vahid(20753): Track 1 Length: 0
10-07 11:37:49.571: V/vahid(20753): Track 2 Length: 37
10-07 11:37:49.571: V/vahid(20753): Track 3 Length: 37
10-07 11:37:49.571: V/vahid(20753): Encrypted Tracks: 47B35616888BB17A055BE87FBAC76DCDD3EFFACA5F1C901047B35616888BB17A055BE87FBAC76DCDD3EFFACA5F1C901060325F039768CE5760325F039768CE5760325F039768CE5760325F039768CE57
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 1:
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 2: 47B35616888BB17A055BE87FBAC76DCDD3EFFACA5F1C9010
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 3: 47B35616888BB17A055BE87FBAC76DCDD3EFFACA5F1C901060325F039768CE5760325F039768CE5760325F039768CE5760325F039768CE57
10-07 11:37:49.571: V/vahid(20753): Partial Track:
10-07 11:37:49.571: V/vahid(20753): pinBlock: 377D28B8C7EF080A
10-07 11:37:49.571: V/vahid(20753): encPAN:
10-07 11:37:49.571: V/vahid(20753): trackRandomNumber:
10-07 11:37:49.571: V/vahid(20753): pinRandomNumber:
```

Decode the Track 2 data using the method descripted before: 
6221061055111111D16051007832281716058FFFFFFFFFFF

Below python script demostrate how to decode PINBLOCK:

```python
def GetPINKeyVariant(ksn, ipek):
    key = GetDUKPTKey(ksn, ipek)
    key = bytearray(key)
    key[7] ^= 0xFF
    key[15] ^= 0xFF
    return str(key)

def TDES_Dec(data, key):
    t = triple_des(key, CBC, padmode=None)
    res = t.decrypt(data)
    return res

def decrypt_pinblock(ksn, data):
    BDK = unhexlify("0123456789ABCDEFFEDCBA9876543210")
    ksn = unhexlify(ksn)
    data = unhexlify(data)
    IPEK = GenerateIPEK(ksn, BDK)
    PIN_KEY = GetPINKeyVariant(ksn, IPEK)
    print hexlify(PIN_KEY)
    res = TDES_Dec(data, PIN_KEY)
    return hexlify(res)

if __name__ == "__main__":
    KSN = "00000332100300E000E6"
    DATA = "377D28B8C7EF080A"
    #DATA="153CEE49576C0B709515946D991CB48368FEA0375837ECA6"
    print decrypt_pinblock(KSN, DATA)

```

The decrypted PINBLOCK (formated Pin data) is: 0411019efaaeeeee
The real PIN value can be caculated using formated pin data and PAN as inputs, according to ANSI X9.8. Below is an example:

1) PAN: 6221061055111111
2) 12 right most PAN digits without checksum: 106105511111
3) Add 0000 to the left: 0000106105511111
4) XOR (#3) and Formated PIN Data 

XOR (0000106105511111, 0411019efaaeeeee) = 041111FFFFFFFFFF
In our example, the plain PIN is 4 bytes in length with data "1111"


## Chip Card Transaction

EMV Chip card transaction is much more complicate than magnatic swipe card transaction. The EMV kernel inside the device may need a lot of information to process the transaction, including:

1. PIN from the card holder
2. Current time from the application
3. Preferred EMV application from card holder
4. The process result from the bank (card issuer) for the transaction

### Start Chip Card Transaction

The app start the EMV transaction by calling
```java
	pos.doEmvApp(EmvOption.START);
```
This is usually happens inside the call back of onDoTradeResult(), as below demo code shows:

```java
		@Override
		public void onDoTradeResult(DoTradeResult result,
				Hashtable<String, String> decodeData) {
			if (result == DoTradeResult.NONE) {
				statusEditText.setText(getString(R.string.no_card_detected));
			} else if (result == DoTradeResult.ICC) {
				statusEditText.setText(getString(R.string.icc_card_inserted));
				TRACE.d("EMV ICC Start")
				pos.doEmvApp(EmvOption.START);
			} else if (result == DoTradeResult.NOT_ICC) {
				statusEditText.setText(getString(R.string.card_inserted));
			} else if (result == DoTradeResult.BAD_SWIPE) {
				statusEditText.setText(getString(R.string.bad_swipe));
			} else if (result == DoTradeResult.MCR) {
                //handling MSR transaction
            }
```

### Input PIN 

The PIN information can be sent to the EMV kernel by:
```java
		@Override
		public void onRequestSetPin() {
				pos.sendPin("123456");
				//pos.emptyPin();    //Bypass PIN Entry
				//pos.cancelPin();   //Cancel the transaction
		}
```
Note, the kernel will not call the callback if PIN is not required for the transaction, or if the QPOS itself is with an embedded PINPAD.

If the user do not want to input PIN, the applicaiton can bypass PIN enter by calling 

```java
	pos.emptyPin();
```
if the user want to cancel the transaction, the app should call
```java
	pos.cancelPin();
```

### Set Time

The current time information can be sent to the EMV kernel by:

```java
		@Override
		public void onRequestTime() {
			String terminalTime = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(Calendar.getInstance().getTime());
			pos.sendTime(terminalTime);
		}
```

### Select EMV Application

If there is multiple EMV applications inside one Chip card, the SDK will ask the user to choose one application from a list:

```java
		@Override
		public void onRequestSelectEmvApp(ArrayList<String> appList) {
				pos.selectEmvApp(position);   //position is the index of the chosen application
				//pos.cancelSelectEmvApp();   //Cancel the transaction
		}

```

The chosen application is sending to the EMV kernel by 
```java
		pos.selectEmvApp(position)
```

### Online Request

If the EMV kernel found the transaction need to go online, below call back will be called.


```java
		@Override
		public void onRequestOnlineProcess(String tlv) {
             //sending online message tlv data to issuer
             ....
             //send the received online processing result to POS
             pos.sendOnlineProcessResult("8A023030");
        }
```

Below is an exmple of tlv data received by onRequestOnlineProcess:
```
2014-08-27 17:52:21.210 qpos-ios-demo[391:60b] alertView.title = Online process requested.
2014-08-27 17:52:21.211 qpos-ios-demo[391:60b] hideAlertView
2014-08-27 17:52:21.221 qpos-ios-demo[391:60b] onRequestOnlineProcess = {
    tlv = 5F200220204F08A0000003330101015F24032312319F160F4243544553542031323334353637389F21031752139A031408279F02060000000000019F03060000000000009F34034203009F120A50424F43204445424954C409623061FFFFFFFF5284C10A00000332100300E00003C708A68701E68CB34BDEC00A00000332100300E00003C2820150E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF;
}
```

The tlv data can be decoded using the online EMVlab tool:

http://www.emvlab.org/tlvutils/?data=5F200220204F08A0000003330101015F24032312319F160F4243544553542031323334353637389F21031752139A031408279F02060000000000019F03060000000000009F34034203009F120A50424F43204445424954C409623061FFFFFFFF5284C10A00000332100300E00003C708A68701E68CB34BDEC00A00000332100300E00003C2820150E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF%0D%0A

As we can see from the decoded table:

Tag  | Tag Name            | Value
-----|---------------------|------
5F20 | Cardholder Name     |
4F   | AID                 | A000000333010101
5F24 | App Expiration Date | 231231
9F16 | Merchant ID         | B C T E S T 1 2 3 4 5 6 7 8
9F21 | Transaction Time    | 175213
...  | ...                 | ...
C4   | Masked PAN          | 623061FFFFFFFF5284
C1   | KSN(PIN)            | 00000332100300E00003
C7   | PINBLOCK            | A68701E68CB34BDE
C0   | KSN Online Msg      | 00000332100300E00003
C2   | Online Message      | E84B5D0D2AA9F40A2EFC....

Inside the table, there are:
1. Some EMV TAGs (5F20,4F,5F24 ...) with plain text value. 
2. Some Proprietary tags starting with 0xC, in our case C4,C1,C7,C0 and C2.

The defination of proprietary tags can be found below:

Tag   | Name                      | Length(Bytes)
------|---------------------------|--------------
C0    | KSN of Online Msg         | 10
C1    | KSN of PIN                | 10
C2    | Online Message(E)         | var
C3    | KSN of Batch/Reversal Data| 10
C4    | Masked PAN                | 0~10
C5    | Batch Data                | var
C6    | Reversal Data             | var
C7    | PINBLOCK                  | 8

It's the responsibility of the app to handle the online message string, sending them to the bank( the cardd issuer), and check the bank processing result.

The value of tag C2 is the encrypted Online Message, usually the app need to send it to the back end system, along with the tag C0 value. The backend system can derive the 3DES key from C0 value, and decrypt the C2 value and get the real online data in plain text format.

In case encrypted PIN is needed by the transaction, the app can also send the value of tag C7,C1 to back end system.

The example above is just a demostration. "8A023030" is a fake result from back end system.

As an exmple of decoding the online message, please find below some demo scripts:

```python

def decrypt_icc_info(ksn, data):
    BDK = unhexlify("0123456789ABCDEFFEDCBA9876543210")
    ksn = unhexlify(ksn)
    data = unhexlify(data)
    IPEK = GenerateIPEK(ksn, BDK)
    DATA_KEY = GetDataKey(ksn, IPEK)
    print hexlify(DATA_KEY)
    res = TDES_Dec(data, DATA_KEY)
    return hexlify(res)

if __name__ == "__main__":
    KSN = "00000332100300E00003"
    DATA = "E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF"
    print decrypt_icc_info(KSN, DATA)

```

The decoded icc online message looks like:

http://www.emvlab.org/tlvutils/?data=708201479f02060000000000015a096230615710101752845713623061571010175284d231222086038214069f9f101307010103a02000010a01000000000013f6c0429f160f4243544553542031323334353637389f4e0f61626364000000000000000000000082027c008e0e000000000000000042031e031f005f24032312315f25031307304f08a0000003330101019f0702ff009f0d05d8609ca8009f0e0500100000009f0f05d8689cf8009f2608059aae950d0b7a679f2701809f3602008d9c01009f3303e0f8c89f34034203009f3704c1cdd24a9f3901059f4005f000f0a0019505088004e0009b02e8008408a0000003330101019a031408275f2a0201565f3401019f03060000000000009f0902008c9f1a0206439f1e0838333230314943439f3501229f4104000000015f200220205f300202205f28020156500a50424f432044454249540000000000

All the online message in embedded inside tag 0x70, the ending 00 are paddings for 3DES encryption.

### Get Transaction Result 

The application will be notified by the SDK regarding the transaction result by:

```java
		@Override
		public void onRequestTransactionResult(
			TransactionResult transactionResult) {
        	if (transactionResult == TransactionResult.APPROVED) {
            } else if (transactionResult == TransactionResult.TERMINATED) {
			} else if (transactionResult == TransactionResult.DECLINED) {
			} else if (transactionResult == TransactionResult.CANCEL) {
			} else if (transactionResult == TransactionResult.CAPK_FAIL) {
			} else if (transactionResult == TransactionResult.NOT_ICC) {
			} else if (transactionResult == TransactionResult.SELECT_APP_FAIL) {
			} else if (transactionResult == TransactionResult.DEVICE_ERROR) {
			} else if (transactionResult == TransactionResult.CARD_NOT_SUPPORTED) {
			} else if (transactionResult == TransactionResult.MISSING_MANDATORY_DATA) {
			} else if (transactionResult == TransactionResult.CARD_BLOCKED_OR_NO_EMV_APPS) {
			} else if (transactionResult == TransactionResult.INVALID_ICC_DATA) {
			}        
        }
      }
```

### Batch Data Handling

When the transaction is finished. The batch data will be returned to the application by below callback.

```java
		@Override
		public void onRequestBatchData(String tlv) {
		}
```
Note, if there is issuer's script result inside the tlv, the mobile app need to feedback it to the bank.
Decoding the tlv inside onRequestBatchData is similar to decoding onRequestOnlineProcess. 

### Reversal Handling

If the EMV chip card refuse the transaction, but the transaction was approved by the issuer. A reversal procedure should be initiated by the mobile app. The requred data for doing reversal can be got by below call back:

```java
		@Override
		public void onReturnReversalData(String tlv) {
			...
		}
```

## Error Notification

During the transaction, if there is anything abnormal happened, the onError callback will be called.

```java
		@Override
		public void onError(Error errorState) {
			if (errorState == Error.CMD_NOT_AVAILABLE) {
			} else if (errorState == Error.TIMEOUT) {
			} else if (errorState == Error.DEVICE_RESET) {
			} else if (errorState == Error.UNKNOWN) {
			} else if (errorState == Error.DEVICE_BUSY) {
			} else if (errorState == Error.INPUT_OUT_OF_RANGE) {
			} else if (errorState == Error.INPUT_INVALID_FORMAT) {
			} else if (errorState == Error.INPUT_ZERO_VALUES) {
			} else if (errorState == Error.INPUT_INVALID) {
			} else if (errorState == Error.CASHBACK_NOT_SUPPORTED) {
			} else if (errorState == Error.CRC_ERROR) {
			} else if (errorState == Error.COMM_ERROR) {
			} else if (errorState == Error.MAC_ERROR) {
			} else if (errorState == Error.CMD_TIMEOUT) {
			}
		}

```

## Mifare Card Operation

Blow introduce how to transmit datas on the different mifare cards and pos.There are three typr mifare card - Mifare Classic, Mifare Ultralight, Mifare Desfire.

**1.Mifare Classic**
![avatar][mifareClassicId] 
As the above image, we can know the work flow for the Mifare Classic.
1).poll on Card
```java
	pos.pollOnMifareCard(int timeout)
```
2).Verify Key A/B
```java
	pos.authenticateMifareCard(MifareCardType cardType,String keyType,String block,String keyValue,int timeout)
```
3).Operate Card
   - Add/Reduce/Restore
```java
	pos.operateMifareCardData(MifareCardOperationType type,String block,String data,int timeout)
```
- Read
```java
	pos.readMifareCard(MifareCardType cardType,String block,int timeout)
```
- Write
```java
	pos.writeMifareCard(MifareCardType cardType,String block,String data,int timeout)
```
4)Finish
```java
	pos.finishMifareCard(int timeout)
```

**2.Mifare Ultralight**
  	The Ultralight card most operate is same with the classic card, except some part is different.
	 1).It don't need to use key A/B to verify, just verify the data.
 	2).It don't have the Add/Reduce/Restore operation,but can read and write data.
 	3).It have a special method to read data.
```java
	pos.faseReadMifareCardData(String startBlock,String endBlock,int timeout)
```
**3.Mifare Desfire**
	Desfire card is different from the above two cards. It has easy method to transfer data.
	1).Power on card
```java
	pos.powerOnNFC(int isEncrypt, int timeout)
```
	2).Send apdu data
```java
	pos.sendApduByNFC(String apduString, int timeout)
```
	3).Power off card
```java
	pos.powerOffNFC(int timeout)
```




[mifareClassicId]:data:image/png;base64,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wz4KhzdMwARMwARMoJ4E7LDr2S+2ygRMwARMwASOImCHfRQOb5iACZiACZhAPQnYYdezX2yVCZiACZiACRxFwA77KBzeMAETMAETMIF6ErDDrme/2CoTMAETMAETOIqAHfZROLxhAiZgAiZgAvUkYIddz36xVSZgAiZgAiZwFAE77KNweMMETMAETMAE6knADrue/WKrTMAETMAETOAoAnbYR+HwhgmYgAmYgAnUk4Addj37xVaZgAmYgAmYwFEE7LCPwuENEzABEzABE6gnATvsevaLrTIBEzABEzCBowjYYR+FwxsmYAImYAImUE8Cdtj17BdbZQImYAImYAJHEbDDPgqHN0zABEzABEygngTssOvZL7bKBEzABEzABI4iYId9FA5vmIAJmIAJmEA9Cdhh17NfbJUJmIAJmIAJHEXADvsoHN4wARMwARMwgXoSsMOuZ7/YKhMwARMwARM4isCso7ZmuPHOO+/s3LkzLbR48eJ0c3JyMt30XtMIAuWPjdNPP/34448PhUOf2LNnz/r164evmV/4whcWLVo0fO1yi0ygcgLHTL1xeO6HP9Sd3k2bNt19993pRfOZZ55JVZ199tnpZk/3HnPMMe+++66q62e9aQNdbz9p/OVf/uV/+2//LXNQpQYMWZohzpe+9KVVq1YNU7v+9m//Foe9fPnyYWqU22ICPSLQvcNmen3aaaeddNJJNblipg67R7CstlYEcGC33HJLTQ6/PpAZyvZeeeWVExMTdth9OH5cxRAQ6H4N++GHH3711Ve5iOzevXsIQFTYhBdffHHlypUVKrQqEzABEzABE+jSYTO9vv3224VvKNfVyhwZhw8fxmeX0eCyJmACJmACJpAh0KXD1vRaun784x/bP2WwetMETMAETMAEqiXQ5V3if/EXf8HaIXPrP//zP1+6dGl631m19lmbCbQiwI8OMr87aCXpfBMwARMYAgJdOux/82/+DY3fvHnzggULfNEcguPATTABEzABE6g5gS4dtlr1wAMP1Lx5AzGP3wdv2LBhIFW7UhMwARMwgWEl0OUa9rDiqKRdLBDgsytRZSUmYAImYAImIAJ22D4STMAETMAETKABBOywG9BJNrGQAM8AyDxarlDMmSZgAiYwHATssIejH90KEzABEzCBISdQymHzWEEeJz7khGbePH6V7iedzRybS5iACZiACbQj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8mklLTDEBCYnJ6+//vpf/vKXuOohbmZVTVu8ePEzzzxTlbam66nhNeTBBx+69bbb/+GXe5rO1vYPH4FSIXGedGZvPXzHRBct+vCHP/zcc89deOGFKssM+11/WhCwt+7iAHMREzABCJRy2CZoAkHg9NNP/9GPfvTzn/883HbscsIETMAETKA8Aa9hl2doDf9CQG7bsfF/IeKUCZiACVREwDPsikBaTUJg9uzZyZaTJmACJmACFRCww64AolWYgAmYgAmYQK8JlHLYftJZr7vH+k3ABEzABExABEo5bEM0ARMwARMwARPoDwE77P5wdi0mYAImYAImUIqAHXYpfC5sAiZgAiZgAv0hYIfdH86uxQRMwARMwARKESj1O2yedFaqchc2ARMwARMwARPojIBn2J1xspQJmIAJmIAJDJSAHfZA8btyEzABEzABE+iMgB12Z5wsZQImYAImYAIDJWCHPVD8rtwETMAETMAEOiNQymH7SWedQbaUCZiACZiACZQlUMphl63c5U3ABEzABEzABDojYIfdGSdLmYAJmIAJmMBACdhhDxS/KzcBEzABEzCBzgjYYXfGyVImYAImYAImMFACftLZQPE3vPI333zz9ddf/8d//Md/9a/+1WuvvTZr1qyTTjqp4W2y+SZgAiZQUwKeYde0Yxph1qFDh0455ZTPfvazTzzxxLx58/7mb/6mEWbbSBMwARNoIgE77Cb2Wl1snj9//rJly2TN7NmzV61aVRfLbIcJmIAJDB0BO+yh69L+Nuimm24iEk6d/+k//acTTzyxv5W7NhMwARMYIQJ22CPU2b1oqibZ+Ozrr7++F/qt0wRMwARMQARK3XTGk84mJiaWL19umimBdevW/d3f/V2aM9zpt9566yMf+cjVV1893M3MtG7Lli2OKGSYeNMETKCnBEo57J5a1lzlr7zyyuTkZHPt787y//k//2d3BRta6u23326o5TbbBEygoQTssHvVcTt27PBvnHoFd6B6L7vsst27dw/UBFduAiYwigTssHvV63jruXPn9kq79Q6OAPfDD65y12wCJjC6BHzT2ej2vVtuAiZgAibQIAKlZtgPPPBAg5pqU03ABEzABEyguQQ8w25u39lyEzABEzCBESJghz1Cne2mmoAJmIAJNJeAHXZz+86Wm4AJmIAJjBABO+wR6mw31QRMwARMoLkESjlsnnS2adOm5jbelpuACZiACZhAUwiUcthNaeSI23ndii8f+4Hj9Ee6PQ0EppVpryHd+8gjj0bV559/QbprRulqrZpR1RY2ARMwgZoQsMOuSUf0yow77vz2tm1P/+63b+mPdIX+WEZTRaEzpqLPX7E8qka4UKxXLbdeEzABExguApU5bB6tfNddd7344ovDxafxrdk+uX3tmm9GM+7beO/GjffFZu8S+/bto6I9L/8iqnjyySee2rZtx46dkeOECZiACZhA5wQqcNhy1fPmzVu5cuXhw4c7r9uSfSAwb3ze2lu+FRWdddYipryxGfFqZsmRGQmmyBJYcNonIhNJZRLuJpPvm2/+Bp44lSF/6w9+eO6SJePj41GQBFVjgHKQj9rlxflmCq585eRrT7U5bQImYAIjRaDsk86uu+66VatW/a//9b+C2p49ew4dOhSbCxYsSN9COAp7o+11SHzvnu/KBcoYprzhRMm/7bZbv3bjV9mFa5xz8smXXnpJ2ByxdHLw3LhSpsj4UdwzfpfEOUvORZ6//QcOMI9nb5QlsX//fsYKaU6aRuGSJed875735t+kr77mi5qL4/if3vaUnHqYp7quuWa0Xt+Z4nLaBEzABCBQymFT/vnnn//Yxz6WOuwf//jH6dugcedLly4N1qOwNxpbk4R8odweXpCJr1wvUWt5a+zEcz/00MOpw77//gcilv6VlTdQEPnndu+W48zM1GfaUhTGuGHRmWfipkODvDXWhnnk2FsHHydMwARGl8DUG4ffLf35+c9/fuGFFwLxmWeeKa2s8QqWL18OiqmpqRq2ZO/eve8/dvazz+7YsuUREunfeed9GoP/6roV/JFIdylN2dibNm39HXeq7LSZaAiZ1IBTF3ycfKyiIgmwV5naLKw3VPU5sXjx4tr2b59RlK+Ok4VXEpTXU6GGzZsfnH/KqRUqtCoTqIpABWvYXLxOP/30H/3oR7htv1ASGvX5MEkl1s13mBTz2rGxMdIEt+MvE9NmL9Hp2EuCnDlz5oSq9omLPvdZ4ttp1cifd/4FWixnvs6EXsof3LwprwrzMmXzMs4xARMwgZEiUI3DFjLcth12rY4eXCwBcNxkWIW/JJMgM394RLlP9rJEzVpyiJFgjZmlZeXo59SkP3XGGXGTOUMBAtdksvgtsfSbWohj45gjU7/pUhCeqlevXqVd/PQrZCKBeWiQeQhHpSHghAmYgAmMGoFSDttPOqv/4cK8Gder26355u6w+KkVE1zuINMuGsLtaWlz2IyC+FSVwo+y2q0iJNikyMKFn8zfJS6FIUwRcqJqZtXcsyY9zONxyXzS2iUs8xhweA07A8ebJmACI0jgGNaw5374Q921HIc9MTGhJdvuNAxlKY1jWMN2vGEo+/fss8+enJx0/1bSuTW8hjz44EO33nb7P/xyTyUNtBITqJBAqRl2hXZYlQmYgAmYgAmYQBsCdtht4HiXCZiACZiACdSFgB12XXrCdpiACZiACZhAGwKlHpzCDyjbqPYufeA5cAAAIABJREFUEzABEzABEzCBqgh4hl0VSesxARMwARMwgR4SsMPuIVyrNgETMAETMIGqCNhhV0XSekzABEzABEyghwTssHsI16pNwARMwARMoCoCpRy2n3RWVTdYjwmYgAmYgAm0J1DKYbdX7b0mYAImYAImYAJVEbDDroqk9ZiACZiACZhADwmU+h12D+1qvuqtW7eecMIJzW+HW5AlcOjQoWyWt03ABEyg9wTssKtn/JnPfIaXQ9x4443Vq7ZGEzABEzCBUSVQymH7SWeFh82FF144e/bs0ZmH7dmz5/HHH1+16o/vty5kMnyZDp8MX5+6RSZQcwKlHHbN2zZA85YuXTrA2vtcNeGE559/3m9Z7TN2V2cCJjBqBHzT2aj1uNtrAiZgAibQSAJ22I3sNhttAiZgAiYwagTssEetx91eEzABEzCBRhIo5bD9pLNG9rmNNgETMAETaCCBUg67ge21ySZgAiZgAibQSAJ22I3sNhttAiZgAiYwagTssEetx91eEzABEzCBRhKww25kt9loEzABEzCBUSNQ6sEpftLZqB0ubq8JmIAJmMCgCHiGPSjyrtcETMAETMAEZkCg1Ax7BvVY1ARMwATqTeDNN998/cjnnXfeee2112bNmnXSSSfV22RbN1oE7LBHq7/dWhMwgVYEeGHPaaed9k//9O6f/OmsefPm3XDDDRs2bGgl7HwT6D8Bh8T7z9w1moAJ1JHA/Pnzly1bJst44d6ovYCujl1im44mUMph+0lnR8P0lgmYQLMJ3HTTTX/6p39KG6699toTTzyx2Y2x9UNHoJTDHjoablBHBFjh60jOQk0j4J5lkv2Xf/kfZ/3pMZ5eN+3gHQl77bBHopurbeSvf/3rf/tv/+2Pf/zjatVa28AJ/If/8B/Wrl17+PDhgVsyQAOYZF933XWeXg+wC1x1KwJ22K3IOL8dgRdffPHf//t/b7fdjlED9+Gqb7nlFu63GmW3zST7r//6rxvYezZ5+AkcM/XG4bkf/lB3Df3MZz7z3//7f++urEsNDYGFCxf+8pe/HPFp2dD0ZjSEKebnPve5733ve5HTiwT3wUxMTCxfvrwXyq3TBIaMQKkZ9vj4OA87e9efESMwNTWl04BfqfK7l1tvvfX0008fMQbD2dzFixfTs9wgzS+afv7zn/+X//Jfhux65+aYQKMJdO+w+c3i97///dtvv903qjT6COjOeMKGjNXw3FzZjz322O6UuFTdCMhV062Mw7yIW7fesT0m0P2DU9avX//222+/+uqrDz/8sCNaI3UkMbF++eWXeQ7USLV6FBr7ox/9CJ89Ci11G02giQS6nGFreq0Ge5LdxI4vYzOu2t66DMDalrW3rm3X2DATgECXDnvTpk1EzD74wQ+ecMIJhMR/+tOfmqYJmIAJmIAJmEDvCHTpsFevXs1C10UXXXTnnXeS+PSnP907E63ZBEzABPpG4Fe/+tWtt97Wt+pckQl0TqBLh915BZY0ARMwgQYR2L792QcferhBBtvU0SFghz06fe2WmoAJmIAJNJiAHXaDO8+mm4AJmIAJjA4BO+zR6Wu31ARMwARMoMEE7LAb3Hk23QRMwARMYHQI2GGPTl+7pSZgAiZgAg0m4IdVNbjzbLoJmEATCfAcC9560kTLbXNJAryEoIwGO+wy9FzWBEzABLoksGDBAj+wvUt2DSzGK4nLv9LQDruBPW+TTcAEmk9g1apVfgtD87ux0xacffbZk5OTnUq3kPMadgswzjYBEzABEzCBOhGww65Tb9gWEzABEzABE2hBwA67BRhnm4AJmMCgCVy34svHfuC4GVmxb98+ivA9o1KthDGAv1Z7p81fcNonHnnk0WnFWgnccee3aUv6R04r4arySza5KjMK9dhhF2JxpgmYgAkMnsDGjfedu2RJJ16qpGtMm7pjx84YJXzvnu/yl+6dNo21559/gcT2vPyLSy+9ZNoibQRo/u9++1b83XzzN8qMANpU1IhddtiN6CYbaQImMHIE8Ey4q8svX3b//Q+MXONbNPiaa67euWtXi53Dn22HPfx93LsWvvnmm6+99tqhQ4fefvttEr/+9a97V5c1m8CoEVh7y7fw1sxQiW8z61Xz8eJMppWO6DczWtKfv2J5zMW/s+EuRZLTgHbElkOMgqQjHyV8zllyLvrJJJ3Gh6lXktERmosrU1ZhHpPgp7Zt0ybfMSGOWtrUHpqnTWA5tqFTs/nUkmCF/apUMqEzbwm7lCmzJZm2PbCjExkUIq+myQw207JRV7UJO+xqeY6WNlz1Kaecctlll+3evXvevHl/8zd/M1rtd2tNoGcE8Dr4BsWTmVY++thjbap68sknxsfHH9y86Ws3fjXECCMTkSaoLgeGO7nttlsVW04Dy0zflclsHjePnqe3PYUSMkmHNtzSkiXnkIkxpJWPa0dYxbEWT4zB1IIqqo6yJPBnae3hszO1p0XyaaqgOZdcfLF2Te2bomraTgPDEuwhrSafd/4FMEFm7759MW4o5BCZ9228lyryVac5mMFACrU0loZs2/a0CMAnMzJIS1WStsOuBOOIKpk/f/6yZcvU+NmzZ/O70hEF4WabQNUE8NC4RmnFRU3rRTL1f2XlDeTgcfkcPHgQB4abCXeO73zon9/5fdVVf3zm2sTiCVxgRk9s4pbkKWNVG4U4qrPOWiQZnHQIZxLUjhlRO050++R2yUxbO5N1zX01haVs1IjBUvLc7t2wUj7fpMnBPD4a8cRSeiGHNFPFM/bnNxcu/KQyGXCsXfNNpWGOtVSal68qxw67KpIjquemm26aNeu9x+9ce+21fmzTiB4EbnYPCOCh+ZOvUow65ohd1IbPplR4PmbYM1WS90P4YJQoOIxmfFUrnao93cuUN91sk2YcoPmrvuWAM/L79+/P5xw8+JtMJpuFHMhUW/Ly0+aAhZUIgXVIfFpcFhgwgflHJtmeXg+4G1z9cBHAN+NCUkfFrDHmxF20dWxsLKOQSPKM9ORdGhNTHJWCw5jaZoZN7Zm6PpYE2zO7uticM2dOphQ5Y2MfzWSyWciBzPxwJF+2MAcssSig/sqDKizYXeZ7cyN/KiewcuVKnhxbudp6Knzrrbc+8pGPsJJdT/N6ZNWWLVscUegRW6vFN0esWDQItzKBw6/Iu5DAMbDkHKzau0AivRRhwVVxaabF88bntfq9VqGrY4GWKD16GExwNxxBZk1MNeUlkxm2YtRzTj45rFIiUztTUmLyGZkym5864wxiBkTsqYhhBJEJnCh8+GAYFmrRnfZmLAkOSAoOlCiuxQi8ftyfT5MLLQTL1dd8UQv21EXTcNuFkpVk2mFXgvEoJTfeeOP3v/99bpw+KnfYNw4cODDsTTyqfaPWv0c13hu9JIDLwfllZsB4FKaweGi8Du5E0VcWdPEusgVnibfYf+DARZ/7bKF1OBImxAqGo6qVt6bse45ufBzh9MYx5KmUTASUjyNkYKEcTOKPlWkGBKzvYgnCafG0drx1rGcXmjrTTNyw7jVTQdLkkP7Jk09gBsbQnDAmtSQ4sFdwkJS3pjhG4rDVQFAX+myw6C5xVR21aLPy72Om3jg898Mf6k4vb4ibmJjw8+sz9MDC6/N27Nhx0kknZXZ5cwgI6K74qampuXPnDkFz2jThmGOOKfk2wDbKtauG15AHH3zo1ttu/4df7pnW+K4F9HrNBx54wBfPrhk2rqBe/lHyhPIMu1f9jrce+gt6r9jVWy8L9vU20NaZgAkMJwHfJT6c/epWmYAJmIAJDBkBO+wh61A3xwRMwARMYDgJ2GEPZ7+6VSZgAiZgAkNGwGvYQ9ahbo4JmEAzCHDDHZ9m2Gor60HADrse/WArTMAERoYAv+BfvHjxyDTXDa2MgB12ZSityARMwAQ6IbD0yKcTScuYQErAa9gpjT6leSAOP8bXX2GVsTcShWIlM2UGT2mYkR4eREDBGRVpJYwetLXaO20+zyuItwZNK2wBEzABE2g6ATvsfvcgDpIn7/C0Hf544k+r17Glz6dFrIxja9VCnlLEg37WrVvfSiDyS3rW0KMEoxCNEnhS0kwfDERBiksPzxhq87SmTKXeNAETMIGmE7DD7ncP8sw8XLVq5RG4bV5xE5bxrEGecMsncson0EbV99xzN9/Vai5vmzWYgAmYgAnkCdhh55n0L4eH6TPHnVF9PKE+4uQREMbjRqbi1fmcTC08lJhH5vLgXAzY+oMfxt6Y/pLD7J/qFBKQQont3LVL1aXzfoSVGTEDymIhMsqXYSrC6wLZTCfuUTwdPaigvsnno/cMkkM6DYlH8agds6kLGRUPVmqXMkO40NRg4oQJmIAJ1IGAHfZgegF/g8/gwf3Mcae1AOeKW8W54oR4dr/C6cTMKU4Oxc87/wKeTU8+mcTbC3MytVBWr6Pn7XjxRpqMjDYJCaCc2iMwwKvsZQMCuDq+5Q4jM7wjtfD8ffKJ6uvR+YqBY6de8qMqpERiK1Zcr0y8KaWkk+aTjw0UZC+ZpCXGt/yuJGOTBJAXnXkm+WLFZuzNC+dNRdgfEzABE6gPATvswfSF/B/ei1mgnG7GDqaSmgXyjYPUq3t47R2eRpL40SjCm/XkDiPens8JYRJMbTFAGnCceLJCG9IiaXrtmm9qk1fL6dXxeLvVq1cpkwSbSmucQZrgv/yl8jPfvOSHcQOZvB4nXlLEUCZe6aPX9mVKxaZi+9pUkD92aVigluqF9q2EOzQ1NDthAiZgAn0mYIfdZ+BHVYfXxE88t3v3UblHNpgUahbId9yZpWllOPIoJSenfE038zkhTILbzXCfqR6C86nAjNLyxDHCUNR6Rhr2Fi3P01hm3jJS7wQs1Fk4DijMpHhhfmFmYV3ONAETMIEBErDDHiD8GVctB4b/johuqIhMZpCKMOdzJIx/Qib2oip9q24o7DyhYUSqEJ2dF0eSeEBeHlfN9F0tJTaeF1COas/sLcxEpjC/MDOj0JsmYAImMHACdtj97oL0Tivqxne2euF83jIcmO4UY1esE5PGt6FW8rifOSefnM8JbdxiFuFfZRI3ppQ0kNCMnyA5tklgbGwsihcmUBhrzwwXdGdZoSSZeQdJxJtJP7uoFMtJaNb7lZU3KB0zbBYFyMl80toxg82MQLo5I+G0oNMm0B8CnImcBfqL87o/VbuWmhOww+53B+Edr7rqyjghmZjmHVgrm/jZMcvDKsvtVOFcUcK9ZspnXZkq8jmhE+cX682RiUlaBb9v470IoIrfZzM4kABrwHhQMkM+k0gj8BSn9oxAuomFhM3TK5HWqtFPvm4ro2n69TmZuqVOowfy+ZCZxrHT2qlIm2mNaXpGwmlBp02gDwQYs8ZzGnQWz+j+kj5Y6CoGSOCYqTcOz/3wh7qzgCfXT0xMLF++vLviw1oKLJs2bZqampo7d+6wtnGU23X22WdPTk6OQv8ec8wx7777bk/7uobXkAcffOjW227/h1/u6WnDO1TOLSnEn+Luyw5LWWxYCXiGPaw963aZgAkMAwFWuDLNIMIU92OyNBYh9Fgmi5xMLCqjx5uNI2CH3bgus8EmYALDQCCcroLerPKk60S0kBxWghYu/GS+tbofUw8Y4EFG3JsZDxugFEF1wulksq4UN5fklTincQTssBvXZTbYBEyg8QTkpPUjCP0kkns19NiAaJvu3uCmjciJhJ56pAcMpGkeNiB5nraEcPpggyjrRHMJ2GE3t+9suQmYQFMJ8HuHWJmW287cqkl8WzeQdtFCVMXdqfqRZxdKXKSGBOywa9gpNskETGDICRTOm6PN+mFk1y+jQ7kGAcTJ+dWG7zMPsE1P2GE3vQd7aD/nOTetqIJYb2PgH/e29LBuqzaBUSXAScfcumtvrdNWv3vUcwsKn14wqnSb3W477Gb3X0+tZ4WMcbqq4AUher8I15GuLyWdW8vdN+2fvtK5KkuaQIMIKIIdAW2c90zHx5y2nKqcPpTlW2/uaRABm9qGwKw2+7zLBIIAA/Zpn3cWwk6YgAl0R4CF7VjbbqUhhtEIFKa5eS1z/1orVc5vFgHPsJvVX+2sZSTOmFrDaskxWidTY23yFSVjV+HPNNmr4np9CGIREiefTe5llcIY8odmVZf5Rk9GIQJ5I8kMezS9oF5+lyJ7pDOviiKhKtqVMcCbJmACJjBMBOywh6Q3cWDxmmrepSHPR9uIrfE2TIbhipKRI3eon2lGJvnxUm3enYW2lItG8dzAko798ZestLGLJ5iGC49SMoC9/IVCMsNIympkgLvVz0YxiRtk2FRMTzfOoFBiUhWbJGiaWtH+/p0wyQkTMAETaDQBh8Qb3X3/YjzxalwdjpZQWPowbd51oeAY3zwtHG/NmzTjDSLKpBQPZ6C4JPGC/6K3dQrXy4PH2V+4pM3jmXjmA9XhfUMhC+HxLm1e7MEEnUpfeOFnGCmni1fOV4ie0MDrriklGYqoVL6Ic0zABExg+Ah4hj0kfapZKVNVRaHxlIUNO3jw4NS+qfSWFlwmkjxvoVC+TaYKthLA9/OUJT0RApMkzHdYGH53/4EDrZSQX1hLYWYbJd5lAiZgAkNAwA57CDrxj03ARypuLE9Z2DAm4vPG5yEgSX1TsIsffkw7uyV+Lv1M6Im3Yw9FiKunVZOTf1RyanlhLYWZaSmnTcAETGD4CNhhD0mfsjwcc1ZcYLg0xaVppBaVmYjzXk4tFZPJVJXpL9Nx5Plo6ZoF6fyadB4Ti9BE18mnVFQdYmjQ2jM5c+bMYVmdBEWuvuaLkqEUVZMmGo+RmjSjRzakd6T7DdYi5m8TMIERJ+A17CE5AJjO8j4AuUCaFIu+eDt8pNwhU1t2aaE6XCz3neHFyf/Jk0+QScgazx3F29Bh6Rp51ZiXZ69u7ZYGVU0mjjxjJNXFvW/MxWUeJmkwQUGW5EMVzUlX6NuY510mYAImMGQE7LCHp0Nxh/zl25P3pjhF+cVUGMcptxqZeM3IiURaRV5zlCVR6FkLjSy0J2osVFVYJK3daRMwARMYMgIOiQ9Zh7o5JmACJmACw0nADns4+9WtMgETMAETGDICdthD1qFHNYeF7cK49FFC3jABEzABE2gCATvsJvSSbTQBEzABExh5AnbYI38IGIAJmIAJmEATCNhhN6GXbKMJmIAJmMDIE/DPunp1CGzduvWEE07olXbrHRyBQ4cODa5y12wCJjC6BOywe9X3N954Y69UW68JmIAJmMDoEbDDrr7Pv/CFL0xMTFSvt64ad+7c+corr9DquhrYE7scPukJVis1ARNoTcAOuzWbbvcsOvLptnQjy/3hD39Yvnx5I0230SZgAibQEAK+6awhHWUzTcAETMAERpuAHfZo979bbwImYAIm0BACdtgN6SibaQIm0GMCk5OT8+bN43bRX//61yRWrFjR4wqt3gRmRsAOe2a8LG0CJjCsBBYvXnz88ce//vrr77zzzmuvvTZq91EOa7cOU7vssIepN90WEzCBUgTWrFnzT//0h3d+/9sLL7zw9NNPL6XLhU2gagJ22FUTtT4TMIHGEgg/jedubCNs+NASsMMe2q51w0zABLoggKsOt91FcRcxgd4R8O+we8fWmk3ABJpHwN66eX02MhZ7hj0yXe2GmoAJdEZg7ty5nQlaygT6SsAOu6+4XZkJmIAJmIAJdEfADrs7bi5lAiZgAiZgAn0lYIfdV9yuzARMwARMwAS6I2CH3R03lzIBEzABEzCBvhLwXeJ9xe3KTMAETAACmzZt2rx5s1GMGoFnnnmmTJPtsMvQc1kTMAETmDGBrVu3rly58vDhwzMu6QKjTcAOe7T7v1zrX3rpJS49L7744q9+9au1a9eeeOKJ1157bTmVLm0Cw0/gzTffxFvfeeedF1100fC31i08QuCyyy7bvXt3SRh22CUBjnTx+fPn33vvvYcOHYICbnvDhg0jjcONN4GZEDjhhBP8g++ZAGu27OzZs8s3wDedlWc4uho4BFetWqX2e3o9useBW24CJtAXArVw2EzRCKh6RacvPV5xJcTAcdUoxXNXMoSs2D6rM4EZEnj++Rcuvew/zrCQxU2gHwRq4bDffvvtW265hTfG2233o88rrUOTbE+v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#pic_cente
