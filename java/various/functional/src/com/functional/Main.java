package com.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
	/*// write your code here
        SuperIterable<String>  strings = new SuperIterable<>(Arrays.asList("LightCoral", "pink", "orange", "gold", "plum", "Blue", "limegreen"));


        SuperIterable<String> upperCase = strings.filter(s->Character.isUpperCase(s.charAt(0)));

       // upperCase.forEach(s->System.out.println("> " +s));

        //strings.forEach(s->System.out.println("> " +s));

        strings.filter(s->Character.isUpperCase(s.charAt(0)))
                .map(x->x.toUpperCase())
                .forEach(s->System.out.println("> " +s));


        //strings.flatMap(c -> new SuperIterable<>(c.))

        final Averager collect = DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-Math.PI, Math.PI))
                .limit(1000)
                .collect(() -> new Averager(), (b, i) -> b.add(i), (b1, b2) -> b1.merge(b2));

        System.out.println(collect.get());*/

        List<Student> school = Arrays.asList(
          new Student("Fred", 71),
            new Student("Jim", 28),
            new Student("Sheila", 97),
            new Student("Weatherwax", 100),
            new Student("Ogg", 56),
            new Student("Ricewind", 28),
            new Student("Ridcully", 65),
            new Student("Magrat", 79),
            new Student("Valentine", 93),
            new Student("Gillian", 87),
            new Student("Anne", 91),
            new Student("Dr. Mahmoud", 88),
            new Student("Ender", 81),
            new Student("Hyrum", 72),
            new Student("Locke", 91),
            new Student("Bonzo", 57)
        );


        final Map<String, List<Student>> table = school.stream()
                .collect(Collectors.groupingBy(s -> s.getLetterGrade()));

        //Comparator<Map.Entry<String, List<Student>>> comparator = (e1, e2) -> e2.getKey().compareTo(e1.getKey());
        Comparator<Map.Entry<String, List<Student>>> comparator = Map.Entry.comparingByKey();
        comparator = comparator.reversed();

        /*        new Comparator<Map.Entry<String, List<Student>>>() {
            @Override
            public int compare(Map.Entry<String, List<Student>> o1, Map.Entry<String, List<Student>> o2) {
                return 0;
            }
        };*/


        table.entrySet()
             .stream()
             .sorted(comparator)
             .forEach(e->System.out.println("Students " + e.getValue()));

        final Map<String, Long> table2 = school.stream().collect(Collectors.groupingBy(s -> s.getLetterGrade(), Collectors.counting()));

        table2.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e->System.out.println(e.getValue() + " studenst got grade " + e.getKey()));


        //final Map<String, String> table3 =  school.stream().collect(Collectors.groupingBy(s -> s.getLetterGrade(), Collectors.joining()));

        school.stream().collect(Collectors.groupingBy(s -> s.getLetterGrade()))
                .entrySet()
                .stream()
                .collect(HashMap<String, String>::new, (c, e) -> c.put(e.getKey(), e.getValue()
                        .stream()
                        .map(a->a.getName())
                        .collect(Collectors.joining(", "))),
                        Map::putAll)
                .entrySet()
                .stream()
                .forEach(e->System.out.println(e.getKey() +"=" +e.getValue()));



        //System.out.println(collect);


        // .forEach(k -> m.put(k, e.getKey())), Map::putAll)

        //System.out.println(table);


        try {
            //Concordance.split("jane_austin.txt");
            Concordance.splitMultipleFiles2(new ArrayList<String>(Arrays.asList("jane_austin.txt", "badbook.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Averager collect = DoubleStream.generate(() -> ThreadLocalRandom.current()
                .nextDouble(-Math.PI, Math.PI))
                .limit(1000)
                .collect(Averager::new, Averager::add, Averager::merge);

        System.out.println(collect.get());

    }



}
