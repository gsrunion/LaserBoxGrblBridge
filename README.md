# LaserBoxGrblBridge
A Grbl facade for the API exposed by MakeBlock C02 Lasers

MakeBlocks C02 lasers come with a proprietary software called LaserBox for CAD (design) and CAM (toolpath generation). The provided software is weak on both fronts and the ecosystem would very much benefit from being able to run the machine from commodity laser control software such as LightBurn. This budding project aims provide a networked interface that emulates a GRBL machine and forward translated commands on to the LaserBox in the dialect of gcode that it speaks using the HTTP interface that it exposes.
