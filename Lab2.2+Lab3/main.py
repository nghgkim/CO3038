import sys
from Adafruit_IO import MQTTClient
import time
import random
# from simple_ai import *
from uart import *

AIO_FEED_ID = ["nutnhan1", "nutnhan2"]
AIO_USERNAME = "nghgkim0412"
AIO_KEY = "aio_oYDB31F26RVc7aDghD3VDo9XGGj4"

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_ID:
        client.subscribe(topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload + ", feed id: " + feed_id)
    if feed_id == 'nutnhan1':
        if payload == "0":
            writeData("1")
        else:
            writeData("2")
    elif feed_id == 'nutnhan2':
        if payload == "0":
            writeData("3")
        else:
            writeData("4")

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
counter = 10
sensor_type = 0
counter_ai = 5
ai_result = ""
prev_result = ""

while True:
    # counter = counter - 1
    # if counter <= 0:
    #     counter = 10
    #     # TODO
    #     print("Random data is publishing ...")
    #     if sensor_type == 0:
    #         print("Temperature...")
    #         temp = random.randint(10, 20)
    #         client.publish("cambien1", temp)
    #         sensor_type = 1
    #     elif sensor_type == 1:
    #         print("Light...")
    #         light = random.randint(100, 500)
    #         client.publish("cambien2", light)
    #         sensor_type = 2
    #     elif sensor_type == 2:
    #         print("Humidity...")
    #         humi = random.randint(50,70)
    #         client.publish("cambien3", humi)
    #         sensor_type = 0
    
    # counter_ai = counter_ai - 1
    # if counter_ai <= 0:
    #     counter_ai = 5
    #     prev_result = ai_result
    #     ai_result = image_detector()
    #     print("AI Output: ", ai_result)
    #     if prev_result != ai_result:
    #         client.publish("ai", ai_result)
    
    readSerial(client)
    time.sleep(1)
    pass