# Bluetooth Classic SPP automation

The app accepts a single Bluetooth Classic SPP send command through an explicit broadcast. It
connects to the supplied device and UUID, writes the decoded bytes unchanged, and remains in a
foreground service with a persistent notification so the socket can stay connected after the
activity is backgrounded.

## Broadcast contract

- Action: `com.eva.bluetoothterminalapp.action.SPP_SEND`
- Target package: `com.eva.bluetoothterminalapp` (use the package matching the installed build;
  debug builds add `.debug`)
- `device_address`: Bluetooth MAC address in `XX:XX:XX:XX:XX:XX` format
- `spp_uuid`: canonical UUID string, such as `00001101-0000-1000-8000-00805f9b34fb`
- `bytes_payload`: optional raw `ByteArray` payload (automation tools may not support this extra
  type)
- `hex_payload`: non-empty, even-length hexadecimal string; spaces and prefixes are not accepted

Example:

```sh
adb shell am broadcast --user 0 \
  -n com.eva.bluetoothterminalapp/.data.services.SPPConnectionReceiver \
  -a com.eva.bluetoothterminalapp.action.SPP_SEND \
  --es device_address 01:23:45:67:89:AB \
  --es spp_uuid 00001101-0000-1000-8000-00805f9b34fb \
  --es hex_payload 48656C6C6F0D0A
```

Tasker/MacroDroid should send a broadcast to the same package/component and action, with
`device_address`, `spp_uuid`, and either `hex_payload` or `bytes_payload`. The receiver validates
the address, UUID, and non-empty payload before starting the service; the service validates again
before attempting a Bluetooth connection. Bluetooth Connect permission must already be granted;
Android foreground-service launch restrictions still apply.

The receiver is exported to allow external automation tools to reach it. Do not treat this broadcast
as an authenticated interface: another installed app can attempt to send it, so only use trusted
automation profiles and avoid sensitive payloads.

## Platform limitation

Android does not expose ordinary third-party applications to a supported API for forcibly closing
another application's Bluetooth socket. This automation command does not attempt to disconnect or
kick another app; it only opens and owns this app's SPP connection. Android may arbitrate concurrent
Bluetooth connections according to device and profile behavior.