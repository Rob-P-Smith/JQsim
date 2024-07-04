package state;

/**
 * Represents a work item with an operator and associated control and target values.
 * This class can handle both single control/target and multiple controls/targets scenarios.
 */
public class WorkItem {
    private String operator;
    private int control;
    private int target;
    private int[] controls;
    private int[] targets;

    /**
     * Constructs a WorkItem with a single control and target.
     *
     * @param operator The operator string.
     * @param control The control value.
     * @param target The target value.
     * @see #WorkItem(String, int[], int[])
     */
    public WorkItem(String operator, int control, int target) {
        this.operator = operator;
        this.control = control;
        this.target = target;
    }

    /**
     * Constructs a WorkItem with multiple controls and targets.
     *
     * @param operator The operator string.
     * @param controls An array of control values.
     * @param targets An array of target values.
     * @see #WorkItem(String, int, int)
     */
    public WorkItem(String operator, int[] controls, int[] targets) {
        this.operator = operator;
        this.controls = controls;
        this.targets = targets;
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
     * @see #setControls(int[])
     * @see #getControl()
     */
    public int[] getControls() {
        return controls;
    }

    /**
     * Gets the array of target values.
     *
     * @return The array of target values.
     * @see #setTargets(int[])
     * @see #getTarget()
     */
    public int[] getTargets() {
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
     * @see #setControls(int[])
     */
    public void setControl(int control) {
        this.control = control;
    }

    /**
     * Sets the single target value.
     *
     * @param target The new target value.
     * @see #getTarget()
     * @see #setTargets(int[])
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
    public void setControls(int[] controls) {
        this.controls = controls;
    }

    /**
     * Sets the array of target values.
     *
     * @param targets The new array of target values.
     * @see #getTargets()
     * @see #setTarget(int)
     */
    public void setTargets(int[] targets) {
        this.targets = targets;
    }
}