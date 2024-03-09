import java.util.Random;

public class Event implements Comparable<Event> {
    private int latency;
    private int executionTime;
    private Message message;
    private int nodeTarget;
    private Node nodePlace;

    public Event(Message message, int nodeTarget, Node nodePlace) {
        this.latency = new Random().nextInt(100) + 1;
        this.message = message;
        this.nodeTarget = nodeTarget;
        this.nodePlace = nodePlace;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getNodeTarget() {
        return nodeTarget;
    }

    public void setNodeTarget(int nodeTarget) {
        this.nodeTarget = nodeTarget;
    }

    public Node getNodePlace() {
        return nodePlace;
    }

    public void setNodePlace(Node nodePlace) {
        this.nodePlace = nodePlace;
    }

    @Override
    public int compareTo(Event other) {
        return Integer.compare(this.executionTime, other.executionTime);
    }
}
