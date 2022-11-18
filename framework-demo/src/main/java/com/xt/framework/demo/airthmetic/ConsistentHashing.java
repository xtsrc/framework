package com.xt.framework.demo.airthmetic;

import cn.hutool.core.util.HashUtil;
import lombok.Data;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * @author tao.xiong
 * @Description 一致性hash
 * @Date 2022/11/17 18:13
 */
public class ConsistentHashing {
    private final SortedMap<Integer, Node> hashCircle = new TreeMap<>();
    /**
     * 虚拟节点数
     */
    private final int virtualNums;

    public ConsistentHashing(Node[] nodes, int virtualNums) {
        this.virtualNums = virtualNums;
        // 初始化一致性hash环
        for (Node node : nodes) {
            // 创建虚拟节点
            add(node);
        }
    }

    /**
     * 添加服务器节点
     *
     * @param node the server
     */
    public void add(Node node) {
        for (int i = 0; i < virtualNums; i++) {
            hashCircle.put(HashUtil.fnvHash(node.toString() + i), node);
        }
    }

    /**
     * 删除服务器节点
     *
     * @param node the server
     */
    public void remove(Node node) {
        for (int i = 0; i < virtualNums; i++) {
            hashCircle.remove(HashUtil.fnvHash(node.toString() + i));
        }
    }

    /**
     * 获取服务器节点
     *
     * @param key the key
     * @return the server
     */
    public Node getNode(String key) {
        if (key == null || hashCircle.isEmpty()) {
            return null;
        }
        int hash = HashUtil.fnvHash(key);
        if (!hashCircle.containsKey(hash)) {
            // 未命中对应的节点
            SortedMap<Integer, Node> tailMap = hashCircle.tailMap(hash);
            hash = tailMap.isEmpty() ? hashCircle.firstKey() : tailMap.firstKey();
        }
        return hashCircle.get(hash);
    }


    /**
     * 集群节点的机器地址
     */
    @Data
    public static class Node {
        private String ipAddress;
        private Integer port;
        private String name;

        public Node() {
        }

        public Node(String ipAddress, int port, String name) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + ":<" + ipAddress + ":" + port + ">";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node node = (Node) o;

            if (!ipAddress.equals(node.ipAddress)) {
                return false;
            }
            return port.equals(node.port);
        }

        @Override
        public int hashCode() {
            int result = ipAddress.hashCode();
            result = 31 * result + port.hashCode();
            return result;
        }
    }

    public static void main(String[] args) {
        Node[] nodes = new Node[10];
        IntStream.rangeClosed(0, 9).forEach(i -> {
            Node node = new Node();
            node.setName("服务节点"+i);
            node.setIpAddress("192.168.0.10"+i);
            node.setPort(8500);
            nodes[i] =node;
        });
        ConsistentHashing consistentHashing=new ConsistentHashing(nodes,500);
        System.out.println(consistentHashing.getNode("测试1"));
        Node node = new Node();
        node.setName("服务新增节点");
        node.setIpAddress("192.168.0.20");
        node.setPort(8500);
        consistentHashing.add(node);
        System.out.println(consistentHashing.getNode("cc"));

    }

}
