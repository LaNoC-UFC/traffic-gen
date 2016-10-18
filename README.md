TrafficGenerator for NoCs
================

traffic-gen is a tool capable of generate traffic (as text files)
that act as input to mesh-like (2D) networks-on-chip of any size.
It's mainly based on the [Atlas Performance Evaluation tool](https://corfu.pucrs.br/redmine/projects/atlas/wiki/Performance_Evaluation).

The networks currently supported are [Phoenix](https://github.com/LaNoC-UFC/Phoenix) and [Thor](https://github.com/LaNoC-UFC/NoCThor).

## Temporal distribution
Currently only uniform temporal distribution is supported.
## Spacial distribution
It can generate traffic with these synthetic spacial distributions:
* uniform (random)
* bit reversal
* butterfly
* perfect shuffle
* matrix transpose
* complement
* hotspot

Some distributions will only work for square networks (due their very definition).
## Output format
traffic-gen will generate a text file to each PE in the network.
Each file will be named after the address of its PE.
Each file's line represent a package with its timestamp (in hexadecimal).
### File name
The address size will depend on the flit size.
We use the most significant half flit as the X coordinate
and the less significant half flit as the Y coordinate.
This is the same format present on the package header to indicate its destination.
The address is represented on hexadecimal.

Example:  
The file generated to the PE with coordinates x = 1 and y = 2 and with a flit size of 16 bits will be named *0102.txt*.
### File format
Inside a generated file each line presents a package's timestamp followed by the package itself.
The package's timestamp is the clock cycle that this package should enter in the network.
### Package format
A package is composed of flits (*flow-control units*) separated by spaces and represented on hexadecimal.

The package header is composed by the two first flits:
the 1st flit is the package destination address
and the 2nd is the package payload size.

The package payload doesn't need to have any special meaning, but we use it to ease our analysis.
So, the 3rd flit represents the source address,
the 4th through the 7th flit represent the package timestamp,
and the 8th and 9th flits represent the package ID.

If the total package size (header + payload) is greater than 9 the traffic injector will have to pad the package.
The traffic injector can (and we encourage to) use these extra flits to add useful data (relevant to the simulation details)
specially if the traffic analysis tool that you are planning to use expects a given format.

## Related resources
* [Phoenix](https://github.com/LaNoC-UFC/Phoenix)
* [Thor](https://github.com/LaNoC-UFC/NoCThor)
* [traffic-eval](https://github.com/LaNoC-UFC/traffic-eval)
* [Atlas](https://corfu.pucrs.br/redmine/projects/atlas)
