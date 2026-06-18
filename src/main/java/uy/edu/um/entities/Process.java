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

    public Process(int pid, String name, User owner) {
        this.pid = pid;
        this.name = name;
        this.owner = owner;

        this.priority = 0;
        this.state = ProcessState.NEW;
        this.finishState = null;

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

    public void setFinishState(FinishState finishState) {
        this.finishState = finishState;
    }
    public void addEvent(Event event) {
        events.add(event);
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

}
