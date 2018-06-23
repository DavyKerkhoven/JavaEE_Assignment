/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.AbstractListModel;

/**
 *
 * @author Shane Birdsall
 * 
 * A special note of thanks to Shane, a past student from AUT, who helped me with this piece of code.
 * This class is his design and I have permission to use it. Thanks Shane!
 * 
 * ClientSet contains a ConcurrentLinkedQueue of clients and a defined list model.
 * The purpose of this class is to allow the Client JList to be updated dynamically.
 * 
 */
class ClientSet {
    private ConcurrentLinkedQueue<String> clientSet;
    private ClientListModel clientListModel;

    ClientSet() {
        super();
        clientSet = new ConcurrentLinkedQueue<>();
        clientListModel = new ClientListModel(clientSet);
    }

    ArrayList<String> getListOfClients() {
        return new ArrayList<>(clientSet);
    }

    void add(String client) {
        if(clientSet.add(client)) {
            clientListModel.addClient(client);
        }
    }

    void remove(String client) {
        if (clientSet.remove(client)) {
            clientListModel.removeClient(client);
        }
    }

    int size() { return clientSet.size(); }

    boolean contains(String client) {
        return clientSet.contains(client);
    }

    ClientListModel getClientListModel() {
        if(clientListModel == null) { // Create if need be
            clientListModel = new ClientListModel(clientSet);
        } return clientListModel;
    }

    private class ClientListModel extends AbstractListModel {
        private ArrayList<String> clientList;

        // constructor initialises arrayList and sorts the collection into its natural order
        ClientListModel(Collection<String> clientData) {
            super();
            clientList = new ArrayList<>();
            clientList.addAll(clientData);
            Collections.sort(clientList);
        }

        // gets the element at the specified index
        public Object getElementAt(int index) {
            if(index < clientList.size()) {
                return clientList.get(index);
            } else return null;
        }

        // gets the size of the arrayList
        public int getSize() { return clientList.size(); }

        // adds the client to the arrayList and notifies any listeners with fireIntervalAdded
        void addClient(String client) {
            if(clientList.add(client)) {
                fireIntervalAdded(this, 0, getSize());
            }
        }

        // removes client from the arrayList and notifies any listeners with fireIntervalRemoved
        void removeClient(String client) {
            if(clientList.remove(client)) {
                fireIntervalRemoved(this, 0, getSize());
            }
        }
    }
}
