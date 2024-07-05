# JQsim is a simple quantum simulator code exploration

It is based on the below article conceptually, but it is implemented using Java instead of Racket and is a more
complex implementation than the article provides for.

https://medium.com/@sourav_datta/build-a-quantum-computing-simulator-from-scratch-part-1-a8d9a9d93fba

## Phase 1:
&#x2705; Initial implementation to ensure flexibility for any practical number of qubits.  
&#x2705; Implement Clifford gates 	

## Phase 2:
&#x2705; Implementing entanglement functionality via CNOT and Hadmard

## Phase 3: 
&#x2705; Implement a circuit builder to queue gates to apply to each qubit   
&#x2705; Finish the Qops class to run the work queue and generate the expectation values 

## Phase 4:  ***Currently Working***
&#x2705;Rework the design to use a single state vector for the system representing all qubit states  
and compose operator matrices based on tensor producting the correct inner products of each qubit state in the  
state vector of the system.  
&#x274C; Rework CNOT to be mathmatically correct to CX 0,1 and CX 1,0 differences  
&#x274C;Expand gate functionality by implementing phase shifts, rotation gates, and composed gates from other gates

## Phase 5:
&#x274C;Convert all outputs to JSON and prepare for front end construction

## Phase 6:
&#x274C;Build the front end (oh boy!)

&#x274C;Implement a decomposer using a TopSort for possible gate compositions to achieve a resultant set of expectation values
