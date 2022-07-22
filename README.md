# LaserBoxGrblBridge
A Grbl facade for the API exposed by MakeBlock C02 Lasers

MakeBlocks C02 lasers come with a proprietary software called LaserBox for CAD (design) and CAM (toolpath generation). The provided software is weak on both fronts and the ecosystem would very much benefit from being able to run the machine from commodity laser control software such as LightBurn. This budding project aims provide a networked interface that emulates a GRBL machine and forward translated commands on to the LaserBox in the dialect of gcode that it speaks using the HTTP interface that it exposes.

# Technical Details
- The machine exposes 3 different network interfaces
 - Wifi
 - Ethernet
 - USB. The usb connection to the machine is a USB ethernet adapter that takes HTTP requests at 201.234.3.1:8080

Once it was discovered that the USB connection was a USB -> Ethernet adapter a little network traffic sniffing with Wireshark showed that interaction between the LaserBox software was primarily HTTP calls and a WebSocket connection whos purpose is yet to be understood. The WireShark capture can be found [here](https://github.com/gsrunion/LaserBoxGrblBridge/blob/main/src/main/resources/laserbox%20capture.pcapng)

Traffic between the software and the machine was monitored over the course of several cuts and these calls were added to a PostMan collection ((https://github.com/gsrunion/LaserBoxGrblBridge/blob/main/src/main/resources/Laserbox.postman_collection.json)) to test that each command could be replayed. Some replay, some dont.

Looking at the installed artifacts for the LaserBox software it was apparent that the software was an Electron app. Electron applications are applications built using web technologies that are packaged up in a manner that they appear as native applications. That means it is built of JavaScript, HTML, CSS, and some WebAssembly. With the exception of the WebAssembly, all this resources are in stored in a plain text, human readable form and can be unpacked from the application and the source code reasoned about.

Between the source code analysis and review of the http traffic two very usefull HTTP calls were identified:

1) POST 201.234.3.1:8080/cnc/data?action=upload&zip=true

which is where the LaserBox software uploads a zip compressed gcode file to the machine to perform a cut

2) GET 201.234.3.1:8080/cnc/cmd?cmd=<gcode command>

which allows a single gcode command at a time to be sent to the machine.


Using (1) I collected 4 copies of the same design sent to the machine with different parameters to material thickens, power, and speed. These for gcode files are captures as text files here and will serve as a basis for reverse engineering the non-standard gcode the machine uses. (https://github.com/gsrunion/LaserBoxGrblBridge/tree/main/src/main/resources)






 
