import java.util.*;
import java.io.*;

public class CustomerServiceQueue {

    static class Customer {
        String token, name, service;

        Customer(String token, String name, String service) {
            this.token = token;
            this.name = name;
            this.service = service;
        }
    }

    static class Node {
        Customer data;
        Node next;

        Node(Customer data) {
            this.data = data;
        }
    }

    static Node front = null, rear = null;
    static int tokenNumber = 101;
    static Scanner sc = new Scanner(System.in);

    // 1. Join Queue
    static void joinQueue() {
        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Service: ");
        String service = sc.nextLine();

        Customer c = new Customer("Q" + tokenNumber++, name, service);
        Node n = new Node(c);

        int ahead = 0;
        Node temp = front;

        while (temp != null) {
            ahead++;
            temp = temp.next;
        }

        if (front == null) {
            front = rear = n;
        } else {
            rear.next = n;
            rear = n;
        }

        System.out.println("Token issued. " + c.token + " | " + c.name
                + " | " + c.service);
        System.out.println("People ahead: " + ahead);

        save();
    }

    // 2. Call Next Customer
    static void callNext() {
        if (front == null) {
            System.out.println("Queue is empty.");
            return;
        }

        Customer c = front.data;

        System.out.println("Now serving: " + c.token + " | " + c.name);

        front = front.next;

        if (front == null) {
            rear = null;
            System.out.println("Queue is now empty.");
        } else {
            int remaining = 0;
            Node temp = front;

            while (temp != null) {
                remaining++;
                temp = temp.next;
            }

            System.out.println("Remaining in queue: " + remaining);
        }

        save();
    }

    // 3. Display Queue
    static void displayQueue() {
        if (front == null) {
            System.out.println("Queue is empty.");
            return;
        }

        Node temp = front;
        int position = 1;

        System.out.println("Waiting Queue:");

        while (temp != null) {
            Customer c = temp.data;

            System.out.println(position + ". " + c.token + " | "
                    + c.name + " | " + c.service);

            temp = temp.next;
            position++;
        }
    }

    // 4. Leave Queue by Token
    static void leaveQueue() {
        System.out.print("Token: ");
        String token = sc.nextLine().trim().toUpperCase();

        Node temp = front, prev = null;

        while (temp != null && !temp.data.token.equalsIgnoreCase(token)) {
            prev = temp;
            temp = temp.next;
        }

        if (temp == null) {
            System.out.println("Token " + token + " not found.");
            return;
        }

        if (prev == null) {
            front = front.next;
        } else {
            prev.next = temp.next;
        }

        if (temp == rear) {
            rear = prev;
        }

        if (front == null) {
            rear = null;
        }

        System.out.println("Customer removed from queue.");

        save();
    }

    // Save queue to file
    static void save() {
        try {
            PrintWriter pw = new PrintWriter("customers.txt");

            pw.println("#NEXT|" + tokenNumber);

            Node temp = front;

            while (temp != null) {
                Customer c = temp.data;

                pw.println(c.token + "|" + c.name + "|" + c.service);

                temp = temp.next;
            }

            pw.close();

        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    // Load queue from file
    static void load() {
        try {
            File f = new File("customers.txt");

            if (!f.exists()) {
                return;
            }

            Scanner file = new Scanner(f);

            while (file.hasNextLine()) {
                String line = file.nextLine();

                if (line.startsWith("#NEXT|")) {
                    tokenNumber = Integer.parseInt(line.split("\\|")[1]);
                    continue;
                }

                String[] a = line.split("\\|", 3);

                if (a.length != 3) {
                    continue;
                }

                Customer c = new Customer(a[0], a[1], a[2]);
                Node n = new Node(c);

                if (front == null) {
                    front = rear = n;
                } else {
                    rear.next = n;
                    rear = n;
                }

                int num = Integer.parseInt(c.token.substring(1));

                if (num >= tokenNumber) {
                    tokenNumber = num + 1;
                }
            }

            file.close();

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }

    // Main Menu
    public static void main(String[] args) {
        load();

        while (true) {
            System.out.println("\n1. Join Queue");
            System.out.println("2. Call Next Customer");
            System.out.println("3. Display Queue");
            System.out.println("4. Leave Queue (by Token)");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    joinQueue();
                    break;

                case "2":
                    callNext();
                    break;

                case "3":
                    displayQueue();
                    break;

                case "4":
                    leaveQueue();
                    break;

                case "5":
                    save();
                    System.out.println("Data saved. Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}