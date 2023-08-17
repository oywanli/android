---
id: o3e5ib2o
title: Android Pos demo Work Doc
file_version: 1.1.3
app_version: 1.15.0
---

To test QPOS mpos directly, you can download and install the demo APK provided above. For more details, please visit our knowledge base: [dspread.gitlab.io/qpos](https://dspread.gitlab.io/qpos/#/)

## QPOS Programming Guide

### Change List

<br/>

|Version|Author       |Date      |Description                                                                                                             |
|-------|-------------|----------|------------------------------------------------------------------------------------------------------------------------|
|0.1    |Austin Wang  |2016-05-01|Initially Added                                                                                                         |
|1.0    |Austin Wang  |2016-09-01|Added EMV related function                                                                                              |
|1.1    |Ausitn Wang  |2017-03-01|Merge QPOS standard and EMV Card reader together                                                                        |
|1.2    |Austin Wang  |2017-10-20|Added UART interface support for GES device                                                                             |
|2.8.0  |Zhengwei Fang|2018-05-14|Change Maximum length of transaction amount to 12                                                                       |
|2.9.0  |Zhengwei Fang|2020-03-18|Add CQPOSService to implement listener callback                                                                         |
|3.0.0  |Zhengwei Fang|2020-06-04|Update CVM pin and fix usb otg bug                                                                                      |
|3.1.0  |Zhengwei Fang|2020-07-27|Add getRandomeNumByLen() and BLE ClearBluetooth function                                                                |
|3.2.0  |Zhengwei Fang|2020-12-17|Add update TR31 keys，fix the "cashback is 0" issue                                                                      |
|3.3.0  |Zhengwei Fang|2021-03-08|Comment createInsecureRfcommSocketToServiceRecord function and add updateIPEKOperationByKeyType function                |
|3.4.0  |Zhengwei Fang|2021-04-11|Add doFelicaOp,generateTransportKey and updateIPEKByTransportKey function                                               |
|3.5.0  |Zhengwei Fang|2021-05-08|Add the sync sendapdu,sendNfcapdu function and multiple cards prompt for CR100,                                         |
|3.6.0  |Zhengwei Fang|2021-06-15|Add getPin function for CR100 & D20 and boardcastReceiver to detect the bluetooth if open or close                      |
|3.7.0  |Zhengwei Fang|2021-08-11|Add clearD20Device,getD20SpLog function to pos log for D20&D30                                                          |
|3.8.0  |Zhengwei Fang|2021-08-11|Fix offline pin bug and change sdk android gradle veision to 30                                                         |
|3.9.0  |Zhengwei Fang|2021-11-22|Add modelInfo and change compile time format in the getQposInfo function                                                |
|4.0.0  |Zhengwei Fang|2022-03-24|Add the new method isBootMode to check the device boot status and update the multi-application selection for contactless|
|4.1.0  |Zhengwei Fang|2022-07-04|Add playBuzzerByType(),operateLEDByType() function                                                                      |
|4.2.0  |Zhengwei Fang|2022-08-25|Add getEncryptedTrack2Data method to get ksn and EncryptedTrack2data.                                                   |
|4.3.0  |Zhengwei Fang|2023-03-02|Fix the app crash bug caused by disconnecting Bluetooth during Bluetooth scanning                                       |

<br/>

## Introduction

QPOS is a serial of mobile payment devices. It can communicate with the mobile device through audio jack, UART or USB cable.

QPOS standard, QPOS mini, QPOS Plus, EMV06, EMV08, GEA and GES are all QPOS products, some of them are with PINPAD embedded and some of them are only card readers without PINPAD.

This document aims to help readers for using the Android SDK of QPOS.

## Programming Model

All methods the SDK provided can be devided into three types:

1.  Init methods；

2.  Interactive methods；

3.  Listener methods.

The application use the init method to init the EMV card reader hardware and get an instance of the Card Reader. It then can use the interactive methods to start the communication with the card reader. During the communication process, if any message returned from the Card reader, a listener method will be invoked by the SDK package.

To avoid the application block and improve the speed of data interaction between the smart terminal and QPOS, the SDK framework is designed to work under asynchronous mode.

## Programming Interface

### Initialization

<br/>

The `QPOSService`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:355:5:5:`        pos = QPOSService.getInstance(mode);`"/> Class serves as the foundation of the SDK library. Prior to creating an instance of this core class using the `CommunicationMode`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:350:7:7:`    private void open(CommunicationMode mode) {`"/>mode parameter, the application must ensure that all the sub-functions are registered in the `QPOSServiceListener`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:974:14:14:`         * @see com.dspread.xpos.QPOSService.QPOSServiceListener#onRequestTransactionResult(com.dspread.xpos.QPOSService.TransactionResult)`"/>. The following code snippet demonstrates how to initialize the SDK.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
350        private void open(CommunicationMode mode) {
351            TRACE.d("open");
352            //pos=null;
353    //        MyPosListener listener = new MyPosListener();
354            MyQposClass listener = new MyQposClass();
355            pos = QPOSService.getInstance(mode);
356            if (pos == null) {
357                statusEditText.setText("CommunicationMode unknow");
358                return;
359            }
360            if (mode == CommunicationMode.USB_OTG_CDC_ACM) {
361                pos.setUsbSerialDriver(QPOSService.UsbOTGDriver.CDCACM);
362            }
363            pos.setConext(MainActivity.this);
364            //init handler
365            Handler handler = new Handler(Looper.myLooper());
366            pos.initListener(handler, listener);
367            String sdkVersion = pos.getSdkVersion();
368            Toast.makeText(MainActivity.this, "sdkVersion--" + sdkVersion, Toast.LENGTH_SHORT).show();
369        }
```

<br/>

The `CommunicationMode`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:2481:3:3:`                        open(CommunicationMode.BLUETOOTH);`"/>can be below
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
2481                           open(CommunicationMode.BLUETOOTH);
2482                           posType = POS_TYPE.BLUETOOTH;
2483                       } else if (type == 4) {
2484                           open(CommunicationMode.BLUETOOTH_BLE);
2485                           posType = POS_TYPE.BLUETOOTH_BLE;
```

<br/>

This code snippet opens the `QPOSService` communication mode using the `AUDIO` method.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/OtherActivity.java
```java
142                    open(QPOSService.CommunicationMode.AUDIO);
```

<br/>

This code snippet opens the communication mode of a QPOSService object using UART.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/OtherActivity.java
```java
159                    open(QPOSService.CommunicationMode.UART);
```

<br/>

The app should select the suitable communication mode based on its hardware configuration. Note that in the example above, the app should understand the callback methods of `MyQposClass`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:354:1:1:`        MyQposClass listener = new MyQposClass();`"/>. Additionally, the code above demonstrates how to establish the communication bridge using the previously described `open`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/OtherActivity.java:142:1:1:`                open(QPOSService.CommunicationMode.AUDIO);`"/> method.

## Get Device Information

<br/>

To obtain the device information, users can issue the following command:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
570                pos.getQposInfo();
```

<br/>

_Note the pos is the instance of QPOSService, the app get it during the initialization process._

<br/>

The device information will be returned on the improved call back
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
<!-- collapsed -->

```java
931            @Override
932            public void onQposInfoResult(Hashtable<String, String> posInfoData) {
933                TRACE.d("onQposInfoResult" + posInfoData.toString());
934                String isSupportedTrack1 = posInfoData.get("isSupportedTrack1") == null ? "" : posInfoData.get("isSupportedTrack1");
935                String isSupportedTrack2 = posInfoData.get("isSupportedTrack2") == null ? "" : posInfoData.get("isSupportedTrack2");
936                String isSupportedTrack3 = posInfoData.get("isSupportedTrack3") == null ? "" : posInfoData.get("isSupportedTrack3");
937                String bootloaderVersion = posInfoData.get("bootloaderVersion") == null ? "" : posInfoData.get("bootloaderVersion");
938                String firmwareVersion = posInfoData.get("firmwareVersion") == null ? "" : posInfoData.get("firmwareVersion");
939                String isUsbConnected = posInfoData.get("isUsbConnected") == null ? "" : posInfoData.get("isUsbConnected");
940                String isCharging = posInfoData.get("isCharging") == null ? "" : posInfoData.get("isCharging");
941                String batteryLevel = posInfoData.get("batteryLevel") == null ? "" : posInfoData.get("batteryLevel");
942                String batteryPercentage = posInfoData.get("batteryPercentage") == null ? ""
943                        : posInfoData.get("batteryPercentage");
944                String hardwareVersion = posInfoData.get("hardwareVersion") == null ? "" : posInfoData.get("hardwareVersion");
945                String SUB = posInfoData.get("SUB") == null ? "" : posInfoData.get("SUB");
946                String pciFirmwareVersion = posInfoData.get("PCI_firmwareVersion") == null ? ""
947                        : posInfoData.get("PCI_firmwareVersion");
948                String pciHardwareVersion = posInfoData.get("PCI_hardwareVersion") == null ? ""
949                        : posInfoData.get("PCI_hardwareVersion");
950                String compileTime = posInfoData.get("compileTime") == null ? ""
951                        : posInfoData.get("compileTime");
952                String content = "";
953                content += getString(R.string.bootloader_version) + bootloaderVersion + "\n";
954                content += getString(R.string.firmware_version) + firmwareVersion + "\n";
955                content += getString(R.string.usb) + isUsbConnected + "\n";
956                content += getString(R.string.charge) + isCharging + "\n";
957    //			if (batteryPercentage==null || "".equals(batteryPercentage)) {
958                content += getString(R.string.battery_level) + batteryLevel + "\n";
959    //			}else {
960                content += getString(R.string.battery_percentage) + batteryPercentage + "\n";
961    //			}
962                content += getString(R.string.hardware_version) + hardwareVersion + "\n";
963                content += "SUB : " + SUB + "\n";
964                content += getString(R.string.track_1_supported) + isSupportedTrack1 + "\n";
965                content += getString(R.string.track_2_supported) + isSupportedTrack2 + "\n";
966                content += getString(R.string.track_3_supported) + isSupportedTrack3 + "\n";
967                content += "PCI FirmwareVresion:" + pciFirmwareVersion + "\n";
968                content += "PCI HardwareVersion:" + pciHardwareVersion + "\n";
969                content += "compileTime:" + compileTime + "\n";
970                statusEditText.setText(content);
971            }
```

<br/>

## Get Device ID

<br/>

The device ID is use to indentifying one paticular EMV card reader. The app use below method to get the device ID:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
576                pos.getQposId();
```

<br/>

The Device ID is returned to the app by the callback below.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1071           @Override
1072           public void onQposIdResult(Hashtable<String, String> posIdTable) {
1073               TRACE.w("onQposIdResult():" + posIdTable.toString());
1074               String posId = posIdTable.get("posId") == null ? "" : posIdTable.get("posId");
1075               String csn = posIdTable.get("csn") == null ? "" : posIdTable.get("csn");
1076               String psamId = posIdTable.get("psamId") == null ? "" : posIdTable
1077                       .get("psamId");
1078               String NFCId = posIdTable.get("nfcID") == null ? "" : posIdTable
1079                       .get("nfcID");
1080               String content = "";
1081               content += getString(R.string.posId) + posId + "\n";
1082               content += "csn: " + csn + "\n";
1083               content += "conn: " + pos.getBluetoothState() + "\n";
1084               content += "psamId: " + psamId + "\n";
1085               content += "NFCId: " + NFCId + "\n";
1086               if (!isVisiblePosID) {
1087                   statusEditText.setText(content);
1088               } else {
1089                   isVisiblePosID = false;
1090                   BaseApplication.setmPosID(posId);
1091               }
1092           }
```

<br/>

## Start Transaction

<br/>

The app can start a magnatic swipe card transaction, or an EMV chip card transaction, by below method:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
2449                       pos.doTrade(keyIdex, 30);//start do trade
```

<br/>

## Set Transaction Amount

<br/>

The transaction amount can be set by:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1198                       pos.setAmount(amount, cashbackAmount, "156", transactionType);
```

<br/>

the setAmount method can be called before start a transaction. If it was not called, a below call back \`will be invoked by the SDK, giving app another chance to enter the transaction amount.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1133           public void onRequestSetAmount() {
```

<br/>

The `setAmount`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/OtherActivity.java:1123:3:3:`                    pos.setAmount(amount, cashbackAmount, &quot;156&quot;, transactionType);`"/>method has below parameters:

1.  amount : how much money in cents

2.  cashbackAmount : reserved for future use

3.  currency code : US Dollar, CNY, etc

4.  transactionType : which kind of transaction to be started. The transaction type can be:

<br/>

Transaction type is used mainly by the EMV Chip card transaction, for magnetic card, app can always use GOODS.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1157                       if (transactionTypeString.equals("GOODS")) {
1158                           transactionType = TransactionType.GOODS;
1159                       } else if (transactionTypeString.equals("SERVICES")) {
1160                           transactionType = TransactionType.SERVICES;
1161                       } else if (transactionTypeString.equals("CASH")) {
1162                           transactionType = TransactionType.CASH;
1163                       } else if (transactionTypeString.equals("CASHBACK")) {
1164                           transactionType = TransactionType.CASHBACK;
1165                       } else if (transactionTypeString.equals("INQUIRY")) {
1166                           transactionType = TransactionType.INQUIRY;
1167                       } else if (transactionTypeString.equals("TRANSFER")) {
1168                           transactionType = TransactionType.TRANSFER;
1169                       } else if (transactionTypeString.equals("ADMIN")) {
1170                           transactionType = TransactionType.ADMIN;
1171                       } else if (transactionTypeString.equals("CASHDEPOSIT")) {
1172                           transactionType = TransactionType.CASHDEPOSIT;
1173                       } else if (transactionTypeString.equals("PAYMENT")) {
1174                           transactionType = TransactionType.PAYMENT;
1175                       } else if (transactionTypeString.equals("PBOCLOG||ECQ_INQUIRE_LOG")) {
1176                           transactionType = TransactionType.PBOCLOG;
1177                       } else if (transactionTypeString.equals("SALE")) {
1178                           transactionType = TransactionType.SALE;
1179                       } else if (transactionTypeString.equals("PREAUTH")) {
1180                           transactionType = TransactionType.PREAUTH;
1181                       } else if (transactionTypeString.equals("ECQ_DESIGNATED_LOAD")) {
1182                           transactionType = TransactionType.ECQ_DESIGNATED_LOAD;
1183                       } else if (transactionTypeString.equals("ECQ_UNDESIGNATED_LOAD")) {
1184                           transactionType = TransactionType.ECQ_UNDESIGNATED_LOAD;
1185                       } else if (transactionTypeString.equals("ECQ_CASH_LOAD")) {
1186                           transactionType = TransactionType.ECQ_CASH_LOAD;
1187                       } else if (transactionTypeString.equals("ECQ_CASH_LOAD_VOID")) {
1188                           transactionType = TransactionType.ECQ_CASH_LOAD_VOID;
1189                       } else if (transactionTypeString.equals("CHANGE_PIN")) {
1190                           transactionType = TransactionType.UPDATE_PIN;
1191                       } else if (transactionTypeString.equals("REFOUND")) {
1192                           transactionType = TransactionType.REFUND;
1193                       } else if (transactionTypeString.equals("SALES_NEW")) {
1194                           transactionType = TransactionType.SALES_NEW;
1195                       }
```

<br/>

## Magstripe Card Transaction

<br/>

Magstripe card transaction is pretty simple.<br/>
After the app start a transaction, if the user use a magnatic card, the callback `onDoTradeResult`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/OtherActivity.java:630:5:5:`        public void onDoTradeResult(QPOSService.DoTradeResult result, Hashtable&lt;String, String&gt; decodeData) {`"/> will be called feeding the app magnatic card related information. The app then use the information returned for further processing.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
727                } else if (result == DoTradeResult.MCR) {//Magnetic card
728                    String content = getString(R.string.card_swiped);
729                    String formatID = decodeData.get("formatID");
730                    if (formatID.equals("31") || formatID.equals("40") || formatID.equals("37") || formatID.equals("17") || formatID.equals("11") || formatID.equals("10")) {
731                        String maskedPAN = decodeData.get("maskedPAN");
732                        String expiryDate = decodeData.get("expiryDate");
733                        String cardHolderName = decodeData.get("cardholderName");
734                        String serviceCode = decodeData.get("serviceCode");
735                        String trackblock = decodeData.get("trackblock");
736                        String psamId = decodeData.get("psamId");
737                        String posId = decodeData.get("posId");
738                        String pinblock = decodeData.get("pinblock");
739                        String macblock = decodeData.get("macblock");
740                        String activateCode = decodeData.get("activateCode");
741                        String trackRandomNumber = decodeData.get("trackRandomNumber");
742                        content += getString(R.string.format_id) + " " + formatID + "\n";
743                        content += getString(R.string.masked_pan) + " " + maskedPAN + "\n";
744                        content += getString(R.string.expiry_date) + " " + expiryDate + "\n";
745                        content += getString(R.string.cardholder_name) + " " + cardHolderName + "\n";
746                        content += getString(R.string.service_code) + " " + serviceCode + "\n";
747                        content += "trackblock: " + trackblock + "\n";
748                        content += "psamId: " + psamId + "\n";
749                        content += "posId: " + posId + "\n";
750                        content += getString(R.string.pinBlock) + " " + pinblock + "\n";
751                        content += "macblock: " + macblock + "\n";
752                        content += "activateCode: " + activateCode + "\n";
753                        content += "trackRandomNumber: " + trackRandomNumber + "\n";
754                        cardNo = maskedPAN;
755                    } else if (formatID.equals("FF")) {
756                        String type = decodeData.get("type");
757                        String encTrack1 = decodeData.get("encTrack1");
758                        String encTrack2 = decodeData.get("encTrack2");
759                        String encTrack3 = decodeData.get("encTrack3");
760                        content += "cardType:" + " " + type + "\n";
761                        content += "track_1:" + " " + encTrack1 + "\n";
762                        content += "track_2:" + " " + encTrack2 + "\n";
763                        content += "track_3:" + " " + encTrack3 + "\n";
764                    } else {
765                        String orderID = decodeData.get("orderId");
766                        String maskedPAN = decodeData.get("maskedPAN");
767                        String expiryDate = decodeData.get("expiryDate");
768                        String cardHolderName = decodeData.get("cardholderName");
769    //					String ksn = decodeData.get("ksn");
770                        String serviceCode = decodeData.get("serviceCode");
771                        String track1Length = decodeData.get("track1Length");
772                        String track2Length = decodeData.get("track2Length");
773                        String track3Length = decodeData.get("track3Length");
774                        String encTracks = decodeData.get("encTracks");
775                        String encTrack1 = decodeData.get("encTrack1");
776                        String encTrack2 = decodeData.get("encTrack2");
777                        String encTrack3 = decodeData.get("encTrack3");
778                        String partialTrack = decodeData.get("partialTrack");
779                        String pinKsn = decodeData.get("pinKsn");
780                        String trackksn = decodeData.get("trackksn");
781                        String pinBlock = decodeData.get("pinBlock");
782                        String encPAN = decodeData.get("encPAN");
783                        String trackRandomNumber = decodeData.get("trackRandomNumber");
784                        String pinRandomNumber = decodeData.get("pinRandomNumber");
785                        if (orderID != null && !"".equals(orderID)) {
786                            content += "orderID:" + orderID;
787                        }
788                        content += getString(R.string.format_id) + " " + formatID + "\n";
789                        content += getString(R.string.masked_pan) + " " + maskedPAN + "\n";
790                        content += getString(R.string.expiry_date) + " " + expiryDate + "\n";
791                        content += getString(R.string.cardholder_name) + " " + cardHolderName + "\n";
792    //					content += getString(R.string.ksn) + " " + ksn + "\n";
793                        content += getString(R.string.pinKsn) + " " + pinKsn + "\n";
794                        content += getString(R.string.trackksn) + " " + trackksn + "\n";
795                        content += getString(R.string.service_code) + " " + serviceCode + "\n";
796                        content += getString(R.string.track_1_length) + " " + track1Length + "\n";
797                        content += getString(R.string.track_2_length) + " " + track2Length + "\n";
798                        content += getString(R.string.track_3_length) + " " + track3Length + "\n";
799                        content += getString(R.string.encrypted_tracks) + " " + encTracks + "\n";
800                        content += getString(R.string.encrypted_track_1) + " " + encTrack1 + "\n";
801                        content += getString(R.string.encrypted_track_2) + " " + encTrack2 + "\n";
802                        content += getString(R.string.encrypted_track_3) + " " + encTrack3 + "\n";
803                        content += getString(R.string.partial_track) + " " + partialTrack + "\n";
804                        content += getString(R.string.pinBlock) + " " + pinBlock + "\n";
805                        content += "encPAN: " + encPAN + "\n";
806                        content += "trackRandomNumber: " + trackRandomNumber + "\n";
807                        content += "pinRandomNumber:" + " " + pinRandomNumber + "\n";
808                        cardNo = maskedPAN;
809                        String realPan = null;
810                        if (!TextUtils.isEmpty(trackksn) && !TextUtils.isEmpty(encTrack2)) {
811                            String clearPan = DUKPK2009_CBC.getData(trackksn, encTrack2, DUKPK2009_CBC.Enum_key.DATA, DUKPK2009_CBC.Enum_mode.CBC);
812                            content += "encTrack2:" + " " + clearPan + "\n";
813                            realPan = clearPan.substring(0, maskedPAN.length());
814                            content += "realPan:" + " " + realPan + "\n";
815                        }
816                        if (!TextUtils.isEmpty(pinKsn) && !TextUtils.isEmpty(pinBlock) && !TextUtils.isEmpty(realPan)) {
817                            String date = DUKPK2009_CBC.getData(pinKsn, pinBlock, DUKPK2009_CBC.Enum_key.PIN, DUKPK2009_CBC.Enum_mode.CBC);
818                            String parsCarN = "0000" + realPan.substring(realPan.length() - 13, realPan.length() - 1);
819                            String s = DUKPK2009_CBC.xor(parsCarN, date);
820                            content += "PIN:" + " " + s + "\n";
821                        }
822                    }
823                    statusEditText.setText(content);
824                } else if ((result == DoTradeResult.NFC_ONLINE) || (result == DoTradeResult.NFC_OFFLINE)) {
```

<br/>

Below table describes the meaning of each data element SDK returned:

<br/>

|Key           |Description                                                                                                  |
|--------------|-------------------------------------------------------------------------------------------------------------|
|maskedPAN     |Masked card number showing at most the first 6 and last 4 digits with in-between digits masked by “X”        |
|expiryDate    |4-digit in the form of YYMM in the track data                                                                |
|cardHolderName|The cardholder name as seen on the card. This can be up to 26 characters.                                    |
|serviceCode   |3-digit service code in the track data                                                                       |
|track1Length  |Length of Track 1 data                                                                                       |
|track2Length  |Length of Track 2 data                                                                                       |
|track3Length  |Length of Track 3 data                                                                                       |
|encTracks     |Reserved                                                                                                     |
|encTrack1     |Encrypted track 1 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK|
|encTrack2     |Encrypted track 2 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK|
|encTrack3     |Encrypted track 3 data with T-Des encryption key derived from DATA-key to be generated with trackksn and IPEK|
|partialTrack  |Reserved                                                                                                     |
|trackksn      |KSN of the track data                                                                                        |

<br/>

The track data returned in the hashtable is encrytped. It can be encrypted by Dukpt Data Key Variant 3DES CBC mode, or by Dukpt Data Key 3DES CBC mode. Per ANSI X9.24 2009 version request, The later (Data Key with 3DES CBC mode) is usually a recommanded choice.

## Decoding Track Data Encrypted with Data Key Variant

Below is an example of the data captured during a live magnatic transaction, the track data is encrypted using data key variant, in 3DES CBC mode:

```java
01-21 04:46:26.764: D/POS_SDK(30241): decodeData: {track3Length=0, track2Length=32, expiryDate=1011, encTrack3=, encPAN=, encTrack1=22FB2E931F3EFAFC8C3899AB779F3719E75D392365DB748EEA789560EEB7714D84AB7FFA5B2E162C9BD566D03DCD240FC9D316CAC4015B782294365F9062CA0A, pinRandomNumber=, encTrack2=153CEE49576C0B709515946D991CB48368FEA0375837ECA6, trackRandomNumber=, trackksn=00000332100300E00002, maskedPAN=622526XXXXXX5453, cardholderName=MR.ZHOU CHENG HAO         , partialTrack=, encTracks=153CEE49576C0B709515946D991CB48368FEA0375837ECA6, psamNo=, formatID=30, track1Length=68, pinKsn=, serviceCode=106, ksn=, pinBlock=}
01-21 04:46:26.766: D/POS_SDK(30241): swipe card:Card Swiped:Format ID: 30
01-21 04:46:26.766: D/POS_SDK(30241): Masked PAN: 622526XXXXXX5453
01-21 04:46:26.766: D/POS_SDK(30241): Expiry Date: 1011
01-21 04:46:26.766: D/POS_SDK(30241): Cardholder Name: MR.ZHOU CHENG HAO   
01-21 04:46:26.766: D/POS_SDK(30241): trackksn: 00000332100300E00002
01-21 04:46:26.766: D/POS_SDK(30241): Service Code: 106
01-21 04:46:26.766: D/POS_SDK(30241): Track 1 Length: 68
01-21 04:46:26.766: D/POS_SDK(30241): Track 2 Length: 32
01-21 04:46:26.766: D/POS_SDK(30241): Track 3 Length: 0
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Tracks: 153CEE49576C0B709515946D991CB48368FEA0375837ECA6
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 1: 22FB2E931F3EFAFC8C3899AB779F3719E75D392365DB748EEA789560EEB7714D84AB7FFA5B2E162C9BD566D03DCD240FC9D316CAC4015B782294365F9062CA0A
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 2: 153CEE49576C0B709515946D991CB48368FEA0375837ECA6
01-21 04:46:26.766: D/POS_SDK(30241): Encrypted Track 3: 
01-21 04:46:26.766: D/POS_SDK(30241): pinKsn: 00000332100300E000C6
01-21 04:46:26.766: D/POS_SDK(30241): pinBlock: 377D28B8C7EF080A
01-21 04:46:26.766: D/POS_SDK(30241): encPAN: 
01-21 04:46:26.766: D/POS_SDK(30241): trackRandomNumber: 
01-21 04:46:26.766: D/POS_SDK(30241): pinRandomNumber:
```

The track ksn 00000332100300E00002 can be used to decode the track data:

Track 1 data: 22FB2E931F3EFAFC8C3899AB779F3719E75D392365DB748EEA789560EEB7714D84AB7FFA5B2E162C9BD566D03DCD240FC9D316CAC4015B782294365F9062CA0A

Track 2 data: 153CEE49576C0B709515946D991CB48368FEA0375837ECA6

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

```c
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

## Decoding Track Data Encrypted with Data Key

Below is another example, the track data is encrypted using data key whith 3DES CBC mode (per ANSI X9.24 2009 version request)

```java
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

```java
10-07 11:37:49.571: V/vahid(20753): ???? ????? ??:Format ID: 30
10-07 11:37:49.571: V/vahid(20753): Masked PAN: 622622XXXXXX3256
10-07 11:37:49.571: V/vahid(20753): Expiry Date: 2612
10-07 11:37:49.571: V/vahid(20753): Cardholder Name:
10-07 11:37:49.571: V/vahid(20753): KSN:
10-07 11:37:49.571: V/vahid(20753): pinKsn: 09118041200085E0000B
10-07 11:37:49.571: V/vahid(20753): trackksn: 09118041200085E00013
10-07 11:37:49.571: V/vahid(20753): Service Code: 220
10-07 11:37:49.571: V/vahid(20753): Track 1 Length: 0
10-07 11:37:49.571: V/vahid(20753): Track 2 Length: 37
10-07 11:37:49.571: V/vahid(20753): Track 3 Length: 0
10-07 11:37:49.571: V/vahid(20753): Encrypted Tracks: 1909568B7256B930EC0DFAB30061B640F24CD3CD0006D349
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 1:
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 2: 1909568B7256B930EC0DFAB30061B640F24CD3CD0006D349
10-07 11:37:49.571: V/vahid(20753): Encrypted Track 3: 
10-07 11:37:49.571: V/vahid(20753): Partial Track:
10-07 11:37:49.571: V/vahid(20753): pinBlock: FFB0DFF5141385FA
10-07 11:37:49.571: V/vahid(20753): encPAN:
10-07 11:37:49.571: V/vahid(20753): trackRandomNumber:
10-07 11:37:49.571: V/vahid(20753): pinRandomNumber:
```

Decode the Track 2 data using the method descripted before: 6226220129263256D26122200059362100000FFFFFFFFFFF

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
    KSN = "09118041200085E0000B"
    DATA = "FFB0DFF5141385FA"
    #DATA="1909568B7256B930EC0DFAB30061B640F24CD3CD0006D349"
    print decrypt_pinblock(KSN, DATA)
```

The decrypted PINBLOCK (formated Pin data) is: 041173DFED6D9CDA The real PIN value can be caculated using formated pin data and PAN as inputs, according to ANSI X9.8. Below is an example:

1.  PAN: 6226220129263256

2.  12 right most PAN digits without checksum: 622012926325

3.  Add 0000 to the left: 0000622012926325

4.  XOR ([#3](/dspread/android/-/issues/3)) and Formated PIN Data

XOR (0000622012926325, 041173DFED6D9CDA) = 041111FFFFFFFFFF In our example, the plain PIN is 4 bytes in length with data "1111"

## Chip Card Transaction

EMV Chip card transaction is much more complicate than magnatic swipe card transaction. The EMV kernel inside the device may need a lot of information to process the transaction, including:

1.  PIN from the card holder

2.  Current time from the application

3.  Preferred EMV application from card holder

4.  The process result from the bank (card issuer) for the transaction

## Start Chip Card Transaction

<br/>

The app start the EMV transaction by calling below method
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
718                    pos.doEmvApp(EmvOption.START);
```

<br/>

This is usually happens inside the call back of `onDoTradeResult`<swm-token data-swm-token=":pos_android_studio_demo\pos_android_studio_app\src\main\java\com\dspread\demoui\activities\MainActivity.java:707:5:5:`        public void onDoTradeResult(DoTradeResult result, Hashtable&lt;String, String&gt; decodeData) {`"/>, as below demo code shows:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
715                } else if (result == DoTradeResult.ICC) {
716                    statusEditText.setText(getString(R.string.icc_card_inserted));
717                    TRACE.d("EMV ICC Start");
718                    pos.doEmvApp(EmvOption.START);
719                } else if (result == DoTradeResult.NOT_ICC) {
```

<br/>

## Input PIN

<br/>

The PIN information can be sent to the EMV kernel by below method in callback `onRequestSetPin`<swm-token data-swm-token=":pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java:1546:5:5:`        public void onRequestSetPin() {`"/>:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1564                           pos.sendPin(pin);
```

<br/>

Note, the kernel will not call the callback if PIN is not required for the transaction, or if the QPOS itself is with an embedded PINPAD.

<br/>

If the user do not want to input PIN, the applicaiton can bypass PIN enter by calling
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1576                       pos.sendPin("");
```

<br/>

if the user want to cancel the transaction, the app should call
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1587                       pos.cancelPin();
```

<br/>

## Set Time

<br/>

The current time information can be sent to the EMV kernel by:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1269           @Override
1270           public void onRequestTime() {
1271               TRACE.d("onRequestTime");
1272               dismissDialog();
1273               String terminalTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
1274               pos.sendTime(terminalTime);
1275               statusEditText.setText(getString(R.string.request_terminal_time) + " " + terminalTime);
1276           }
```

<br/>

If there is multiple EMV applications inside one Chip card, the SDK will ask the user to choose one application from a list:
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1095           public void onRequestSelectEmvApp(ArrayList<String> appList) {
```

<br/>

Then chosen application is sending to the EMV kernel by below method in the callback `onRequestSelectEmvApp`<swm-token data-swm-token=":pos_android_studio_demo\pos_android_studio_app\src\main\java\com\dspread\demoui\activities\MainActivity.java:1095:5:5:`        public void onRequestSelectEmvApp(ArrayList&lt;String&gt; appList) {`"/>
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1114                       pos.selectEmvApp(position);
```

<br/>

## Online Request

<br/>

If the EMV kernel found the transaction need to go online, below call back will be called.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1226           public void onRequestOnlineProcess(final String tlv) {
```

<br/>

Then users can send the authorization code returned from issuer bank according calling the below api.
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 pos_android_studio_demo/pos_android_studio_app/src/main/java/com/dspread/demoui/activities/MainActivity.java
```java
1260                                   pos.sendOnlineProcessResult(str);//Script notification/55domain/ICCDATA
```

<br/>

Below is an exmple of tlv data received by onRequestOnlineProcess:

```objectivec
2014-08-27 17:52:21.210 qpos-ios-demo[391:60b] alertView.title = Online process requested.
2014-08-27 17:52:21.211 qpos-ios-demo[391:60b] hideAlertView
2014-08-27 17:52:21.221 qpos-ios-demo[391:60b] onRequestOnlineProcess = {
    tlv = 5F200220204F08A0000003330101015F24032312319F160F4243544553542031323334353637389F21031752139A031408279F02060000000000019F03060000000000009F34034203009F120A50424F43204445424954C409623061FFFFFFFF5284C10A00000332100300E00003C708A68701E68CB34BDEC00A00000332100300E00003C2820150E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF;
}
```

The tlv data can be decoded using the online EMVlab tool:

[http://www.emvlab.org/tlvutils/?data=5F200220204F08A0000003330101015F24032312319F160F4243544553542031323334353637389F21031752139A031408279F02060000000000019F03060000000000009F34034203009F120A50424F43204445424954C409623061FFFFFFFF5284C10A00000332100300E00003C708A68701E68CB34BDEC00A00000332100300E00003C2820150E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF%0D%0A](http://www.emvlab.org/tlvutils/?data=5F200220204F08A0000003330101015F24032312319F160F4243544553542031323334353637389F21031752139A031408279F02060000000000019F03060000000000009F34034203009F120A50424F43204445424954C409623061FFFFFFFF5284C10A00000332100300E00003C708A68701E68CB34BDEC00A00000332100300E00003C2820150E84B5D0D2AA9F40A2EFCC52424C52DDE2ABB1A07F8B53A8F37837A9AA4BF7200CC55AA1480ED5665AEC03DFE493248AEEA126345F1C2BA0EB0AA82546CC0AF5E6F4E40D7F9A3788C8F35B33F5AF1D85231D77FCE112A1C9D2AFF3679C3C46456232D32FD0D2AAF288CFD4CC52C1F33F128C247296C9E46647D930ACED5B34CFD0C2A823B3F91BEC60E8280005CB96C3EFCCC352F0A30F77A2A033361B5C2C720D8B6E85BFA3C589ADBD6FAF15D3C520085A5276B736860441BB15DBF8FA537708654EE90E32C194D1487362498F59346706FD797DFC8DD28FCF31E7D49886BA62779EC42411A54F03FE22B9431969B780E8280005CB96C3EEF460C1F76C0F2217EAC9B999E3E03128A93A11A4FC6885E4106A4EA4D815D10900AC6AC95E3325D585CB8678AE17A4DEE4C45E2E44209B9493B5FD94F3F46CCF730CD8FED9430B7574CE670018A94907B2AA4B475A93ABF%0D%0A)

As we can see from the decoded table:

<br/>

|Tag |Tag Name                |Value                      |
|----|------------------------|---------------------------|
|5F20|Cardholder Name         |<br/>                      |
|4F  |AID<br/>                |A000000333010101           |
|5F24|App Expiration Date<br/>|231231                     |
|9F16|Merchant ID             |B C T E S T 1 2 3 4 5 6 7 8|
|9F21|Transaction Time        |175213                     |
|... |...                     |...                        |
|C4  |Masked PAN              |623061FFFFFFFF5284         |
|C1  |KSN(PIN)                |00000332100300E00003       |
|C7  |PINBLOCK                |A68701E68CB34BDE           |
|C0  |KSN Online Msg          |00000332100300E00003       |
|C2  |Online Message          |E84B5D0D2AA9F40A2EFC....   |

<br/>

Inside the table, there are:

1.  Some EMV TAGs (5F20,4F,5F24 ...) with plain text value.

2.  Some Proprietary tags starting with 0xC, in our case C4,C1,C7,C0 and C2.

The defination of proprietary tags can be found below:

<br/>

|Tag|Name                      |Length(Bytes)|
|---|--------------------------|-------------|
|C0 |KSN of Online Msg         |10           |
|C1 |KSN of PIN                |10           |
|C2 |Online Message(E)         |var          |
|C3 |KSN of Batch/Reversal Data|10           |
|C4 |Masked PAN                |0~10         |
|C5 |Batch Data                |var          |
|C6 |Reversal Data             |var          |
|C7 |PINBLOCK                  |8            |

<br/>

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

[http://www.emvlab.org/tlvutils/?data=708201479f02060000000000015a096230615710101752845713623061571010175284d231222086038214069f9f101307010103a02000010a01000000000013f6c0429f160f4243544553542031323334353637389f4e0f61626364000000000000000000000082027c008e0e000000000000000042031e031f005f24032312315f25031307304f08a0000003330101019f0702ff009f0d05d8609ca8009f0e0500100000009f0f05d8689cf8009f2608059aae950d0b7a679f2701809f3602008d9c01009f3303e0f8c89f34034203009f3704c1cdd24a9f3901059f4005f000f0a0019505088004e0009b02e8008408a0000003330101019a031408275f2a0201565f3401019f03060000000000009f0902008c9f1a0206439f1e0838333230314943439f3501229f4104000000015f200220205f300202205f28020156500a50424f432044454249540000000000](http://www.emvlab.org/tlvutils/?data=708201479f02060000000000015a096230615710101752845713623061571010175284d231222086038214069f9f101307010103a02000010a01000000000013f6c0429f160f4243544553542031323334353637389f4e0f61626364000000000000000000000082027c008e0e000000000000000042031e031f005f24032312315f25031307304f08a0000003330101019f0702ff009f0d05d8609ca8009f0e0500100000009f0f05d8689cf8009f2608059aae950d0b7a679f2701809f3602008d9c01009f3303e0f8c89f34034203009f3704c1cdd24a9f3901059f4005f000f0a0019505088004e0009b02e8008408a0000003330101019a031408275f2a0201565f3401019f03060000000000009f0902008c9f1a0206439f1e0838333230314943439f3501229f4104000000015f200220205f300202205f28020156500a50424f432044454249540000000000)

All the online message in embedded inside tag 0x70, the ending 00 are paddings for 3DES encryption.

## Get Transaction Result

<br/>

This file was generated by Swimm. [Click here to view it in the app](https://app.swimm.io/repos/Z2l0bGFiJTNBJTNBYW5kcm9pZCUzQSUzQWRzcHJlYWQ=/docs/o3e5ib2o).
