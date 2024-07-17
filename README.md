# JQsim is a simple quantum simulator code exploration

The project started off as a system based on the below article conceptually, but it is implemented  
using Java instead of Racket and is a more complex implementation than the article provides for.  
https://medium.com/@sourav_datta/build-a-quantum-computing-simulator-from-scratch-part-1-a8d9a9d93fba

Currently I have shifted from a per qubit state tracker to a single state vector representing  
the state of the entire system. No measurements are taken along the process, preserving all  
information until the final measurement, as a real QC does. The computational basis states are  
observable as gates are added.

Practical upper limit for qubit creation at this point is ~14 qubits, which consumes roughly  
30gb of RAM. This will be an item to address in the future and eliminate the rampant memory consumption.  
The groundwork is created that will implement sparse storage of the state vector and implement  
sparse matrix calculations to reduce memory overhead, will be implemented when all other vector and  
matrix operations provide correct outputs.

## Phase 1:
&#x2705; Initial implementation to ensure flexibility for any practical number of qubits.  
&#x2705; Implement Clifford gates. 	

## Phase 2:
&#x2705; Implementing entanglement functionality via CNOT and Hadmard.

## Phase 3: 
&#x2705; Implement a circuit builder to queue gates to apply to each qubit.   
&#x2705; ~~Finish the Qops class to run the work queue and generate the expectation values.~~

## Phase 4:  
&#x2705;Rework the design to use a single state vector for the system representing all qubit states.  
and compose operator matrices based on the tensor product of the correct inner products of each qubit state in the  
state vector of the system.  
&#x2705; Rework CNOT to be mathematically correct to CX 0,1 and CX 1,0 differences.  
&#x2705;Expand gate functionality by implementing phase shifts, rotation gates, and composed gates from other gates.  

## Phase 5: ***Currently Working***
&#x2705;Ensure control gates apply correctly across superposition system states when multiple gates are applied, preserving  
both amplitude and phase of each qubit.    
&#x274C;Continue implementing the more complex gates, e.g. RXX, QFT, etc.  
&#x274C;Implement simulatnious measurment of each qubit and implement measurement of single qubit and cast result into  
a classical bit.  
&#x274C;Implement deriving expectation values for the whole system upon simulated system collapse.

## Phase 6:
&#x274C;Convert all outputs to JSON and prepare for front end construction.  
&#x274C;Build the front end (oh boy!)

&#x274C;Implement a decomposer using a TopSort for possible gate compositions to achieve a resultant set of expectation values.
