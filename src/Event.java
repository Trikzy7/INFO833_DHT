public class Event {
    private int duration;
    private Message message;
    private int nodeTarget;

    private int nodePlace;

    public Event(int duration, Message message, int nodeTarget, int nodePlace) {
        this.duration = duration;
        this.message = message;
        this.nodeTarget = nodeTarget;
        this.nodePlace = nodePlace;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public int getNodePlace() {
        return nodePlace;
    }

    public void setNodePlace(int nodePlace) {
        this.nodePlace = nodePlace;
    }
}
