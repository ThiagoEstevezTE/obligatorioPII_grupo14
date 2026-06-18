package uy.edu.um.entities;

import uy.edu.um.tad.list.MyLinkedListImpl;

public class Process implements Comparable<Process> {


    private int pid;

    private String name;

    private User owner;

    private int priority;

    private ProcessState state;

    private MyLinkedListImpl<Event> events;

    private FinishState finishState;

    private User terminatedBy;

    public Process(int pid, String name, User owner) {
        this.pid = pid;
        this.name = name;
        this.owner = owner;

        this.priority = 0;
        this.state = ProcessState.NEW;
        this.finishState = null;
        this.terminatedBy = null;

        this.events = new MyLinkedListImpl<>();
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

    public MyLinkedListImpl<Event> getEvents() {
        return events;
    }

    public FinishState getFinishState() {
        return finishState;
    }

    public User getTerminatedBy() {return terminatedBy;}

    public void setFinishState(FinishState finishState) {
        this.finishState = finishState;
    }
    public void addEvent(Event event) {
        events.add(event);
    }

    public void finish(FinishState type) {
        this.finishState = type;
        this.state = ProcessState.FINISHED;
    }

    public void terminate(User byUser) {
        this.finishState = FinishState.TERMINATED;
        this.terminatedBy = byUser;
        this.state = ProcessState.FINISHED;
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.priority, other.priority);
    }

    public int countEvents(EventType type) {
        int count = 0;

        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);

            if (e.getType() == type) {
                count++;
            }
        }

        return count;
    }


public String toShortString() {
    return "PID=" + pid + " | " + name + " | " + owner.toString() + " | P=" + priority;
}

public String toFinishedString() {
    StringBuilder sb = new StringBuilder();
    sb.append("PID=").append(pid).append(" ").append(name);
    sb.append(" | STATE: ").append(finishState);

    if (finishState == FinishState.TERMINATED && terminatedBy != null) {
        sb.append(" by ").append(terminatedBy.toString());
    }

    sb.append(" | ").append(owner.toString());
    return sb.toString();
}

public String toVerboseString() {
    StringBuilder sb = new StringBuilder();
    sb.append(toShortString()).append("\n");

    for (int i = 0; i < events.size(); i++) {
        sb.append("  ").append(events.get(i).toString()).append("\n");
    }

    return sb.toString();
}
}
