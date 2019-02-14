package com.network;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collector;


public class CommandHandler {

    private enum transferType {
        ASCII, BINARY
    }

    private String currDirectory;
    final private PrintWriter printWriter;
    private transferType transferMode;
    final private String root;
    private ServerSocket dataSocket;


    public CommandHandler(PrintWriter printWriter) {
        this.root = System.getProperty("user.dir").replace("\\","/");
        this.printWriter = printWriter;
        this.currDirectory = root + "/test";

        try {
            dataSocket = socketCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(String command) {

        final String[] commandSplit = command.split(" ");
        final String cmd = commandSplit[0].trim().toLowerCase();
        final String args = commandSplit.length > 1 ? commandSplit[1].trim() : null;


        System.out.println("Command: " + cmd + " Args: " + args);

        try {
            Method commandhandler = CommandHandler.class.getDeclaredMethod("handle_" + cmd, String.class);
            commandhandler.invoke(this, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private  void handle_user(String args){
        if (args.equals("anonymous")) {
            printWriter.println("331 User name OK, give me an e-mail.");
        } else {
            printWriter.println("530 Only anonymous exists.");
        }
    }

    private  void handle_syst(String args)
    {
        printWriter.println("215 UNIX Type: L8");
    }

    private  void handle_pass(String args){
        printWriter.println("230 User logged in, proceed.");
    }

    private  void handle_pwd(String args) {
        String reply = String.format("257 \"%s\"", currDirectory);
        printWriter.println(reply);
    }

    private void handle_type(String mode)
    {
        if(mode.toUpperCase().equals("A") || mode.toUpperCase().equals("I")) {
            transferMode = mode.toUpperCase().equals("A") ? transferType.ASCII : transferType.BINARY;
            printWriter.println("200 OK");
        } else {
            printWriter.println("504 Not OK");
        }
    }

    private void handle_size(String args) {
        File temp = new File(currDirectory);
        final long size;
        if(temp.isDirectory()) {
            printWriter.println("550 File not found.");
            return;
        } else {
            size = FileUtils.sizeOf(temp);
        }
        System.out.println("size: "+size);
        printWriter.println("213 " + size);
    }

    private void handle_cwd(String args){
        String filename = currDirectory;
        final String fileSeparator = "/";

        if (args.equals("..")){
            int ind = filename.lastIndexOf(fileSeparator);
            if (ind > 0){
                filename = filename.substring(0, ind);
            }
        } else if ((args != null) && (!args.equals("."))) {
            filename = args;
        }


        File f = new File(filename);

        if (f.exists() && f.isDirectory() && (filename.length() >= root.length())) {
            currDirectory = filename;
            printWriter.println("250 The current directory has been changed to " + currDirectory);
        } else {
            printWriter.println("550 Requested action not taken. File unavailable.");
        }
    }

    private void handle_list(String args){
        printWriter.println("150 Opening IMAGE mode data connection for LIST.");

        File file = new File(currDirectory);
        if(!file.isDirectory()) {
            return;
        }

        Function<File, StringBuilder> parser =  (fileToCheck) ->{
            if (!fileToCheck.isDirectory() && !fileToCheck.isFile()) {
                return null;
            }

            final SimpleDateFormat fmtDate = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);
            final SimpleDateFormat fmtPast = new SimpleDateFormat("MMM dd  yyyy", Locale.ENGLISH);
            final Calendar cal = Calendar.getInstance();
            final int currentYear = cal.get(Calendar.YEAR);

            StringBuilder sb = new StringBuilder();
            cal.setTimeInMillis(fileToCheck.lastModified());

            sb.append(fileToCheck.isDirectory() ? "drwxr-xr-x" : "-rw-r--r--")
                    .append(' ')
                    .append(String.format("%3d", 1))
                    .append(' ')
                    .append(String.format("%8d", fileToCheck.isDirectory() ? 4096 : fileToCheck.length()))
                    .append(' ')
                    .append(cal.get(Calendar.YEAR) == currentYear ? fmtDate.format(cal.getTime()) : fmtPast.format(cal.getTime()))
                    .append(' ')
                    .append(fileToCheck.getName());

            return sb;
        };

        final String filesList = Arrays.stream(file.listFiles())
                .map(parser)
                .filter(elem -> elem != null)
                .collect(Collector.of(StringBuilder::new,
                        (stringBuilder, str) -> stringBuilder.append(str).append("\r\n"),
                        StringBuilder::append,
                        StringBuilder::toString));

        acceptConnection(filesList);

        printWriter.println("226 Transfer complete");
    }

    private void handle_retr(String args){

        final String[] argSplit = args.split("/");
        File tmp = new File(argSplit[argSplit.length-1]);
        if(tmp.isDirectory()){
            printWriter.println("550 File not found.");
            return;
        }

        printWriter.println("150 ening IMAGE mode data connection for RETR.");
        acceptConnection(tmp);

        printWriter.println("226 Transfer complete");
    }

   private void handle_pasv(String args){
        final int [] ip = new int[]{127,0,0,1};
        final String reply = String.format("227 Entering Passive Mode (%d,%d,%d,%d,%d,%d).", ip[0], ip[1], ip[2], ip[3],
                (int)(dataSocket.getLocalPort() / 256),
                (int)(dataSocket.getLocalPort() % 256));
        printWriter.println(reply);
    }

    private void handle_quit(String args) {
        printWriter.println("221 Bye.");
    }

    private ServerSocket socketCreate( ) throws IOException {
        ServerSocket socket = new ServerSocket(0,1);
        socket.setReuseAddress(true);
        socket.setSoTimeout(60000);

        return socket;
    }

    private <T> void acceptConnection(T content){
        try {
            final Socket accept = dataSocket.accept();
            final PrintWriter pw = new PrintWriter(accept.getOutputStream(), true);
            pw.println(content);
            accept.shutdownOutput();
            accept.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}