public class QuickFindUF {
    private int[] id;
    private int[] sz;
    private int[] bigObj;

    public QuickFindUF (int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
        sz = new int[N];
        for (int j = 0; j < N; j++) {
            sz[j] = 1;
        }
        bigObj = new int[N];
        for (int k = 0; k < N; k++) {
            bigObj[k] = k;
        }

    }

    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) System.out.println(p + " and" + q + " are already connected");
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        }
        else {
            id[j] = i;
            sz[i] += sz[j];
        }
        if (i > bigObj[j]) {
            bigObj[j] = i;
            }
        else {
            bigObj[i] = j;
        }
    }

    private int root(int i)
    {
        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public void find(int i) {
        int x = root(i);
        System.out.println(bigObj[x]);
    }

    public static void main(String[] args) {
        QuickFindUF quickFindUF = new QuickFindUF(6);
        quickFindUF.union(1,2);
        quickFindUF.union(2,5);
        quickFindUF.union(0,4);
        quickFindUF.union(3,0);
        quickFindUF.find(2);
        quickFindUF.find(0);
    }
}
