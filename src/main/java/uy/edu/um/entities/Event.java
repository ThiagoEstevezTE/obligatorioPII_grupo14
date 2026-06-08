package uy.edu.um.entities;

public class Event {
private EventType type;
private String[] instructions;

public Event(EventType type, String[] instructions){
    this.type = type;
    this.instructions = instructions;
}
    public EventType getType() {
        return type;
    }

    public String[] getInstructions() {
        return instructions;
    }
}
