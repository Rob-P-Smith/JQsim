package state;

/**
 * Represents a work item with an operator and associated control and target values.
 * This class can handle both single control/target and multiple controls/targets scenarios.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 4 July 2024
 */
public class WorkItem {
    private String operator;
    private int control;
    private int target;
    private Integer[] controls;
    private Integer[] targets;
    private boolean singleQubit;

    /**
     * Constructs a WorkItem with a single control and target.
     *
     * @param operator The operator string.
     * @param target The target value.
     * @see #WorkItem(String, int)
     */
    public WorkItem(String operator, int target) {
        this.operator = operator;
        this.target = target;
        this.singleQubit = true;
    }

    /**
     * Constructs a WorkItem with multiple controls and targets.
     *
     * @param operator The operator string.
     * @param controls An array of control values.
     * @param targets An array of target values.
     * @see #WorkItem(String, Integer[], Integer[])
     */
    public WorkItem(String operator, Integer[] controls, Integer[] targets) {
        this.operator = operator;
        this.controls = controls;
        this.targets = targets;
        this.singleQubit = false;
    }

    /**
     * Gets whether this work item is a single qubit gate.
     * @return boolean true if single qubit gate, false if not.
     */
    public boolean isSingleQubit() {
        return singleQubit;
    }

    /**
     * Gets the operator.
     *
     * @return The operator string.
     * @see #setOperator(String)
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the single control value.
     *
     * @return The control value.
     * @see #setControl(int)
     * @see #getControls()
     */
    public int getControl() {
        return control;
    }

    /**
     * Gets the single target value.
     *
     * @return The target value.
     * @see #setTarget(int)
     * @see #getTargets()
     */
    public int getTarget() {
        return target;
    }

    /**
     * Gets the array of control values.
     *
     * @return The array of control values.
     * @see #setControls(Integer[])
     * @see #getControl()
     */
    public Integer[] getControls() {
        return controls;
    }

    /**
     * Gets the array of target values.
     *
     * @return The array of target values.
     * @see #setTargets(Integer[])
     * @see #getTarget()
     */
    public Integer[] getTargets() {
        return targets;
    }

    /**
     * Sets the operator.
     *
     * @param operator The new operator string.
     * @see #getOperator()
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Sets the single control value.
     *
     * @param control The new control value.
     * @see #getControl()
     */
    public void setControl(int control) {
        this.control = control;
    }

    /**
     * Sets the single target value.
     *
     * @param target The new target value.
     * @see #getTarget()
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * Sets the array of control values.
     *
     * @param controls The new array of control values.
     * @see #getControls()
     * @see #setControl(int)
     */
    public void setControls(Integer[] controls) {
        this.controls = controls;
    }

    /**
     * Sets the array of target values.
     *
     * @param targets The new array of target values.
     * @see #getTargets()
     * @see #setTarget(int)
     */
    public void setTargets(Integer[] targets) {
        this.targets = targets;
    }

    /**
     * To string for the work item outputs
     *
     * Single-qubit gates in format
     * operator, Target qubit: qubit#
     *
     * Multi-qubit gates in format
     * operator
     * Controls: qubit#, qubit#
     * Targets: qubit#, qubit#
     *
     * @return String of the work item
     */
    @Override
    public String toString(){
        String result;
        if(this.singleQubit){
            result = this.operator +", Target qubit: " + this.target;
        } else {
            result = this.operator +"\nControls: ";
            for(int i = 0; i < controls.length; i++){
                if(i!=controls.length-1) {
                    result += controls[i] + ", ";
                } else {
                    result += controls[i];
                }
            }
            result+="\n Targets: ";
            for(int i = 0; i < targets.length; i++){
                if(i!=targets.length-1){
                    result += targets[i]+", ";
                } else {
                    result += targets[i];
                }
            }
        }
        return result;
    }
}