package fpcc.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Message {
	private byte opCode;
	private int value;

	// Constantes indicadoras de estados e operacoes
	// NIL indica que mensagem eh vazia
	public static final byte NIL = 0;
	// Codigos de operacao DEC decrementa
	// INC incrementa
	public static final byte DEC = 1;
	public static final byte INC = 2;
	// Codigos de estado para utilizacao
	// em lado servidor
	public static final byte OK = 3;
	public static final byte ERR = 4;

	// Constante para representar o tamanho do pacote
	public static final int DATA_LENGTH = Integer.BYTES + 1;

	public Message() {
		opCode = NIL;
		value = NIL;
	}

	public Message(byte[] data) {
		setFromBytes(data);

	}

	public Message(int op, int value) {
		this.opCode = (byte) op;
		this.value = value;
	}

	public int getOpCode() {
		return opCode;
	}

	public void setOpCode(int op) {
		this.opCode = (byte) op;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setFromBytes(byte[] data) {
		if (data.length != 5) {
			throw new RuntimeException("Invalid data!");
		}
		opCode = data[0];
		byte[] valueBytesRepr = Arrays.copyOfRange(data, 1, DATA_LENGTH);
		value = ByteBuffer.wrap(valueBytesRepr).getInt();
	}

	public byte[] getBytes() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(opCode);
		byte[] valueInBytes = ByteBuffer.allocate(4).putInt(value).array();
		try {
			outputStream.write(valueInBytes);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}

		return outputStream.toByteArray();

	}

	public boolean isEmpty() {
		return opCode == NIL;
	}

}
