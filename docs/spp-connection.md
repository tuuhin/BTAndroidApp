# Bluetooth Classic SPP automation

This guide describes sending bytes over Bluetooth Classic Serial Port Profile (SPP) using the
app's broadcast receiver. The receiver starts a foreground service, which connects to the requested
device and UUID, writes the payload bytes unchanged, and retains the connection for subsequent
sends. Matching sends reuse the connection. To switch device or UUID, disconnect first.

## Commands

### Send data

Send an explicit broadcast using action `com.eva.bluetoothterminalapp.action.SPP_SEND` with these
extras:

| Extra            | Type      | Required             | Value                                                                                                                                           |
|------------------|-----------|----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `device_address` | String    | Yes                  | Bluetooth MAC address in `XX:XX:XX:XX:XX:XX` format, for example `01:23:45:67:89:AB`.                                                           |
| `spp_uuid`       | String    | Yes                  | UUID accepted by the app, commonly the SPP UUID `00001101-0000-1000-8000-00805f9b34fb`.                                                         |
| `hex_payload`    | String    | One payload required | Non-empty, even number of hexadecimal characters, e.g. `48656C6C6F0D0A`. No spaces or `0x` prefix. Each pair of characters represents one byte. |
| `bytes_payload`  | ByteArray | One payload required | Raw bytes. Use this when the automation tool can create a byte-array extra.                                                                     |

Provide one payload extra. If both are supplied, `bytes_payload` is used. Empty payloads, malformed
addresses, invalid UUIDs, and invalid hex are rejected. The app does not append a terminator, encode
text, or otherwise modify the bytes; include any protocol-required line ending yourself (for
example, `0D0A` for CRLF).

Example using ADB and a hex payload:

```sh
adb shell am broadcast --user 0 \
  -n com.eva.bluetoothterminalapp/.data.services.SPPConnectionReceiver \
  -a com.eva.bluetoothterminalapp.action.SPP_SEND \
  --es device_address 01:23:45:67:89:AB \
  --es spp_uuid 00001101-0000-1000-8000-00805f9b34fb \
  --es hex_payload 48656C6C6F0D0A
```

Replace the package/component with the application ID for the installed build if it differs.
Tasker, MacroDroid, or another caller should target the app's
`.data.services.SPPConnectionReceiver` component (or package) and provide the same action and
extras. Prefer hex when the automation tool cannot reliably send a `ByteArray`.

### Disconnect

Send action `com.eva.bluetoothterminalapp.action.SPP_DISCONNECT` to the same receiver. No extras
are needed. ADB example:

```sh
adb shell am broadcast --user 0 \
  -n com.eva.bluetoothterminalapp/.data.services.SPPConnectionReceiver \
  -a com.eva.bluetoothterminalapp.action.SPP_DISCONNECT
```

The ongoing notification also has a **Disconnect** action. Either method closes this app's SPP
session and stops its foreground service; it cannot close a Bluetooth socket owned by another app.

## Results and connection lifecycle

The receiver validates send arguments before starting the service. An ordered broadcast caller can
read an immediate canceled result and error message for invalid arguments or a foreground-service
start failure. A successful receiver result only means the command was accepted for service
processing; connecting and writing happen asynchronously and are not reported as a final delivery
result through the broadcast. Check the ongoing notification for connection/send status or failure.

The first send establishes a connection; later sends with the same device address and UUID reuse
it. A send for a different address or UUID fails and requires an explicit disconnect before
retrying. A connection/send failure closes the session. On Android versions that restrict
foreground-service starts from the background, bring the app to the foreground and retry if the
caller reports that the start is not allowed.

Bluetooth Connect permission must be granted before sending. The receiver is exported so external
automation can invoke it; it is not an authenticated interface. Only use trusted automation
profiles and avoid sending sensitive payloads.

## Platform limitation

Android does not expose ordinary third-party applications to a supported API for forcibly closing
another application's Bluetooth socket. This automation command does not attempt to disconnect or
kick another app; it only opens and owns this app's SPP connection. Android may arbitrate concurrent
Bluetooth connections according to device and profile behavior.