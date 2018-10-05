package com.blackjack.io;

import com.blackjack.game.Action;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public final class InputOutput implements InputInterface {
    private interface inputValidator {
        boolean isValid(int number);
    }

    private BufferedReader reader;
    private StringBuffer buffer;

    public InputOutput() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        buffer = new StringBuffer();
    }

    private String readInput( String decision ) {
        System.out.print(decision);
        String str = "";
        try {
            str = reader.readLine();
        }
        catch (IOException ex) {
            System.out.println("IO Error");
        }

        return str.trim();
    }

    public void showText(String format, Object ... args)
    {
        System.out.println(String.format(format, args));
    }

    public int firstBet(final int bet) {
        return getBet(bet, String.format("You have $%d. Enter your betting amount: $" , bet));
    }

    private int getBet(final int bet, String decision) {
        return getInteger(decision, new inputValidator() {
            @Override
            public boolean isValid(int number) {
                return number > 0 && number <= bet;
            }
        },
        String.format("You don't have that much money!"));
    }

    public Action readUserInput(final List<Action> options) {
        final HashMap<Integer,Action> optionMap = new HashMap<Integer, Action>(options.size());
        String prompt = getPromptForOptions(options, optionMap);

        int action = getInteger(prompt, new inputValidator(){
            @Override
            public boolean isValid(int number) {
                return optionMap.containsKey(number);
            }
        },
                "Not an action. Use the numbers corresponding to the actions \n");
        return optionMap.get(action);
    }

    private String getPromptForOptions( List<Action> options, HashMap<Integer,Action> validOptions ) {
        int count = options.size();
        buffer.append("Actions: ");

        for (int i = 0; i < count; i++) {
            validOptions.put(i + 1, options.get(i));
            if (i == count - 1){
                buffer.append(String.format("%s(%d) \n", options.get(i), i + 1));
            } else {
                buffer.append(String.format("%s(%d) ", options.get(i), i + 1));
            }

        }
        buffer.append(">>> ");

        String str = buffer.toString();
        buffer.delete(0, buffer.length());
        return str;
    }

    private int getInteger(String decision, inputValidator validator, String err) {
        while (true) {
            boolean isValid = false;
            String str = readInput(decision);
            int value = 1;
            try {
                value = Integer.parseInt(str);
                isValid = validator.isValid(value);
            }
            catch ( NumberFormatException ex ) {}

            if (! isValid) {
                showText(err);
            }
            else {
                return value;
            }
        }
    }

}
