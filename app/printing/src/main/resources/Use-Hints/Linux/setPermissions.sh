# This script copies the required files and sets the permissions on the Serial converter

sudo cp usbRules.rules /etc/udev/rules.d/
sudo chmod 666 /dev/ttyUSB0