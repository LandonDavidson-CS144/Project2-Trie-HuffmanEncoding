import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] str = {"apk", "app", "apple", "arp", "array"};
        String pre = "ap";
        Trie trie = new Trie();
        for (String s: str) {
            trie.insert(s);
        }
        System.out.println("The words in the trie are " + Arrays.toString(str));
        System.out.println("There are " + trie.countPre(pre) + " words with the prefix " + pre);


        String text = "abbdhabdbbbad";
        System.out.println("\nInitial String: " + text);

        HuffmanCoding huffman = new HuffmanCoding();
        String ciphertext = huffman.encode(text);
        System.out.println("The encoded text is: " + ciphertext);
        System.out.println("The encoded text was decoded back to: " + huffman.decode(ciphertext));
    }
}

class Trie {
    static class Node {
        private final ArrayList<Node> children;
        private final char letter;
        public Node(char letter) {
            this.letter = letter;
            children = new ArrayList<>();
        }
        public ArrayList<Node> getChildren() {
            return children;
        }
        public Node addChild(char element) {
            Node node = new Node(element);
            children.add(node);
            return node;
        }
        public Node getChild(char target) {
            for (Node node: children) {
                if (node.letter == target) {
                    return node;
                }
            }
            return null;
        }
        public boolean hasChild(char key) {
            for (Node node: children) {
                if (node.letter == key) {
                    return true;
                }
            }
            return false;
        }
        public void removeChild(Node target) {
            children.remove(target);
        }
        public boolean hasChildren() {
        return !children.isEmpty();
    }
    }

    Node root;
    public Trie() {
        root = new Node('1');
    }
    public void insert(String word) {
        word += '0';
        Node curNode = root;
        for (char c: word.toCharArray()) {
            if (!curNode.hasChild(c)) curNode = curNode.addChild(c);
            else curNode = curNode.getChild(c);
        }
    }
    public boolean search(String key) {
        key += '0';
        Node curNode = root;
        int charIndex = 0;
        while (curNode.hasChildren()) {
            char curChar = key.charAt(charIndex);
            if (!curNode.hasChild(curChar)) return false;
            curNode = curNode.getChild(curChar);
            charIndex++;
        }
        return true;
    }
    public void delete(String target) {
        deleteHelper(target + '0', root);
    }
    private void deleteHelper(String target, Node curNode) {
        char curChar = target.charAt(0);
        if (curNode.hasChild(curChar) && curChar != '0') {
            deleteHelper(target.substring(1), curNode.getChild(curChar));
        }
        if (curNode.hasChild(curChar) && !curNode.getChild(curChar).hasChildren()) {
            curNode.removeChild(curNode.getChild(curChar));
        }
    }
    public int countPre(String prefix) {
        Node curNode = root;
        for (char c: prefix.toCharArray()) {
            if (curNode.hasChild(c)) {
                curNode = curNode.getChild(c);
            } else return 0;
        }
        return countPreHelper(curNode, 0);
    }
    private int countPreHelper(Node curNode, int numPre) {
        for (Node n: curNode.getChildren()) {
            numPre = countPreHelper(n, numPre);
        }
        if (curNode.hasChild('0')) {
            return numPre + 1;
        }
        return numPre;
    }
}

class HuffmanCoding {
    static class Node implements Comparable<Node> {
        String letter;
        int frequency;
        public Node(String letter, int frequency) {
            this.letter = letter;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(Node node) {
            // this.compareTo(node) > 0 means this.frequency > node.frequency
            return this.frequency - node.frequency;
        }
    }
    HashMap<Character, String> encoder = new HashMap<>();
    HashMap<String, Character> decoder = new HashMap<>();
    public String encode(String plaintext) {
        HashMap<Character, Node> freqTable = new HashMap<>();
        for (char c: plaintext.toCharArray()) {
            if (freqTable.containsKey(c)) freqTable.get(c).frequency++;
            else freqTable.put(c, new Node(Character.toString(c), 1));
        }
        ArrayList<Node> freqList = new ArrayList<>(freqTable.values());
        Collections.sort(freqList);
        while (freqList.size() > 1) {
            Node right = freqList.get(0);
            Node left = freqList.get(1);
            freqList.remove(1);
            freqList.remove(0);
            freqList.add(new Node(left.letter + right.letter, left.frequency + right.frequency));
            Collections.sort(freqList);

            for (char c: right.letter.toCharArray()) {
                if (encoder.containsKey(c)) {
                    encoder.put(c, "1" + encoder.get(c));
                } else {
                    encoder.put(c, "1");
                }
            }
            for (char c: left.letter.toCharArray()) {
                if (encoder.containsKey(c)) {
                    encoder.put(c, "0" + encoder.get(c));
                } else {
                    encoder.put(c, "0");
                }
            }
        }
        for (char c: encoder.keySet()) {
            decoder.put(encoder.get(c), c);
        }
        StringBuilder encodedText = new StringBuilder();
        for (char c: plaintext.toCharArray()) {
            encodedText.append(encoder.get(c));
        }
        return encodedText.toString();
    }
    public String decode(String ciphertext) {
        StringBuilder curChar = new StringBuilder();
        StringBuilder plaintext = new StringBuilder();
        for (char c: ciphertext.toCharArray()) {
            curChar.append(c);
            if (decoder.containsKey(curChar.toString())) {
                plaintext.append(decoder.get(curChar.toString()));
                curChar = new StringBuilder();
            }
        }
        return plaintext.toString();
    }
}
