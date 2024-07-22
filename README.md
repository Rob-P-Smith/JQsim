# JQsim is a *simple* quantum simulator code exploration which may evolve into a publicly available quantum simulation Java library.

[1. About](#about)  
[2. Progress](#progress)  
[3. Use](#use)  
[4. Known Issues](#issues)

# Use  
JQsim uses a layered abstraction design to present the user with a simply syntax, inspired by the popular Qiskit syntax, streamlining the Java specific code naunce for a better user experience.   
Similar to Qiskit, JQsim requires a setup line at the head of the users file for example:  

**jqs jqs = new jqs(n); n = number of qubits in system**  

This will setup the backend and build a state vector with the MSB set to 1, while all other values are set to 0, e.g. a zero initialized 
quantum state vector.  

Following instantiation and setup above, the user can add gates intuitively by using the syntax:  

**<your_jqs_object_name>.<gate_abbreviation>(target);**  

Example Pauli-X on the above targeting qubit 0 and Hadmard on qubit 1:  

**jqs.X(0);**  
**jqs.H(1);**  

This does not execute any calculation, as that is left to the user to pick whawt type of calculation is done.  
getComputationalState() - This method simply calculates the current state vector values. It has no return. To print your system computational basis states
a user would need to invoke a connsole print, e.g.:  

**jqs.getComputationalState();**
**System.out.println(jqs);**  

This will display the current system state in Dirac notation, like this:  

**|ψ⟩ = 0.707|01⟩, 0.707|11⟩**  

For more complex circuits, the measure(n) or M(n) function options exist where n is the target qubit to measure. If the amplitude is not 0.0 or 1.0
measure or M will probabilistically determine the value and set the state vector value to the appropriate value based on that determination. This is a single
'shot' determination. Useful in cases like quantum teleportation, where the goal is to transfer the value to the ancillary qubit and that behavior can be observed 
by setting up a system with  
**jqs jqs = new jqs(3);**  
**jqs.X(0);**  
**jqs.H(1);**  
**jqs.CX(1,2);**  
**jqs.H(0);**  
**jqs.CX(0,1);**  
**jqs.measure(0);**  
**jqs.measure(1);**  
**jqs.CX(1,2);**  
**jqs.CZ(0,2);**
**jqs.measure(2);**  

When using this the output for the system state if a user prints it will be one of these four states with equal probabilities based on the amplitudes of 0.500 for each:  
|ψ⟩100  
|ψ⟩101  
|ψ⟩110  
|ψ⟩111  

This measure function is akin to Qiskit's measure function, but only show a single determined output state. To get the probabilities for each state, the simulate() method should
be used instead. simulate() will calculate and print out all valid basis states after applying the circuit and the probability of each occuring.  

Single qubit gates available are:  
<ol>
  <li>Hadmard Gate</li>
  <ul>
    <li>The H gate is invoked with jqs.H(n) where n is the target qubit.</li>
  </ul>
  <li>Pauli-X</li>
  <ul>
     <li>The X gate is invoked with jqs.X(n) where n is the target qubit.</li>
    <\ul>
  <li>Pauli-Y</li>
      <ul>
     <li>The Y gate is invoked with jqs.Y(n) where n is the target qubit.</li>
    <\ul>
  <li>Puali-Z</li>
        <ul>
     <li>The Z gate is invoked with jqs.Z(n) where n is the target qubit.</li>
    <\ul>
  <li>Rotation X</li>
  <li>Rotation Y</li>
  <li>Rotation Z</li>
  <li>Rotation Phase</li>
  <li>Phase Gate S</li>
  <li>Phase Gate Si</li>
  <li>Phase Gate T</li>
  <li>Phase Gate Ti</li>
  <li>Identity</li>
</ol>  

# About  

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

# Progress

## Phase 1:
&#x2705; Initial implementation to ensure flexibility for any practical number of qubits.  
&#x2705; Implement Clifford gates. 	

## Phase 2:
&#x2705; Implementing entanglement functionality via CNOT and Hadmard.

## Phase 3: 
&#x2705; Implement a circuit builder to queue gates to apply to each qubit.   
&#x2705; ~~Finish the Qops class to run the work queue and generate the expectation values.~~  Removed due to system design change.  

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
&#x274C;Do a careful and thorough code review to clean up and restructure for efficiency and maintainability before freezing to start  
front end construction.  
&#x274C;Convert all outputs to JSON and prepare for front end construction.  
&#x274C;Build the front end (oh boy!)

&#x274C;Implement a decomposer using a TopSort for possible gate compositions to achieve a resultant set of expectation values.  

# Issues  

