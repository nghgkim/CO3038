package bku.iot.iot_lab5;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;

    TextView txtTemp, txtLight;
    LabeledSwitch btnLED, btnCOND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemperature);
        txtLight = findViewById(R.id.txtLight);
        btnLED = findViewById(R.id.btnLED);
        btnCOND = findViewById(R.id.btnCOND);

        btnLED.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT("iumm0123/feeds/nutnhan1", "1");
                } else {
                    sendDataMQTT("iumm0123/feeds/nutnhan1", "0");
                }
            }
        });

        btnCOND.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT("iumm0123/feeds/nutnhan2", "1");
                } else {
                    sendDataMQTT("iumm0123/feeds/nutnhan2", "0");
                }
            }
        });

        startMQTT();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e){

        }
    }

    public void startMQTT() {
        mqttHelper = new MQTTHelper(this);

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if (topic.contains("cambien1")) {
                    txtTemp.setText(message.toString() + "°C");
                    int temp = Integer.parseInt(message.toString());
                    if (temp > 25) {
                        btnCOND.setOn(true);
                    } else {
                        btnCOND.setOn(false);
                    }
                } else if (topic.contains("cambien2")) {
                    txtLight.setText(message.toString() + "lux");
                    int light = Integer.parseInt(message.toString());
                    if (light < 200) {
                        btnLED.setOn(true);
                    } else {
                        btnLED.setOn(false);
                    }
                } else if (topic.contains("nutnhan1")) {
                    btnLED.setOn(message.toString().equals("1"));
                } else if (topic.contains("nutnhan2")) {
                    btnCOND.setOn(message.toString().equals("1"));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}