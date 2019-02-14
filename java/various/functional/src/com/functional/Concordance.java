package com.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Concordance {

    private static final Pattern  WORD_BREAK = Pattern.compile("\\W+");
    private static final Comparator<Map.Entry<String, Long>> valueOrder = Map.Entry.comparingByValue();

    private static final Comparator<Map.Entry<String, Long>> valueOrderReversed = valueOrder.reversed();

    public static void split(String fileName) throws IOException {

        Files.lines(Paths.get(fileName))
                .flatMap(l -> WORD_BREAK.splitAsStream(l))
                .filter(w -> w.length() > 0)
                .map(w -> w.toLowerCase())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(valueOrderReversed)
                .limit(200)
                .forEach(l -> System.out.printf("%20s : %5d\n", l.getKey(), l.getValue() ));

    }

    public static void split2(String fileName) throws IOException {

        Files.lines(Paths.get(fileName))
                .flatMap(WORD_BREAK::splitAsStream)
                .filter(w->w.length() > 0)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(valueOrderReversed)
                .limit(200)
                .map(l -> String.format("%20s : %5d\n", l.getKey(), l.getValue() ))
                .forEach(System.out::println);

    }

    public static <E,F> Function<E, Optional<F>> wrap(ExceptionFunction<E,F> op){
        return e -> {
            try {
                return Optional.of(op.applay(e));
            } catch (Throwable t) {
                return Optional.empty();
            }
        };
    }


    public static Optional<Stream<String>> lines(Path path){
        try{
            return Optional.of(Files.lines(path));
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    public static void splitMultipleFiles(List<String> fileNames) throws IOException {

        fileNames.stream()
                 .map(Paths::get)
                 .map(wrap(p-> Files.lines(p)))
                 .peek(o-> {
                    if (!o.isPresent()){
                        System.err.println("File read failed!");
                    }
                })
                .filter(o->o.isPresent())
                .flatMap(Optional::get)
                .flatMap(WORD_BREAK::splitAsStream)
                .filter(w->w.length() > 0)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(valueOrderReversed)
                .limit(200)
                .map(l -> String.format("%20s : %5d", l.getKey(), l.getValue() ))
                .forEach(System.out::println);
    }

    public static void splitMultipleFiles2(List<String> fileNames) throws IOException {
        fileNames.stream()
                .map(Paths::get)
                .map(Either.wrap(p -> Files.lines(p)))
                .peek(e -> e.handle(System.err::println))
                .filter(e->e.success())
                .flatMap(Either::get)
                .flatMap(WORD_BREAK::splitAsStream)
                .filter(w->w.length() > 0)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(valueOrderReversed)
                .limit(200)
                .map(l -> String.format("%20s : %5d", l.getKey(), l.getValue() ))
                .forEach(System.out::println);
    }

}
