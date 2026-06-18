package uy.edu.um.entities;

public class Process implements Comparable<Process> {


    private int pid;

    private String name;

    private User owner;

    private int priority;

    private ProcessState state;

    private Event[] events;

    private FinishState finishState;

    public Process(
            int pid,
            String name,
            User owner,
            Event[] events) {

        this.pid = pid;
        this.name = name;
        this.owner = owner;
        this.events = events;

        this.state = ProcessState.NEW;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public Event[] getEvents() {
        return events;
    }

    public FinishState getFinishState() {
        return finishState;
    }
    public void setFinishState(FinishState finishState) {
        this.finishState = finishState;
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.priority, other.priority);
    }
}
