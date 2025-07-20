# :large_blue_circle: Bluetooth Terminal App

This Bluetooth Android Terminal App facilitates interaction with Bluetooth and Bluetooth Low
Energy (BLE) devices.The app provides a user-friendly interface to manage connections, interactwith
devices.

## :information_desk_person: Description

The Bluetooth Android Terminal App supports both classic Bluetooth and Bluetooth Low Energy (BLE)
connections. It allows users to scan for available devices, establish connections, and communicate
with connected devices.
Additionally, the app includes a built-in server functionality for simple interactions between two
peers running the same app

## :performing_arts: Features

### Classic Bluetooth

- **Devices:** Display a list of paired devices and available unpaired devices.
- **Connect and Interact:** Allow the user to communicate with the connected device
- **Chat Server:** Start a server within the app to connect to other phones and interact with
  messages.
- **Settings:** You can check customize the connection terminal for clients and server.

### Bluetooth Low Energy (BLE)

- **Scan for Devices:** Scan for devices supporting bluetooth low energy
- **Services and Charateristics:** Display available services and characteristics for the connected
  device. Allow users to read ,write or observe values to the characteristics .
- **Settings:** You can customize scan settings for the app to discover your device.

## :camera_flash: Screenshots

These are some of the screens shots showing the working of classic bluetooth connection

<p align="center">
   <img src="screenshots/bt_classic_scan.png" width="20%" />
   <img src="screenshots/bt_peer_features.png" width="20%"/>
   <img src="screenshots/bt_client_talking.png" width="20%"/>  
   <img src="screenshots/bt_classic_settings.png" width="20%"/>
</p>

This screenshots shows the working of a bluetooth low energy device connection

<p align="center">
   <img src="screenshots/ble_devices_scanning.png" width="20%" />
   <img src="screenshots/ble_device_profile.png" width="20%"/>
   <img src="screenshots/ble_notify_running.png" width="20%"/>  
   <img src="screenshots/ble_settings.png" width="20%">
</p>

## :building_construction: Getting Started

Make sure the device supports **Bluetooth** and if possible **Bluetooth Low Energy** to check out
its functionalities

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/tuuhin/BTAndroidApp.git
   ```

2. **Open Project**
   Open the project in android studio

3. **Build and Run**
   Build and run on android device with api _29_ and above

### :curly_loop: Feedback and Support

If you encounter any problems or bugs in the app, please raise
an [issue](https://github.com/tuuhin/BTAndroidApp/issues/new)

### :man_cook: Contributing

Contributions are alaways welcomed from the community

- Fork the repository.
- Create your feature branch (git checkout -b feature/YourFeature).
- Commit your changes (git commit -am 'Add some feature').
- Push to the branch (git push origin feature/YourFeature).
- Create a new Pull Request.

## :end: Conclusiion

The Bluetooth Android Terminal App is a versatile tool for interacting with Bluetooth and BLE
devices. While the app aims to provide a seamless experience and was created to debug esp32 based
microcontroller which have bluetooth features.
These seems complete for now , but if someone encounter any issues or bugs please raise
an [issue](https://github.com/tuuhin/BTAndroidApp/issues/new)

### :revolving_hearts: Special Thanks

Special thanks two most used open sourced bluetooth terminal apps.

- [Nordic Semiconductor android app](https://github.com/NordicSemiconductor/Android-nRF-Connect)
- [Simple Bluetooth termial](https://github.com/kai-morich/SimpleBluetoothTerminal)
