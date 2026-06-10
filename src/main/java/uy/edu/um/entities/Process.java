package uy.edu.um.entities;
import uy.edu.um.tad.list.MyLinkedListImpl;
public class Process implements Comparable<Process> {


    private int pid;
    private String name;
    private User owner;
    private int priority;
    private ProcessState state;
    private FinishState finishType;
    private User terminatedBy;


    private MyLinkedListImpl<Event> events;

    public Process(int pid, String name, User owner) {
        this.pid = pid;
        this.name = name;
        this.owner = owner;
        this.priority = 0;
        this.state = ProcessState.NEW;
        this.finishType = null;
        this.terminatedBy = null;
        this.events = new MyLinkedListImpl<>();
    }


    @Override
    public int compareTo(Process other) {
        return Integer.compare(other.priority, this.priority);
    }


    public int getPid() { return pid; }
    public String getName() { return name; }
    public User getOwner() { return owner; }
    public int getPriority() { return priority; }
    public ProcessState getState() { return state; }
    public FinishState getFinishType() { return finishType; }
    public User getTerminatedBy() { return terminatedBy; }
    public MyLinkedListImpl<Event> getEvents() { return events; }


    public void setPriority(int priority) { this.priority = priority; }
    public void setState(ProcessState state) { this.state = state; }

    public void addEvent(Event event) {
        events.add(event);
    }


    public void finish(FinishState type) {
        this.finishType = type;
        this.state = ProcessState.FINISHED;
    }


    public void terminate(User byUser) {
        this.finishType = FinishState.TERMINATED;
        this.terminatedBy = byUser;
        this.state = ProcessState.FINISHED;
    }


    public int countEvents(EventType type) {
        int count = 0;
        for (int i = 0; i < events.size(); i++) {
            try {
                if (events.get(i).getType() == type) count++;
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return count;
    }

    public int getTotalEvents() {
        return events.size();
    }

    public String toShortString() {
        return "PID=" + pid + " | " + name + " | " + owner.toString() + " | P=" + priority;
    }


    public String toFinishedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PID=").append(pid).append(" ").append(name);
        sb.append(" | STATE: ").append(finishType);
        if (finishType == FinishState.TERMINATED && terminatedBy != null) {
            sb.append(" by ").append(terminatedBy.toString());
        }
        sb.append(" | ").append(owner.toString());
        return sb.toString();
    }


    public String toVerboseString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toShortString()).append("\n");
        for (int i = 0; i < events.size(); i++) {
            try {
                sb.append("  ").append(events.get(i).toString()).append("\n");
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return sb.toString();
    }
}