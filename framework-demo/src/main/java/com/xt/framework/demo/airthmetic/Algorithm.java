package com.xt.framework.demo.airthmetic;

import java.util.LinkedList;
import java.util.Stack;

/**
 * @Author: tao.xiong
 * @Date: 2020/1/19 17:15
 * @Description: 图论、广度优先算法、深度优先算法、二分法、动态规划、暴力枚举、贪心算法、分治法、回溯法、数论
 */
public class Algorithm {
    /**
     * 二分法
     */
    public int dichotomy() {
        // 左边界
        int left = 0;
        // 右边界
        int right = 100;
        // 记录值
        int answer = 0;
        while (left <= right) {
            // 中间值
            int middle = (left + right) / 2;
            if (guess(middle)) {
                // 满足，记录答案
                answer = middle;
                // 在更小的范围搜索
                right = middle - 1;
            } else {
                // 不满足，在更大的范围搜索
                left = middle + 1;
            }
        }
        return answer;
    }

    private Boolean guess(int middle) {
        return Boolean.FALSE;
    }

    /**
     * 排序算法
     */
    static class Array {
        public void bubbleSort(int[] arr) {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (arr[i] > arr[j]) {
                        int temp = arr[j];
                        arr[j] = arr[i];
                        arr[i] = temp;
                    }
                }
            }
        }

        public void selectSort(int[] arr) {
            for (int i = 0; i < arr.length; i++) {
                int min = i;
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] < arr[min]) {
                        min = j;
                    }
                }
                if (min != i) {
                    int temp = arr[min];
                    arr[min] = arr[i];
                    arr[i] = temp;
                }
            }
        }

        public void insertSort(int[] arr) {
            for (int i = 1; i < arr.length; i++) {
                int j = i;
                int target = arr[i];
                while (j > 0 && target < arr[j - 1]) {
                    arr[j] = arr[j - 1];
                    j--;
                }

                arr[j] = target;
            }
        }
    }

    /**
     * 一个人爬楼梯，每次只能爬1个或2个台阶，假设有n个台阶，那么这个人有多少种不同的爬楼梯方法？ 1.找到如何将大问题分解为小问题的规律 2.通过规律写出递推公式 3.通过递归公式的临界点推敲出终止条件
     * 4.将递推公式和终止条件翻译成代码 自顶向下 时间复杂度：O(2^n)
     */
    int f(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        return f(n - 1) + f(n - 2);
    }

    /**
     * 动态规划：1.最优子结构性质 2.子问题重叠性质 3.自底而上的求解方法 记忆化地求解递推式 【最优子结构】 【边界】 【状态转移公式】 时间复杂度：O(n)
     */
    int f2(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        // a 保存倒数第二个子状态数据，b 保存倒数第一个子状态数据， temp 保存当前状态的数据
        int a = 1, b = 2;
        int temp = a + b;
        for (int i = 3; i <= n; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return temp;
    }

    /**
     * 图 邻接列表表示的有向图
     */
    static class Graph {
        int v;// No .vertices
        private LinkedList<Integer>[] adj; // adjacency list

        private Graph(int v) {
            this.v = v;
            adj = new LinkedList[v];
            for (int i = 0; i < v; ++i) {
                adj[i] = new LinkedList<>();
            }
        }

        void addEdge(int v, int w) {
            adj[v].add(w);
        }

        /**
         * 拓扑排序调用的DFS函数
         */
        void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
            // 标记当前节点为已访问.
            visited[v] = true;
            Integer i;

            // 递归访问它的所有邻接顶点
            for (Integer integer : adj[v]) {
                i = integer;
                if (!visited[i]) {
                    topologicalSortUtil(i, visited, stack);
                }
            }

            // 把当前节点加入存放结果的栈中
            stack.push(v);
        }
        // 拓扑排序
        void topologicalSort()
        {
            Stack stack = new Stack();

            // 标记所有节点为未访问
            boolean[] visited = new boolean[v];
            for (int i = 0; i < v; i++) {
                visited[i] = false;
            }

            // Call the recursive helper function to store
            // Topological Sort starting from all vertices
            // one by one
            for (int i = 0; i < v; i++) {
                if (!visited[i]) {
                    topologicalSortUtil(i, visited, stack);
                }
            }

            // 打印结果栈的内容
            while (!stack.empty()) {
                System.out.print(stack.pop() + " ");
            }
        }

        public static void main(String[] args)
        {
            Graph g = new Graph(6);
            g.addEdge(5, 2);
            g.addEdge(5, 0);
            g.addEdge(4, 0);
            g.addEdge(4, 1);
            g.addEdge(2, 3);
            g.addEdge(3, 1);

            System.out.println("Following is a Topological " +
                    "sort of the given graph");
            g.topologicalSort();
        }
    }


}
