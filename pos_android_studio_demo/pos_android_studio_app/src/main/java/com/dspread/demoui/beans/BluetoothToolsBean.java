package com.dspread.demoui.beans;


@SuppressWarnings("ALL")
public class BluetoothToolsBean {

    private static String conectedState;
    private static String blueName;

    public static String getBulueName() {
        return blueName;
    }

    public static void setBulueName(String bulueName) {
        BluetoothToolsBean.blueName = bulueName;
    }

    public static String getConectedState() {
        return conectedState;
    }

    public static void setConectedState(String conectedState) {
        BluetoothToolsBean.conectedState = conectedState;
    }
//    private static BluetoothTools.BluetoothConnectState connected_states = BluetoothTools.BluetoothConnectState.NOCONNECT;
//
//    public static BluetoothTools.BluetoothConnectState getConnected_state() {
//        return connected_states;
//    }
//
//    public static void setConnected_state(BluetoothTools.BluetoothConnectState connected_state) {
//        connected_states = connected_state;
//    }

}
