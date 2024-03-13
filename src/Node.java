import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Node {
    private int id;
    private ArrayList<Integer> left_neighbours;
    private ArrayList<Integer> right_neighbours;

    public Node(int id) {
        this.id = id;

        // Create a new ArrayList empty for left and right neighbours
        this.left_neighbours = new ArrayList<Integer>();
//        this.left_neighbours.add(this);

        this.right_neighbours = new ArrayList<Integer>();
//        this.right_neighbours.add(this);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getLeft_neighbours() {
        return left_neighbours;
    }

    public void setLeft_neighbours(ArrayList<Integer> left_neighbours) {
        this.left_neighbours = left_neighbours;
    }

    public ArrayList<Integer> getRight_neighbours() {
        return right_neighbours;
    }

    public void setRight_neighbours(ArrayList<Integer> right_neighbours) {
        this.right_neighbours = right_neighbours;
    }


    public void addLeft_neighbor(Integer idNode) {
        this.left_neighbours.add(idNode);
    }

    public void addRight_neighbor(Integer idNode) {
        this.right_neighbours.add(idNode);
    }

    public boolean isMinOfNetwork() {
        /*
         * GOAL : Vérifier si le node est le plus petit du réseau
         * */
        return Collections.min(this.getLeft_neighbours()) > this.getId() &&
                Collections.max(this.getRight_neighbours()) > this.getId() &&
                this.getLeft_neighbours().size() != 0 &&
                this.getRight_neighbours().size() != 0;
    }

    public boolean isMaxOfNetwork() {
        /*
         * GOAL : Vérifier si le node est le plus grand du réseau
         * */
        return Collections.min(this.getLeft_neighbours()) < this.getId() &&
                Collections.max(this.getRight_neighbours()) < this.getId() &&
                this.getLeft_neighbours().size() != 0 &&
                this.getRight_neighbours().size() != 0;
    }

    public void sendMessage(Network network, Event event) {
        /*
         * GOAL : Envoyer un message à un node et le stocker dans la liste des évènements du network
         * Params
         *   - Network network: le réseau
         *   - Event event: l'évènement à envoyer
         * */

        // Set the execution time of the event
        event.setExecutionTime(network.getCurrentTime() + event.getLatency());

        // Set the current time of the network
//        network.setCurrentTime(network.getCurrentTime() + event.getExecutionTime());

        // Ajouter l'évènement à la liste des évènements
        network.getEventQueue().add(event);

        // Le node qui a reçu le message doit traiter le message
//        network.getNodeById(event.getNodeTarget()).deliverMessage(network, event);
    }

    public void deliverMessage(Network network, Event event) {
        /*
         * GOAL : Traiter un message reçu
         * Params
         *   - Message message: le message reçu
         * */

        if (event.getMessage().getProtocol() == Message.Protocol.JOIN) {
            switch (event.getMessage().getContent()) {
                case REQUEST:
                    System.out.println("REQUEST : " + this.getId() + " -> " + event.getNodePlace().getId());

                    // Si le message est une demande de join
                    this.joinRequest(network, event.getNodePlace());;
                    // On accepte la demande

                    // On ajoute le node à la liste des nodes

                    break;
                case ACK:
                    System.out.println("ACK : " + this.getId() + " -> " + event.getNodePlace().getId());
                    // Si le message est un ack
                    // On ajoute le node à la liste des nodes
                    this.joinAck(network, event.getNodePlace());

                    break;
                case EXECUTE:
                    System.out.println("EXECUTE : " + this.getId() + " -> " + event.getNodePlace().getId());
                    // Si le message est un execute
                    this.joinExecute(network, event.getNodePlace());
                    // On exécute le message
                    break;
            }

        }

    }

    public void joinRequest(Network network, Node nodePlace) {
        /*
         * GOAL : Envoyer une demande de join à un node
         * Params
         *   - Node nodePlace: le node que l'on veut placer
         */

        // Ajouter le node au network (même s'il n'est pas encore relié aux autres)
        // vérifier si le noeud est déjà dans le réseau
        if (!network.getListNode().contains(nodePlace))
            network.getListNode().add(nodePlace);

        // Si le node à placer a un id plus petit que le current node
        if (nodePlace.getId() < this.getId()) {
            System.out.println("PLUS PETIT");

            // Si le current Node est le min du network
            if (this.isMinOfNetwork()) {

                // Il faut que le voisin de droite envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que voisin de gauche de voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                network.getNodeById(Collections.max(this.getRight_neighbours()))
                        )
                );
                // Envoyer un message à son voisin de droite pour qu'il fasse un join execute avec le nodeToPlace
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                Collections.max(this.getRight_neighbours()),
                                nodePlace
                        )
                );

                // Il faut que current Node envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que son voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                this
                        )
                );

                // On envoie un event pour faire un join execute sur le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                this.getId(),
                                nodePlace
                        )
                );

            }
            // Aller voir dans les voisins de droites (les voisins plus petits que le current node)
            // -> on prends le plus proche de lui (le max de la liste) : cas où le id_current_node > node_à_placer > max(right_neighbours)
            else if (nodePlace.getId() > Collections.max(this.getRight_neighbours())) {
                // Il faut que le voisin de droite envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que voisin de gauche de voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                network.getNodeById(Collections.max(this.getRight_neighbours()))
                        )
                );
                // Envoyer un message à son voisin de droite pour qu'il fasse un join execute avec le nodeToPlace
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                Collections.max(this.getRight_neighbours()),
                                nodePlace
                        )
                );

                // Il faut que current Node envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que son voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                this
                        )
                );

                // On envoie un event pour faire un join execute sur le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                this.getId(),
                                nodePlace
                        )
                );


            }
            // on prends dans ses voisins de droite le plus proche de lui (le max de la liste) : cas où le node_à_placer < max(right_neighbours) < id_current_node
            // => on envoie de join au voisin de droite
            else {
                // Send a request join message to the right neighbour depuis le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.REQUEST
                                ),
                                Collections.max(this.getRight_neighbours()),
                                nodePlace
                        )
                );
            }
        } else {
            System.out.println("PLUS GRAND");

            if (this.isMaxOfNetwork()) {
                // Il faut que le voisin de gauche envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                network.getNodeById(Collections.min(this.getLeft_neighbours()))
                        )
                );
                // Envoyer un message à son voisin de gauche pour qu'il fasse un join execute avec le nodeToPlace
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                Collections.min(this.getLeft_neighbours()),
                                nodePlace
                        )
                );

                // Il faut que current Node envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que son voisin de gauche.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                this
                        )
                );

                // On envoie un event pour faire un join execute sur le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                this.getId(),
                                nodePlace
                        )
                );

            }

            // Aller voir dans les voisins de gauches (les voisins plus grands que le current node)
            // -> on prends le plus proche de lui (le min de la liste) : cas où le min(right_neighbours) > node_à_placer > id_current_node
            else if (nodePlace.getId() < Collections.min(this.getLeft_neighbours())) {
                // Il faut que le voisin de gauche envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que voisin de droite.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                network.getNodeById(Collections.min(this.getLeft_neighbours()))
                        )
                );
                // Envoyer un message à son voisin de gauche pour qu'il fasse un join execute avec le nodeToPlace
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                Collections.min(this.getLeft_neighbours()),
                                nodePlace
                        )
                );

                // Il faut que current Node envoie un ACK au nodeToPlace pour lui dire que nodeToPlace a bien été ajouté en tant
                // que son voisin de gauche.
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.ACK
                                ),
                                nodePlace.getId(),
                                this
                        )
                );

                // On envoie un event pour faire un join execute sur le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.EXECUTE
                                ),
                                this.getId(),
                                nodePlace
                        )
                );

            }
            // on prends dans ses voisins de gauche le plus proche de lui (le min de la liste) : cas où le node_à_placer > min(left_neighbours) > id_current_node
            // => on envoie de join au voisin de gauche
            else {
                // Send a request join message to the right neighbour depuis le current node
                this.sendMessage(
                        network,
                        new Event(
                                new Message(
                                        Message.Protocol.JOIN,
                                        Message.Content.REQUEST
                                ),
                                Collections.min(this.getLeft_neighbours()),
                                nodePlace
                        )
                );
            }
        }
    }

    public void joinExecute(Network network, Node nodePlace) {
        if (this.isMaxOfNetwork() && (this.getId() < nodePlace.getId() || nodePlace.getId() < Collections.min(this.getLeft_neighbours()))) {
            this.setLeft_neighbours(
                    new ArrayList<>(Collections.singleton(nodePlace.getId()))
            );
        } else if (this.isMinOfNetwork() && (this.getId() > nodePlace.getId()) || nodePlace.getId() > Collections.max(this.getRight_neighbours())) {
            this.setRight_neighbours(
                    new ArrayList<>(Collections.singleton(nodePlace.getId()))
            );
        }
        else {
            // Si le nodePlaceId < currentNodeId
            if (nodePlace.getId() < this.getId()) {
                // Mettre le nodeToPlace comme son voisin de droite
                this.setRight_neighbours(
                            new ArrayList<>(Collections.singleton(nodePlace.getId() ))
                    );
            }
            // Si le nodePlaceId > currentNodeId
            else {
                // Mettre le nodeToPlace comme son voisin de gauche
                this.setLeft_neighbours(
                            new ArrayList<>(Collections.singleton(nodePlace.getId() ))
                    );
            }
        }
    }

    public void joinAck(Network network, Node nodePlace){
//        System.out.println("ACK : " + this.getId() + " -> " + nodePlace.getId());
//        System.out.println("nodePlace : " +nodePlace + " " + nodePlace.isMaxOfNetwork() + nodePlace.isMinOfNetwork());
        if (nodePlace.isMaxOfNetwork()
                && (this.getId() > nodePlace.getId() || this.getId() <= Collections.min(nodePlace.getLeft_neighbours()))){
            System.out.println("max");
            this.setRight_neighbours(
                    new ArrayList<>(Collections.singleton(nodePlace.getId()))
            );
        } else if (nodePlace.isMinOfNetwork()
                && (this.getId() < nodePlace.getId() || this.getId() >= Collections.max(nodePlace.getRight_neighbours()))){
            System.out.println("min");
            this.setLeft_neighbours(
                    new ArrayList<>(Collections.singleton(nodePlace.getId()))
            );
        }
        else {
            // Si le nodePlaceId < currentNodeId
            if (nodePlace.getId() < this.getId()) {
                // Mettre currentNode comme son voisin de gauche
                this.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(nodePlace.getId()))
                );
            }
            // Si le nodePlaceId > currentNodeId
            else {
                // Mettre currentNode comme son voisin de droite
                this.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(nodePlace.getId()))
                );
            }
        }
    }

