package cn.haday.device.hksdk.exception;

public class InstructionExecuteException extends Exception {
    private int exceptionCode;

    public InstructionExecuteException(String msg) {
        super(msg);
    }

    public InstructionExecuteException(String msg, int exceptionCode) {
        super(msg);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }
}
