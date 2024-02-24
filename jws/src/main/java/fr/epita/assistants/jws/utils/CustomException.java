package fr.epita.assistants.jws.utils;

public class CustomException extends Exception {
    private int returnCode;

    public CustomException(String message, int returnCode)
    {
        super(message);
        this.returnCode = returnCode;
    }

    public int getReturnCode(){
        return returnCode;
    }
}
