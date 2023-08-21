package com.example.despreaddemo.utils;


public class bluebean {

    private static String conected_state;
    private static String bulueName;

    public static String getBulueName() {
        return bulueName;
    }

    public static void setBulueName(String bulueName) {
        bluebean.bulueName = bulueName;
    }

    public static String getConected_state() {
        return conected_state;
    }

    public static void setConected_state(String conected_state) {
        bluebean.conected_state = conected_state;
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
