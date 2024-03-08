public class Message {
    public enum Protocol {
        JOIN, LEAVE, MESSAGE
    };

    public enum Content {
        ACK, REQUEST, EXECUTE
    };

    private Protocol protocol;

    private Content content;

    public Message(Protocol protocol, Content content) {
        this.protocol = protocol;
        this.content = content;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


}
