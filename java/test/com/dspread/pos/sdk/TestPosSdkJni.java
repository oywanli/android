package com.dspread.pos.sdk;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import junit.framework.TestCase;

public class TestPosSdkJni extends TestCase{
	private String updateFirmwareStr="";
	private OutputStream output = null;
	private InputStream input = null;
	public void testSdk() throws Exception{
	  		//-Djava.library.path=
			//System.out.println(System.getProperty("java.library.path"));
			byte[] bytes = {0x4D,0x00,0x01,0x23, 0x6F};
			System.out.println(PosSdkJni.isPackageRreceiveComplete());			
			for (byte b : bytes) {
				PosSdkJni.onChar(b);
			}
			System.out.println(PosSdkJni.isPackageRreceiveComplete());;
			
			PosSdkJni.packU8("TradeMode", (byte)0x0A);
			PosSdkJni.packBytes("TradeAmount", new byte[]{0x31,0x31,0x31});
			bytes = PosSdkJni.getDLPackage((byte)0x21, (byte)0x10, (byte)0x10, (byte)0x3C);
			System.out.println(Util.bytes2Hex(bytes));
			
			PosSdkJni.setTck(new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,});
			System.out.println(Util.bytes2Hex(PosSdkJni.getTck()));
			
			PosSdkJni.setTck(new byte[]{0x00,0x00,0x00,0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,});
			System.out.println(Util.bytes2Hex(PosSdkJni.calcMAC(new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,})));
			
			bytes = PosSdkJni.getDLPackage((byte)0x21, (byte)0x10, (byte)0x00, (byte)0x0A);
			bytes = new byte[]{0x4D, 0x00, 0x1F, 0x24, 0x10, 0x00, 0x00, 0x00, 0x19, 0x08, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x0A, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7D};
			for (byte b : bytes) {
				PosSdkJni.onChar(b);
			}
			System.out.println(PosSdkJni.isPackageRreceiveComplete());
			System.out.println("PosId:" + Util.bytes2Hex(PosSdkJni.getBykey("PosId")));
			System.out.println("PsamId:" + Util.bytes2Hex(PosSdkJni.getBykey("PsamId")));
			System.out.println("CmdId:" + Util.bytes2Hex(PosSdkJni.getBykey("CmdID")));
			setMasterKey("9B3A7B883A100F739B3A7B883A100F73","82E13665B4624DF5",0);
//			udpateWorkKey("DFEA613760EF4B8B7D538741B2C509E5","6C4E2C799D3B0EDF","CD0FE2DB34AE5C9C3BE9F3F6D83F1738","DF4F6D0A5A96FB82","CD0FE2DB34AE5C9C3BE9F3F6D83F1738","DF4F6D0A5A96FB82",0,20);
			
	}
	
	public void mytt(){
//		System.out.println(System.getProperty("java.library.path"));
    	Enumeration comList = null;
        CommPortIdentifier portId = null;
        SerialPort serialPort = null;
       
        comList = CommPortIdentifier.getPortIdentifiers();
        while (comList.hasMoreElements()) {
        	portId = (CommPortIdentifier) comList.nextElement();
        	if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        		System.out.println("portID:"+portId.getName());
        		 if (portId.getName().equals("COM4")) {
        			  try {
						serialPort = (SerialPort)portId.open("PcSDKJavaDemo", 2000);
						output = serialPort.getOutputStream();
						input = serialPort.getInputStream();
						serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                      System.out.println(serialPort);
        		 }
        	}
        }
	}
	
	public void setMasterKey(String keyString, String checkValue,int keyIndex) throws Exception{
		String mastrkeyStr=keyString+checkValue+"0" + keyIndex;
		CommandDownlink dc = new CommandDownlink(0x10, 0xe2, 20, Util.HexStringToByteArray(mastrkeyStr));
		System.out.println("write masterKey:"+Util.byteArray2Hex(dc.getBytes()));
		mytt();
		transmit(output, input, dc.getBytes());
	}
	
	public void udpateWorkKey(String pik, String pikCheck, String trk,
			String trkCheck, String mak, String makCheck,int keyIndex,int timeout) {
		String str = "";
		int pikkLen = 0;
		if ((pik != null && !"".equals(pik))
				&& (pikCheck != null && !"".equals(pikCheck))) {
			pikkLen = pik.length() + pikCheck.length();
			pikkLen = pikkLen / 2;
		} else {
			pik = "";
			pikCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) pikkLen }) + pik
				+ pikCheck;

		int trkLen = 0;
		if ((trk != null && !"".equals(trk))
				&& (trkCheck != null && !"".equals(trkCheck))) {
			trkLen = trk.length() + trkCheck.length();
			trkLen = trkLen / 2;
		} else {
			trk = "";
			trkCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) trkLen }) + trk
				+ trkCheck;

		int makLen = 0;
		if ((mak != null && !"".equals(mak))
				&& (makCheck != null && !"".equals(makCheck))) {
			makLen = mak.length() + makCheck.length();
			makLen = makLen / 2;
		} else {
			mak = "";
			makCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) makLen }) + mak
				+ makCheck;
		updateFirmwareStr=str+ "0" + keyIndex;
		setUpdateKey(Util.HexStringToByteArray(updateFirmwareStr), timeout);
	}
	
	public void setUpdateKey(byte[] cmdBytes, int udpateWorkKey_timeout){
		CommandDownlink dc = new CommandDownlink(0x10, 0xf0, udpateWorkKey_timeout, cmdBytes);
		System.out.println("write updateKey:"+Util.byteArray2Hex(dc.getBytes()));
		mytt();
		try {
			transmit(output, input, dc.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void transmit(OutputStream output,InputStream input,byte[] packat) throws Exception {
		output.write(packat);
		byte[] tmp = new byte[1024];
		int len  = 0;
		byte[] response;
		System.out.println("Write To Com:" + Util.bytes2Hex(packat));
        do {
        	int ret = input.read();
        	if(ret == -1){
        		break;
        	}
        	byte b = (byte) ret;                    	
        	PosSdkJni.onChar(b);
        	tmp[len ++] = b;
        	System.out.println("b+++:"+Util.byte2Hex(b));
        	System.out.println("Complete:" + PosSdkJni.isPackageRreceiveComplete());
		} while (PosSdkJni.isPackageRreceiveNotComplete());
        response = new byte[len];
        System.arraycopy(tmp, 0, response, 0, len);
        System.out.println("Read From Com:" + Util.byteArray2Hex(response));
        if(Util.byteArray2Hex(response).substring(12,14).equals("08")){
        	System.out.println("update keys failed!");
        }else if(Util.byteArray2Hex(response).substring(12,14).equals("00")){
        	System.out.println("update keys success!");
        }
	}
	
}
