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
        return Collections.min(this.getLeft_neighbours()) > this.getId() && Collections.max(this.getRight_neighbours()) > this.getId();
    }

    public boolean isMaxOfNetwork() {
        /*
        * GOAL : Vérifier si le node est le plus grand du réseau
        * */
        return Collections.min(this.getLeft_neighbours()) < this.getId() && Collections.max(this.getRight_neighbours()) < this.getId();
    }

    public void join(Network network, Node node) {
        /*
        * GOAL : Placer un node dans le réseau à partir du node sur lequel on est
        * Params
        *   - Node node: le node que l'on veut placer
        * */



        // Si le node à placer a un id plus petit que le current node
        if (node.getId() < this.getId() ) {
            System.out.println("PLUS PETIT");

            // Si le current Node est le min du network
            if (this.isMinOfNetwork()) {
                // on place le node à placer direct à droite du current node
                node.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(this.getId()))
                );
                node.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(Collections.max(this.getRight_neighbours())))
                );

                // Node à placer devient le left neighbor de l'ancien right neighbor du current node
                network.getNodeById( Collections.max(this.getRight_neighbours()) )
                        .setLeft_neighbours(
                                new ArrayList<>(Collections.singleton(node.getId()))
                        );

                // Node à placer devient le right neighbor du current node
                this.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(node.getId()))
                );

            }

            // Aller voir dans les voisins de droites (les voisins plus petits que le current node)
            // -> on prends le plus proche de lui (le max de la liste) : cas où le id_current_node > node_à_placer > max(right_neighbours)
            else if (node.getId() > Collections.max(this.getRight_neighbours())) {

                // Set the left and right neighbours of the node to place
                node.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(this.getId()))
                );
                node.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(Collections.max(this.getRight_neighbours())))
                );

                // Node à placer devient le left neighbor de l'ancien right neighbor du current node
                network.getNodeById( Collections.max(this.getRight_neighbours()) )
                        .setLeft_neighbours(
                                new ArrayList<>(Collections.singleton(node.getId()))
                        );

                // Node à placer devient le right neighbor du current node
                this.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(node.getId()))
                );
            }
            // on prends dans ses voisins de droite le plus proche de lui (le max de la liste) : cas où le node_à_placer < max(right_neighbours) < id_current_node
            // => on envoie de join au voisin de droite
            else {
                network.getNodeById(Collections.max(this.getRight_neighbours())).join(network, node);
            }
        }
        else {
            System.out.println("PLUS GRAND");

            if (this.isMaxOfNetwork()) {
                // Set the left and right neighbours of the node to place
                node.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(this.getId()))
                );
                node.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(Collections.min(this.getLeft_neighbours())))
                );

                // Node à placer devient le right neighbor de l'ancien left neighbor du current node
                network.getNodeById( Collections.min(this.getLeft_neighbours()) )
                        .setRight_neighbours(
                                new ArrayList<>(Collections.singleton(node.getId()))
                        );

                // Node à placer devient le left neighbor du current node
                this.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(node.getId()))
                );

            }

            // Aller voir dans les voisins de gauches (les voisins plus grands que le current node)
            // -> on prends le plus proche de lui (le min de la liste) : cas où le min(right_neighbours) > node_à_placer > id_current_node
            else if (node.getId() < Collections.min(this.getLeft_neighbours())) {

                // Set the left and right neighbours of the node to place
                node.setRight_neighbours(
                        new ArrayList<>(Collections.singleton(this.getId()))
                );
                node.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(Collections.min(this.getLeft_neighbours())))
                );


                // Node à placer devient le right neighbor de l'ancien left neighbor du current node
                network.getNodeById( Collections.min(this.getLeft_neighbours()) )
                        .setRight_neighbours(
                                new ArrayList<>(Collections.singleton(node.getId()))
                        );

                // Node à placer devient le left neighbor du current node
                this.setLeft_neighbours(
                        new ArrayList<>(Collections.singleton(node.getId()))
                );

            }
            // on prends dans ses voisins de gauche le plus proche de lui (le min de la liste) : cas où le node_à_placer > min(left_neighbours) > id_current_node
            // => on envoie de join au voisin de gauche
            else {
                network.getNodeById(Collections.min(this.getLeft_neighbours())).join(network, node);
            }
        }

        // Ajouter le node au network
        if (!network.getListNode().contains(node))
            network.getListNode().add(node);

    }


    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", left_neighbours=" + left_neighbours +
                ", right_neighbours=" + right_neighbours +
                '}';
    }
}
