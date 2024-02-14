import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Network {
    private ArrayList<Node> listNode;

    public Network() {
        this.listNode = new ArrayList<Node>();
    }

    public ArrayList<Node> getListNode() {
        return listNode;
    }

    public void setListNode(ArrayList<Node> listNode) {
        this.listNode = listNode;
    }

    public void addNode(Node node) {

//        this.setRandomId(node);

        // If the list is empty, add the node to the list
        if (this.listNode.size() == 0)
            // Add the node to the list
            this.listNode.add(node);
        else if (this.listNode.size() == 1) {
            // Add in the list of the node already present in the network in left and right neighbours
            this.listNode.get(0).addRight_neighbor(node.getId());
            this.listNode.get(0).addLeft_neighbor(node.getId());

            // Add in the list of the node to place in left and right neighbours
            node.addLeft_neighbor(this.listNode.get(0).getId());
            node.addRight_neighbor(this.listNode.get(0).getId());

            // If node to place has a smaller id than the first node in the list
            if (node.getId() < this.listNode.get(0).getId()) {
                // Add the node to the beginning of the list
                this.listNode.add(0, node);
            } else {
                // Add the node to the end of the list
                this.listNode.add(node);
            }

        }
        else {
            // Get a random node in the network
            Node nodeEnter = listNode.get(new Random().nextInt(listNode.size()));

            // On insert le node en fonction des autres nodes du network
            nodeEnter.join(this, node);

            System.out.println("prout");
        }
    }

    public void removeNode(Node node) {
        this.listNode.remove(node);
    }

    public void setRandomId(Node node) {
        /*
        * GOAL : Set un id aléatoire à un nouveau node en ne mettant pas un id déjà existant dans le réseau
        * Params : Node node: le node à qui on veut donner un id
        * */

        ArrayList<Integer> excludeNumbers = new ArrayList<Integer>(); // Les numéros à exclure

        for(Node n : this.listNode) {
            excludeNumbers.add(n.getId());
        }

        Random rand = new Random();
        int randomNumber;

        do {
            randomNumber = rand.nextInt(101); // Génère un nombre aléatoire entre 0 et 100
        } while (excludeNumbers.contains(randomNumber)); // Continue à générer jusqu'à ce qu'un nombre non exclu soit trouvé

        node.setId(randomNumber);
    }

    public Node getNodeById(int id) {
        /*
        * GOAL : Get un node dans la liste avec son id
        * Params :
        *   - id : id du node a get
        * */

        for(Node n : this.listNode) {
            if(n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Network{" +
                "listNode=" + listNode +
                '}';
    }
}