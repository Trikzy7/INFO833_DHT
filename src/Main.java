public class Main {
    public static void main(String[] args) {

        // VOISON GAUCHE -> LES PLUS GRANDS
        // VOISON DROITE -> LES PLUS PETITS

        Network network = new Network();

        // Create 3 nodes
        Node node1 = new Node(30);
        Node node2 = new Node(40);
        Node node3 = new Node(35);
        Node node4 = new Node(50);
        Node node5 = new Node(50);

        // Add the nodes to the network
//        node1.addLeft_neighbor(node2.getId());
//        node1.addLeft_neighbor(node3.getId());


        // Network before add
        System.out.println(network);


        // Ajout d'un node au network si le network est vide
        System.out.println("---------- Ajout d'un node au network si le network est vide ----------");
        network.addNode(node1);
        System.out.println(network);

        // Ajout d'un node au network si le network contient déjà un node
        System.out.println("---------- Ajout d'un node au network si le network contient déjà 1 node ----------");
        network.addNode(node2);
        System.out.println(network);
//
//        // Ajout d'un node au network si le network contient déjà plus de 2 node
        System.out.println("---------- Ajout d'un node au network si le network contient déjà plus de 2 node : Ajout d'un node direct a côté du node sur lequel on rentre ----------");
        network.addNode(node3);
        System.out.println(network);

        // Ajout d'un node au network si le network contient déjà plus de 3 node
        System.out.println("---------- Ajout d'un node au network si le network contient déjà plus de 2 node : Ajout d'un node en passant par un autre node ----------");
        network.addNode(node4);
        System.out.println(network);
//
//        // Ajout d'un node au network si le network contient déjà plus de 3 node
//        System.out.println("---------- Ajout d'un node au network si le network contient déjà plus de 2 node : Ajout d'un node en passant par un autre node ----------");
//        network.addNode(node5);
//        System.out.println(network);

//
//        System.out.println("---------- Ajout d'un node quand le network est déjà bien rempli ----------");
//        network.addNode(node5);
//        System.out.println(network);

    }
}