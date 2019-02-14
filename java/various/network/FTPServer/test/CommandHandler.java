package com.network;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class CommandHandler {

    private enum transferType {
        ASCII, BINARY
    }

    private String currDirectory;
    private PrintWriter printWriter;
    private transferType transferMode = transferType.ASCII;
    private final long dataPort;
    private String root;

    public CommandHandler(PrintWriter printWriter, long dataPort) {
        this.currDirectory = System.getProperty("user.dir")+ "/test";
        this.printWriter = printWriter;
        this.dataPort = dataPort;
        this.root = System.getProperty("user.dir");
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

    private  void handle_syst(String args) {
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
        if(mode.toUpperCase().equals("A")) {
            transferMode = transferType.ASCII;
            printWriter.println("200 OK");
        } else if(mode.toUpperCase().equals("I")) {
            transferMode = transferType.BINARY;
            printWriter.println("200 OK");
        } else {
            printWriter.println("504 Not OK");
        }
    }

    private void handle_size(String args) {
        File temp = new File(currDirectory);
        final long size;
        if(temp.isDirectory()) {
            size = FileUtils.sizeOfDirectory(temp);
        } else {
            size = FileUtils.sizeOf(temp);
        }
        System.out.println("size: "+size);
        printWriter.println("213 " + size);
    }

    private void handle_cwd(String args){
        /*File temp = new File(currDirectory);
        if(!temp.isDirectory()){
            printWriter.println("550 File not found.");
            return;
        }

        printWriter.println("250 OK.");*/

        String filename = currDirectory;
        final String fileSeparator = "/";

        if (args.equals(".."))
        {
            int ind = filename.lastIndexOf(fileSeparator);
            if (ind > 0)
            {
                filename = filename.substring(0, ind);
            }
        }
        else if ((args != null) && (!args.equals(".")))
        {
            filename = filename + fileSeparator + args;
        }

        File f = new File(filename);

        if (f.exists() && f.isDirectory() && (filename.length() >= root.length())) {
            currDirectory = filename;
            printWriter.println("250 The current directory has been changed to " + currDirectory);
        } else {
            printWriter.println("550 Requested action not taken. File unavailable.");
        }
    }

    private void handle_pasv(String args){
        final int [] ip = new int[]{127,0,0,1};
        final String reply = String.format("227 Entering Passive Mode (%d,%d,%d,%d,%d,%d).", ip[0], ip[1], ip[2], ip[3], (int)(dataPort / 256), (int)(dataPort % 256));
        printWriter.println(reply);
    }

    private void handle_quit(String args) {
        printWriter.println("221 Bye.");
    }
}
