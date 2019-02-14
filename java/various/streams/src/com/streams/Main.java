package com.streams;

import com.streams.model.Actor;
import com.streams.model.Movie;
import com.streams.model.Person;
import com.streams.spliterator.PersonSpliterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.*;

import static java.lang.StrictMath.sqrt;

public class Main {

    public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","2");
	// write your code here
        Path path = Paths.get("files/people.txt");

        try (Stream<String> lines = Files.lines(path);){

            Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<Person> peopleSpliterator = new PersonSpliterator(lineSpliterator);

            Stream<Person> people =  StreamSupport.stream(peopleSpliterator,false);
            //people.forEach(System.out::println);

            ForkJoinPool fjp = new ForkJoinPool(2);

            final OptionalDouble optionalDouble = fjp.submit(() -> people.parallel().mapToInt(p -> p.getAge()).filter(age -> age > 20).average()).get();

            if(optionalDouble.isPresent()) {
                System.out.println("optionalDouble: " + optionalDouble.getAsDouble());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        try {
            Stream<String> stream1 = Files.lines(Paths.get("files/tom_sawyer_1.txt"));
            Stream<String> stream2 = Files.lines(Paths.get("files/tom_sawyer_2.txt"));
            Stream<String> stream3 = Files.lines(Paths.get("files/tom_sawyer_3.txt"));
            Stream<String> stream4 = Files.lines(Paths.get("files/tom_sawyer_4.txt"));

          /*  System.out.println(stream1.count());
            System.out.println(stream2.count());
            System.out.println(stream3.count());
            System.out.println(stream4.count());*/

            Function<String, Stream<String>> lineSplitter = line-> Pattern.compile("\\s+").splitAsStream(line);

            Stream<Stream<String>> streamOfStreams = Stream.of(stream1, stream2, stream3, stream4);
            Stream<String> streamOfLines =  streamOfStreams.flatMap(Function.identity());
            Stream<String> streamOfWords = streamOfLines
                    .flatMap(lineSplitter)
                    .map(word->word.toLowerCase())
                    .filter(word->word.length()==4)
                    .distinct();

            System.out.println(streamOfWords.count());


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            final Set<String> shakespearesWords = Files.lines(Paths.get("files/words.shakespeare.txt"))
                    .map(word -> word.toLowerCase())
                    .collect(Collectors.toSet());

           final Set<String> scrableWords =  Files.lines(Paths.get("files/ospd.txt"))
                    .map(word-> word.toLowerCase())
                    .collect(Collectors.toSet());

            final int [] scrabbleENScoreTmp = {
                    // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
                       1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10} ;


            Function<String, Integer> score = word -> word.chars().map(letter -> scrabbleENScoreTmp[letter - 'a']).sum();

            ToIntFunction<String> toIntScore = word->word.chars().reduce(0, (acc, letter)-> acc + scrabbleENScoreTmp[letter - 'a']);

            System.out.println("Score: " + score.apply("hello"));
            System.out.println("toIntScore: " + toIntScore.applyAsInt("hello"));


            final String bestWord =
                    shakespearesWords.stream()
                            .filter(word->scrableWords.contains(word))
                    .max(Comparator.comparingInt(toIntScore))
                    .get();

            System.out.println("bestWord: " + bestWord);


            final IntSummaryStatistics intSummaryStatistics = shakespearesWords.stream()
                    .filter(scrableWords::contains)
                    .mapToInt(toIntScore)
                    .summaryStatistics();

            System.out.println("Stats: " + intSummaryStatistics);


            Map<Integer, List<String>> histogram = shakespearesWords
                    .stream()
                    .filter(scrableWords::contains)
                    .collect(Collectors.groupingBy(score));

            System.out.println("# histogram: " + histogram.size());

            histogram.entrySet()
                     .stream()
                     .sorted(Comparator.comparing(entry -> -entry.getKey()))
                     .limit(3)
                     .forEach(System.out::println);

            final int [] scrabbleENSDistribution = {
                    // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
                       9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 1} ;

            Function<String, Map<Integer, Long>> histoWord = word -> word.chars()
                                                                         .boxed()
                                                                         .collect(Collectors.groupingBy(letter->letter, Collectors.counting()));

            Function<String, Long> nBlanks = word -> histoWord.apply(word)
                    .entrySet()
                    .stream()
                    .mapToLong(entry ->
                            Long.max(entry.getValue() - scrabbleENSDistribution[entry.getKey()-'a'], 0L)
                    ).sum();


            Function<String, Integer> score2 = word ->
                    histoWord.apply(word)
                    .entrySet()
                    .stream()
                    .mapToInt(entry ->
                            Integer.min(entry.getValue().intValue(),scrabbleENSDistribution[entry.getKey() - 'a'])
                                    * scrabbleENScoreTmp[entry.getKey() - 'a']

                    ).sum();


            System.out.println("-----------------------");
            shakespearesWords
                    .stream()
                    .filter(scrableWords::contains)
                    .filter(word -> nBlanks.apply(word) == 0)
                    .collect(Collectors.groupingBy(score2))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> -entry.getKey()))
                    .limit(3)
                    .forEach(System.out::println);


            //Function<String, Integer> score2 = word->


            System.out.println("# histoWord: " + histoWord.apply("whizzing"));
            System.out.println("# nBlanks: " + nBlanks.apply("whizzing"));
            System.out.println("# score2: " + score2.apply("whizzing"));
            //System.out.println("# histogram2: " + histogram2);
            //score2.apply("whizzing").forEach( System.out::println);



        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> strings = new ArrayList<>();

       Stream.iterate("+", s->s+"+")
               .parallel()
               .limit(1000)
               //.peek(s->System.out.println(s + " processed in thread " + Thread.currentThread().getName()))
               //.forEach(System.out::println);
               .forEach(strings::add);

       System.out.println("# "+ strings.size());


        Optionals();
        System.out.println("-----------------------");
        try {
            MoviesActors();
        } catch (IOException e) {
            e.printStackTrace();
        }


        test();

        System.out.println("-----------------------");

        String aaa =
                "you!)";

        System.out.println(aaa.replaceAll("[^a-zA-Z0-9]", ""));

        try {
            Words();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Words() throws IOException {
        Stream<String> streamTextFile = Files.lines(Paths.get("files/MiscellaneousWritings.txt"));

        Pattern pattern = Pattern.compile("\\s+");


        final Set<Character> charsElems =  new HashSet<Character>(Arrays.asList(new Character [] {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',  'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',  'z'
        }));

        final Map<Character, List<Map.Entry<String, Long>>> data = streamTextFile
                .flatMap(line -> pattern.splitAsStream(line))
                .map(word->word.replaceAll("[^a-zA-Z0-9]", ""))
                .map(String::toLowerCase)
                .filter(word->word.length() > 0 && charsElems.contains(word.charAt(0)))
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(word -> word.getKey().charAt(0)));

        data.forEach((letter, worldList) ->
                {
                    System.out.printf("%n%C%n", letter);
                    worldList.stream().forEach(
                           word->System.out.printf("%13s :%d%n",word.getKey(), word.getValue())
                    );
                }
                );

    }
    
    public static void dice() {
        SecureRandom random = new SecureRandom();

        random.ints(6_000_000, 1, 7)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))

    }



    public static void Optionals() {

        List<Double> result = new ArrayList<>();

       ThreadLocalRandom.current()
                .doubles(10_000)
                .boxed()
                .forEach(

                        (d)->{
                           Optional<Double> res = d != 0 ? Optional.of(1d/d) : Optional.empty();
                            res.ifPresent(result::add);
                        }
                );

        System.out.println("# optionals:  "+ result.size());

        Function<Double, Stream<Double>> flatMapper = (d) ->
        {
            Double res = 0d;

           if(d != 0) {
               res = 1d / d;
               if(res > 0)
                   res = sqrt(res);
           }

           return Stream.of(res);
        };
    }

    public static void test() {
        String one = "one";
        String two = "two";

        List<String> list = new ArrayList<>();


        list.add(one);
        list.add(two);

        System.out.println("# one EQ: " + (one == list.get(0)));
        System.out.println("# one EQ: " + (one.equals(list.get(0))));

        System.out.println("# one contains: " + list.contains(new String("one")));
        one = one.replace("o","b");

        System.out.println("# one EQ: " + (one == list.get(0)));
        System.out.println("# one EQ: " + (one.equals(list.get(0))));


        list.clear();
        System.out.println(list);                                // line 10

        StringBuilder ejg = new StringBuilder(10 + 2 + "SUN" + 4 + 5);
        ejg.append(ejg.delete(3, 6));
        System.out.println(ejg);


    }


    public static void MoviesActors() throws IOException {

        Set<Movie> movies = new HashSet<>();

        Stream<String> lines = Files.lines(Paths.get("files", "movies-mpaa.txt"));

        lines.forEach(
                (String line) -> {
                    String [] elements = line.split("/");
                    String title = elements[0].substring(0, elements[0].lastIndexOf('(')).trim();
                    String releaseYear = elements[0].substring(elements[0].lastIndexOf('(')+1, elements[0].lastIndexOf(')')).trim();

                    if(releaseYear.contains(",")) {
                        return;
                    }

                    Movie movie = new Movie(title, Integer.valueOf(releaseYear));
                    for(String actorLine : elements) {
                        String[] name = actorLine.split(", ");
                        String lastName = name[0].trim();
                        String firstName = "";
                        if(name.length > 1){
                            firstName = name[1].trim();
                        }

                        movie.addActor(new Actor(lastName, firstName));
                    }
                    movies.add(movie);

                }
                );

        System.out.println("# movies: " + movies.size());

        Integer actors = movies.stream().map(movie->movie.getActors().size()).reduce(Integer::sum).orElse(0);

        Long distinctActors = movies.stream().flatMap(movie->movie.getActors().stream()).distinct().count();

        final Map<Actor, Long> collect = movies.stream()
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.groupingBy(
                        //actor -> actor,
                        Function.identity(),
                        Collectors.counting()));

        final Map.Entry<Actor, Long> actorLongEntry = collect.entrySet()
                .stream()
                .max(
                        //Comparator.comparing/entry -> entry.getValue())
                        Map.Entry.comparingByValue()
                    ).orElse(null);

        System.out.println("# actors: " + actors);
        System.out.println("# distinctActors: " + distinctActors);

        if(actorLongEntry != null){
            System.out.println("# actorLongEntry: " + actorLongEntry.getKey() + " movies: " + actorLongEntry.getValue());
        }

        Map.Entry<Integer, Map.Entry<Actor, AtomicLong>> result = movies.stream().collect(Collectors.groupingBy(
                movie -> movie.getReleaseYear(),
                Collector.of(
                        () -> new HashMap<Actor, AtomicLong>(),//supplier
                        (map, movie) -> {
                            movie.getActors().forEach(
                                    actor -> map.computeIfAbsent(actor, a -> new AtomicLong()).incrementAndGet()
                            );
                        }, //accumulator
                        (accumulator, map) -> {
                            map.entrySet().forEach(
                                    entry -> accumulator.merge(
                                            entry.getKey(),
                                            entry.getValue(),
                                            (al1, al2) -> {
                                                al1.addAndGet(al2.get());
                                                return al1;
                                            }
                                    )
                            );
                            return accumulator;
                        },//combiner
                        Collector.Characteristics.IDENTITY_FINISH
                )
        )
      )//Map<Integer, HashMap<Actor, AtomicLong>>
       .entrySet().stream().collect(Collectors.toMap(
               entry->entry.getKey(),
              entry->entry.getValue().
                      entrySet().
                      stream().
                      max( Map.Entry.comparingByValue(Comparator.comparing(atomicL -> atomicL.get()))).
                      get()

      ))//Map<Integer, Map.Entry<Actor, AtomicLong>>
        .entrySet()
                .stream()
                .max(
                Map.Entry.comparingByValue(
                        Comparator.comparing(
                                entry->entry.getValue().get()
                        )
                )).get();

        System.out.println("# result: " + result);

    }





}
