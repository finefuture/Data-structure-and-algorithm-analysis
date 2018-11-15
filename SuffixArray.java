package org.gra4j.trochilus;

import java.util.Arrays;

public class SuffixArray {

    public static int computeLCT (String s1, String s2) {
        int i = 0;
        while (i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i))
            i++;

        return i;
    }

    public static void createSuffixArray (String str, int[] SA, int[] LCP) {
        if (SA.length != str.length() || LCP.length != str.length())
            throw new IllegalArgumentException();

        int N = str.length();

        String[] suffixes = new String[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = str.substring(i);

        Arrays.sort(suffixes);

        for (int i = 0; i < N; i++)
            SA[i] = N - suffixes[i].length();

        LCP[0] = 0;
        for (int i = 1; i < N; i++)
            LCP[i] = computeLCT(suffixes[i - 1], suffixes[i]);
    }

    public static void createSuffixArray2 (String str, int[] sa, int[] LCP) {
        if (sa.length != str.length() || LCP.length != str.length())
            throw new IllegalArgumentException();

        int N = str.length();
        int[] s = new int[N+3];
        int[] SA = new int[N+3];

        for (int i = 0; i < N; i++)
            s[i] = str.charAt(i);

        makeSuffixArray(s, SA, N, 256);

        for (int i = 0; i < N; i++)
            sa[i] = SA[i];

        makeLCPArray(s, sa, LCP);
    }

    private static void makeSuffixArray(int[] s, int[] SA, int n, int K) {
        int n0 = (n + 2) / 3;
        int n1 = (n + 1) / 3;
        int n2 = n / 3;
        int t = n0 - n1;
        int n12 = n1 + n2 + t;

        int[] s12 = new int[n12 + 3];
        int[] SA12 = new int[n12 + 3];
        int[] s0 = new int[n0];
        int[] SA0 = new int[n0];

        for (int i = 0, j = 0; i < n + t; i++)
            if (i % 3 != 0)
                s12[j++] = i;

        int K12 = assignNames(s, s12, SA12, n0, n12, K);

        computeS12(s12, SA12, n12, K12);
        computeS0(s, s0, SA0, SA12, n0, n12, K);
        merge(s, s12, SA, SA0, SA12, n, n0, n12, t);
    }

    private static void merge(int[] s, int[] s12, int[] SA, int[] SA0, int[] SA12, int n, int n0, int n12, int t) {
        int p = 0, k = 0;
        while (t != n12 && p != n0) {
            int i = getIndexIntoS(SA12, t, n0);
            int j = SA0[p];

            if (suffix12IsSmaller(s, s12, SA12, n0, i, j, t)) {
                SA[k++] = i;
                t++;
            } else {
                SA[k++] = j;
                p++;
            }
        }

        while (p < n0)
            SA[k++] = SA0[p++];
        while (t < n12)
            SA[k++] = getIndexIntoS(SA12, t++, n0);
    }

    private static boolean suffix12IsSmaller(int[] s, int[] s12, int[] SA12, int n0, int i, int j, int t) {
        if (SA12[t] < n0)
            return leq(s[i], s12[SA12[t] + n0], s[j], s12[j/3]);
        else
            return leq(s[i], s[j + 1], s12[SA12[t] - n0 + 1], s[j], s[j + 1], s12[j / 3 + n0]);
    }

    private static boolean leq(int a1, int a2, int b1, int b2) {
        return a1 < b1 || a1 == b1 && a2 <= b2;
    }

    private static boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
        return a1 < b1 || a1 == b1 && leq(a2, a3, b2, b3);
    }

    private static int getIndexIntoS(int[] SA12, int t, int n0) {
        if (SA12[t] < n0)
            return SA12[t] * 3 + 1;
        else
            return (SA12[t] - n0) * 3 + 2;
    }

    private static void computeS0(int[] s, int[] s0, int[] SA0, int[] SA12, int n0, int n12, int K) {
        for (int i = 0, j = 0; i < n12; i++)
            if (SA12[i] < n0)
                s0[j++] = 3 * SA12[i];

        radixPass(s0, SA0, s, n0, K);
    }

    private static void computeS12(int[] s12, int[] SA12, int n12, int K12) {
        if (K12 == n12)
            for (int i = 0; i < n12; i++)
                SA12[s12[i] - 1] = i;
        else {
            makeSuffixArray(s12, SA12, n12, K12);
            for (int i = 0; i < n12; i++)
                s12[SA12[i]] = i + 1;
        }

    }

    private static int assignNames(int[] s, int[] s12, int[] SA12, int n0, int n12, int K) {
        radixPass(s12, SA12, s, 2, n12, K);
        radixPass(SA12, s12, s, 1, n12, K);
        radixPass(s12, SA12, s, 0, n12, K);

        int name = 0;
        int c0 = -1, c1 = -1, c2 = -1;

        for (int i = 0; i < n12; i++) {
            if (s[SA12[i]] != c0 || s[SA12[i] + 1] != c1 || s[SA12[i] + 2] != c2) {
                name++;
                c0 = s[SA12[i]];
                c1 = s[SA12[i] + 1];
                c2 = s[SA12[i] + 2];
            }

            if (SA12[i] % 3 == 1)
                s12[SA12[i] / 3] = name;
            else
                s12[SA12[i] / 3 + n0] = name;
        }

        return name;
    }

    private static void radixPass(int[] in, int[] out, int[] s, int offset, int n, int K) {
        int[] count = new int[K + 2];
        for (int i = 0; i < n; i++)
            count[s[in[i] + offset] + 1]++;

        for (int i = 1; i<= K + 1; i++)
            count[i] += count[i - 1];

        for (int i = 0; i < n; i++)
            out[count[s[in[i] + offset]]++] = in[i];
    }

    private static void radixPass (int[] in, int[] out, int[] s, int n, int K) {
        radixPass(in, out, s, 0, n , K);
    }

    private static void makeLCPArray(int[] s, int[] sa, int[] LCP) {
        int N = sa.length;
        int[] rank = new int[N];

        s[N] = -1;
        for (int i = 0; i < N; i++)
            rank[sa[i]] = i;

        int h = 0;
        for (int i = 0; i < N; i++)
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];

                while (s[i + h] == s[j + h])
                    h++;

                LCP[rank[i]] = h;
                if (h > 0)
                    h--;
            }
    }
}
