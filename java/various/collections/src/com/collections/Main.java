package com.collections;

public class Main {

    public static void main(String[] args) {
	// write your code here
        MyList<String> myList = new MyList<>();
        myList.add("aa");
        myList.add("bb");

        myList.add("aaa",-4);

        var ccc = myList.get(2).orElse("");

        for(var el : new Sequence(1,3,100)) {
            System.out.println(el);

        }

        int a = 10;
        a = ++a + a + --a - --a + a++;
        System.out.println (a);
    }
}
