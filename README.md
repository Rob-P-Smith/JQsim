# JQsim is a simple quantum simulator code exploration

It is based on the below article conceptually, but it is implemented using Java instead of Racket and is a more
complex implementation than the article provides for.

https://medium.com/@sourav_datta/build-a-quantum-computing-simulator-from-scratch-part-1-a8d9a9d93fba

## Phase 1:
~~Initial implementation to ensure flexibility for any practical number of sandbox.~~

~~Implementing Clifford gates~~

## Phase 2:
~~Implementing entanglement functionality via CNOT~~

## Phase 3: 
~~Implement a circuit builder to queue gates to apply to each qubit~~

~~Finish the Qops class to run the work queue and generate the expectation values~~

## Phase 4:  ***Currently Working***
~~Rework CNOT to be mathmatically correct to CX 0,1 and CX 1,0 differences~~

Rework the wire design to use a single queue for all qubits and maintain order by adding to queue in 
order the user adds the gates, eliminating the for identity on non-active qubits.

Expand gate functionality by implementing phase shifts, rotation gates, and composed gates from other gates

## Phase 5:
Convert all outputs to JSON and prepare for front end construction

## Phase 6:
Build the front end (oh boy!)

Implement a decomposer using a TopSort for possible gate compositions to achieve a resultant set of expectation values
