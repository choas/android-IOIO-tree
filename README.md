IOIO Tree
=========

slides: http://www.slideshare.net/LarsGregori/connecting-electronic-to-an-android



about remote debugging

- Connect device via USB and make sure debugging is working.
- adb tcpip 5555
- adb connect <DEVICE_IP_ADDRESS>:5555
- Disconnect USB and proceed with wireless debugging.
- adb -s <DEVICE_IP_ADDRESS>:5555 usb to switch back when done.

http://stackoverflow.com/a/10236938/3895023



Image:

"Ñooñ" by Pedro1267 - Own work. Licensed under Creative Commons Attribution-Share Alike 3.0-2.5-2.0-1.0 via Wikimedia Commons - http://commons.wikimedia.org/wiki/File:%C3%91oo%C3%B1.png#mediaviewer/File:%C3%91oo%C3%B1.png
