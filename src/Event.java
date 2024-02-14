public class Event {
    private int duration;
    private Message message;
    private int nodeTarget;

    public Event(int duration, Message message, int nodeTarget) {
        this.duration = duration;
        this.message = message;
        this.nodeTarget = nodeTarget;
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

}
