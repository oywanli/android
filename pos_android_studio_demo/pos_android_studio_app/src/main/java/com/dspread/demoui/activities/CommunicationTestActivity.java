package com.dspread.demoui.activities;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.dspread.demoui.beans.ConstantsBean;
import com.dspread.demoui.utils.TRACE;
import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;

import java.util.Hashtable;

/**
 * @author Qianmeng
 */
public class CommunicationTestActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_blu_led;
    private Button btn_yel_led;
    private Button btn_buzzer;
    private Button btn_mcr;
    private Button btn_psam;
    private Button btn_red_led;
    private Button btn_led_gre;
    private Button btn_icc;
    private Button btn_nfc;
    private Button btn_get_version;
    private Button btn_hardware_check;
    private Button btn_led;
    private Button btn_reset_pos;
    private TextView txt_result;
    private boolean flagLed = false;
    private boolean flagBuzzer = false;
    private QPOSService pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = ConstantsBean.pos;
        setTitle(getString(R.string.communicate_test));
        initView();
        initData();
    }

    @Override
    public void onToolbarLinstener() {
        finish();
        pos.closeUart();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_communication_test;
    }

    private void initView(){
        btn_led = findViewById(R.id.btn_led);
        btn_blu_led = findViewById(R.id.btn_blu_led);
        btn_yel_led = findViewById(R.id.btn_yel_led);
        btn_buzzer = findViewById(R.id.btn_buzzer);
        btn_mcr = findViewById(R.id.btn_mcr);
        btn_psam = findViewById(R.id.btn_psam);
        btn_red_led = findViewById(R.id.btn_red_led);
        btn_led_gre = findViewById(R.id.btn_led_gre);
        btn_icc = findViewById(R.id.btn_icc);
        btn_nfc = findViewById(R.id.btn_nfc);
        btn_get_version = findViewById(R.id.btn_get_version);
        btn_hardware_check = findViewById(R.id.btn_hardware_check);
        txt_result = findViewById(R.id.txt_result);
        btn_reset_pos = findViewById(R.id.btn_reset_pos);
    }

    private void initData(){
        open();
        btn_led.setOnClickListener(this);
        btn_blu_led.setOnClickListener(this);
        btn_yel_led.setOnClickListener(this);
        btn_buzzer.setOnClickListener(this);
        btn_mcr.setOnClickListener(this);
        btn_psam.setOnClickListener(this);
        btn_red_led.setOnClickListener(this);
        btn_led_gre.setOnClickListener(this);
        btn_icc.setOnClickListener(this);
        btn_nfc.setOnClickListener(this);
        btn_get_version.setOnClickListener(this);
        btn_hardware_check.setOnClickListener(this);
        btn_reset_pos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_led:
                //test LED
                if(btn_led.getText().toString().equals(getString(R.string.test_led_start))){
                    pos.testPosFunctionCommand(20, QPOSService.TestCommand.LED_TEST_START);
                    btn_led.setText(getString(R.string.test_led_end));
                }else if(btn_led.getText().toString().equals(getString(R.string.test_led_end))){
                    btn_led.setText(getString(R.string.test_led_start));
                    pos.testPosFunctionCommand(20, QPOSService.TestCommand.LED_TEST_STOP);
                }
                break;
            case R.id.btn_buzzer:
                //test buzzer
                if(btn_buzzer.getText().toString().equals(getString(R.string.test_buzzer_start))){
                    pos.testPosFunctionCommand(20, QPOSService.TestCommand.BUZZER_TEST_START);
                    btn_buzzer.setText(getString(R.string.test_buzzer_end));
                }else if(btn_buzzer.getText().toString().equals(getString(R.string.test_buzzer_end))){
                    btn_buzzer.setText(getString(R.string.test_buzzer_start));
                    pos.testPosFunctionCommand(20, QPOSService.TestCommand.BUZZER_TEST_STOP);
                }
                break;
            case R.id.btn_psam:
                break;
            case R.id.btn_icc:
                //test ICC card
                txt_result.setText(getString(R.string.msg_pls_insert_card));
                pos.testPosFunctionCommand(20, QPOSService.TestCommand.ICC_TEST);
                break;
            case R.id.btn_mcr:
                //test MCR card
                txt_result.setText(getString(R.string.msg_pls_swipe_card));
//                pos.testPosFunctionCommand(20, QPOSService.TestCommand.MRC_TEST);
                break;
            case R.id.btn_nfc:
                //Test NFC card
                txt_result.setText(getString(R.string.msg_pls_tap_card));
                pos.testPosFunctionCommand(20, QPOSService.TestCommand.NFC_TEST);
                break;
            case R.id.btn_get_version:
                //qpos don't support it now, so can not use
                pos.doPosSelfTest(20, QPOSService.PosSelfTestCommand.GET_VERSION);
                break;
            case R.id.btn_hardware_check:
                break;
            case R.id.btn_reset_pos:
                //reset the qpos status
                pos.resetPosStatus();
                break;
            default:
                break;
        }
    }

    /**
     * Init the pos instance
     */
    private void open() {
        TRACE.d("open");
        MyQposClass listener = new MyQposClass();
        pos = QPOSService.getInstance(QPOSService.CommunicationMode.UART);
        if (pos == null) {
            txt_result.setText("CommunicationMode unknow");
            return;
        }
        pos.setConext(this);
        //通过handler处理，监听MyPosListener，实现QposService的接口，（回调接口）
        Handler handler = new Handler(Looper.myLooper());
        pos.initListener(handler, listener);
        String blueTootchAddress = "/dev/ttyS1";//同方那边是s1，天波是s3 ttyHSL1
        pos.setDeviceAddress(blueTootchAddress);
        pos.openUart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pos.closeUart();
    }

    /**
     * The callback of all method
     */
    class MyQposClass extends CQPOSService {
        @Override
        public void onRequestQposConnected() {
            super.onRequestQposConnected();
            Toast.makeText(CommunicationTestActivity.this, "onRequestQposConnected", Toast.LENGTH_LONG).show();
            txt_result.setText(getString(R.string.device_plugged));
        }

        @Override
        public void onRequestQposDisconnected() {
            super.onRequestQposDisconnected();
            txt_result.setText(getString(R.string.device_unplugged));
        }

        @Override
        public void onRequestNoQposDetected() {
            super.onRequestNoQposDetected();
        }

//        @Override
//        public void onQposTestCommandResult(boolean isSuccess) {
//            super.onQposTestCommandResult(isSuccess);
//            txt_result.setText(getString(R.string.result)+" "+isSuccess);
//        }

        @Override
        public void onQposTestSelfCommandResult(boolean isSuccess, String datas) {
            super.onQposTestSelfCommandResult(isSuccess, datas);
            txt_result.setText(getString(R.string.result)+" "+datas);
        }

        @Override
        public void onError(QPOSService.Error errorState) {
            super.onError(errorState);
            if (errorState == QPOSService.Error.CMD_NOT_AVAILABLE) {
                txt_result.setText(getString(R.string.command_not_available));
            } else if (errorState == QPOSService.Error.TIMEOUT) {
                txt_result.setText(getString(R.string.device_no_response));
            } else if (errorState == QPOSService.Error.DEVICE_RESET) {
                txt_result.setText(getString(R.string.device_reset));
            } else if (errorState == QPOSService.Error.UNKNOWN) {
                txt_result.setText(getString(R.string.unknown_error));
            } else if (errorState == QPOSService.Error.DEVICE_BUSY) {
                txt_result.setText(getString(R.string.device_busy));
            } else if (errorState == QPOSService.Error.INPUT_OUT_OF_RANGE) {
                txt_result.setText(getString(R.string.out_of_range));
            } else if (errorState == QPOSService.Error.INPUT_INVALID_FORMAT) {
                txt_result.setText(getString(R.string.invalid_format));
            } else if (errorState == QPOSService.Error.INPUT_ZERO_VALUES) {
                txt_result.setText(getString(R.string.zero_values));
            } else if (errorState == QPOSService.Error.INPUT_INVALID) {
                txt_result.setText(getString(R.string.input_invalid));
            } else if (errorState == QPOSService.Error.CASHBACK_NOT_SUPPORTED) {
                txt_result.setText(getString(R.string.cashback_not_supported));
            } else if (errorState == QPOSService.Error.CRC_ERROR) {
                txt_result.setText(getString(R.string.crc_error));
            } else if (errorState == QPOSService.Error.COMM_ERROR) {
                txt_result.setText(getString(R.string.comm_error));
            } else if (errorState == QPOSService.Error.MAC_ERROR) {
                txt_result.setText(getString(R.string.mac_error));
            } else if (errorState == QPOSService.Error.APP_SELECT_TIMEOUT) {
                txt_result.setText(getString(R.string.app_select_timeout_error));
            } else if (errorState == QPOSService.Error.CMD_TIMEOUT) {
                txt_result.setText(getString(R.string.cmd_timeout));
            } else if (errorState == QPOSService.Error.ICC_ONLINE_TIMEOUT) {
                if (pos == null) {
                    return;
                }
                pos.resetPosStatus();
                txt_result.setText(getString(R.string.device_reset));
            }
        }
    }
}
