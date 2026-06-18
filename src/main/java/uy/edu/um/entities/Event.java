package uy.edu.um.entities;

import uy.edu.um.tad.list.MyLinkedListImpl;

public class Event {

    private EventType type;
    private MyLinkedListImpl<String> instructions;

    public Event(EventType type) {
        this.type = type;
        this.instructions = new MyLinkedListImpl<>();
    }

    public EventType getType() {
        return type;
    }

    public MyLinkedListImpl<String> getInstructions() {
        return instructions;
    }


    public void addInstruction(String instruction) {
        instructions.add(instruction);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EVENT: ").append(type).append(" | Instructions [");
        for (int i = 0; i < instructions.size(); i++) {
            if (i > 0) sb.append(", ");
            try { sb.append(instructions.get(i)); }
            catch (IndexOutOfBoundsException e) { break; }
        }
        sb.append("]");
        return sb.toString();
    }
}