//    public void join(Network network, Node node) {
//        /*
//         * GOAL : Placer un node dans le réseau à partir du node sur lequel on est
//         * Params
//         *   - Node node: le node que l'on veut placer
//         * */
//
//
//        // Si le node à placer a un id plus petit que le current node
//        if (node.getId() < this.getId()) {
//            System.out.println("PLUS PETIT");
//
//            // Si le current Node est le min du network
//            if (this.isMinOfNetwork()) {
//                // on place le node à placer direct à droite du current node
//                node.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(this.getId()))
//                );
//                node.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(Collections.max(this.getRight_neighbours())))
//                );
//
//                // Node à placer devient le left neighbor de l'ancien right neighbor du current node
//                network.getNodeById(Collections.max(this.getRight_neighbours()))
//                        .setLeft_neighbours(
//                                new ArrayList<>(Collections.singleton(node.getId()))
//                        );
//
//                // Node à placer devient le right neighbor du current node
//                this.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(node.getId()))
//                );
//
//            }
//
//            // Aller voir dans les voisins de droites (les voisins plus petits que le current node)
//            // -> on prends le plus proche de lui (le max de la liste) : cas où le id_current_node > node_à_placer > max(right_neighbours)
//            else if (node.getId() > Collections.max(this.getRight_neighbours())) {
//
//                // Set the left and right neighbours of the node to place
//                node.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(this.getId()))
//                );
//                node.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(Collections.max(this.getRight_neighbours())))
//                );
//
//                // Node à placer devient le left neighbor de l'ancien right neighbor du current node
//                network.getNodeById(Collections.max(this.getRight_neighbours()))
//                        .setLeft_neighbours(
//                                new ArrayList<>(Collections.singleton(node.getId()))
//                        );
//
//                // Node à placer devient le right neighbor du current node
//                this.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(node.getId()))
//                );
//            }
//            // on prends dans ses voisins de droite le plus proche de lui (le max de la liste) : cas où le node_à_placer < max(right_neighbours) < id_current_node
//            // => on envoie de join au voisin de droite
//            else {
//                network.getNodeById(Collections.max(this.getRight_neighbours())).join(network, node);
//                // Send a request join message to the right neighbour depuis le current node
//                this.sendMessage(
//                        network,
//                        new Event(10, new Message(Message.Protocol.JOIN, Message.Content.REQUEST), Collections.max(this.getRight_neighbours()))
//                );
//            }
//        } else {
//            System.out.println("PLUS GRAND");
//
//            if (this.isMaxOfNetwork()) {
//                // Set the left and right neighbours of the node to place
//                node.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(this.getId()))
//                );
//                node.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(Collections.min(this.getLeft_neighbours())))
//                );
//
//                // Node à placer devient le right neighbor de l'ancien left neighbor du current node
//                network.getNodeById(Collections.min(this.getLeft_neighbours()))
//                        .setRight_neighbours(
//                                new ArrayList<>(Collections.singleton(node.getId()))
//                        );
//
//                // Node à placer devient le left neighbor du current node
//                this.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(node.getId()))
//                );
//
//            }
//
//            // Aller voir dans les voisins de gauches (les voisins plus grands que le current node)
//            // -> on prends le plus proche de lui (le min de la liste) : cas où le min(right_neighbours) > node_à_placer > id_current_node
//            else if (node.getId() < Collections.min(this.getLeft_neighbours())) {
//
//                // Set the left and right neighbours of the node to place
//                node.setRight_neighbours(
//                        new ArrayList<>(Collections.singleton(this.getId()))
//                );
//                node.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(Collections.min(this.getLeft_neighbours())))
//                );
//
//
//                // Node à placer devient le right neighbor de l'ancien left neighbor du current node
//                network.getNodeById(Collections.min(this.getLeft_neighbours()))
//                        .setRight_neighbours(
//                                new ArrayList<>(Collections.singleton(node.getId()))
//                        );
//
//                // Node à placer devient le left neighbor du current node
//                this.setLeft_neighbours(
//                        new ArrayList<>(Collections.singleton(node.getId()))
//                );
//
//            }
//            // on prends dans ses voisins de gauche le plus proche de lui (le min de la liste) : cas où le node_à_placer > min(left_neighbours) > id_current_node
//            // => on envoie de join au voisin de gauche
//            else {
//                network.getNodeById(Collections.min(this.getLeft_neighbours())).join(network, node);
//            }
//        }
//
//        // Ajouter le node au network
//        if (!network.getListNode().contains(node))
//            network.getListNode().add(node);
//
//    }


    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", left_neighbours=" + left_neighbours +
                ", right_neighbours=" + right_neighbours +
                '}';
    }
}
