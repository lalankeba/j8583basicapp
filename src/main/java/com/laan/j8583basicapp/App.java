package com.laan.j8583basicapp;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

public class App {

    private static final String HEADER = "ISO1987";

    public static void main(String[] args) throws Exception {
        // create message factory with default settings
        MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();

        // creating iso message with MTI as financial
        IsoMessage isoMessage = messageFactory.newMessage(0x200);

        // set header
        isoMessage.setIsoHeader(HEADER);

        // set data fields
        isoMessage.setValue(2, "5642570404782927", IsoType.LLVAR, 19);
        isoMessage.setValue(3, "011000", IsoType.NUMERIC, 6);
        isoMessage.setValue(4, "780.00", IsoType.AMOUNT, 12);
        isoMessage.setValue(7, "1220145711", IsoType.DATE10, 12);
        isoMessage.setValue(11, "101183", IsoType.NUMERIC, 6);
        isoMessage.setValue(12, "145711", IsoType.TIME, 6);
        isoMessage.setValue(13, "1220", IsoType.DATE4, 4);
        isoMessage.setValue(14, "2408", IsoType.DATE_EXP, 4);
        isoMessage.setValue(15, "1220", IsoType.DATE4, 4);
        isoMessage.setValue(18, "6011", IsoType.NUMERIC, 4);
        isoMessage.setValue(22, "051", IsoType.NUMERIC, 3);
        isoMessage.setValue(25, "00", IsoType.NUMERIC, 2);
        isoMessage.setValue(26, "04", IsoType.NUMERIC, 2);
        isoMessage.setValue(28, "C00000000", IsoType.ALPHA, 9);
        isoMessage.setValue(30, "C00000000", IsoType.ALPHA, 9);
        isoMessage.setValue(32, "56445700", IsoType.LLVAR, 11);
        isoMessage.setValue(37, "567134101183", IsoType.ALPHA, 12);
        isoMessage.setValue(41, "N1742", IsoType.ALPHA, 8);
        isoMessage.setValue(42, "ATM004", IsoType.ALPHA, 15);
        isoMessage.setValue(43, "45 SR LEDERSHIP DUABANAT NUEVA ECIJAQ PH", IsoType.ALPHA, 40);
        isoMessage.setValue(49, "608", IsoType.NUMERIC, 3);
        isoMessage.setValue(102, "970630181070041", IsoType.LLVAR, 28);
        isoMessage.setValue(120, "BRN015301213230443463", IsoType.LLLVAR, 999);

        System.out.println("New ISO8583 message:\n" + new String(isoMessage.writeData()));

        readIsoMessage(isoMessage.writeData());

    }

    private static void readIsoMessage(byte[] messageStream) throws Exception {
        MessageFactory<IsoMessage> recMessageFactory = ConfigParser.createFromClasspathConfig("fields.xml");

        // reading ISO8583 message came through TCP/IP connection. So the message is coming as a byte stream
        IsoMessage receivedIsoMessage = recMessageFactory.parseMessage(messageStream, HEADER.length());

        System.out.println("Received ISO8583 message:\n" + new String(receivedIsoMessage.writeData()));

        System.out.println(receivedIsoMessage.getIsoHeader());
        printIsoField(receivedIsoMessage, 2);
        printIsoField(receivedIsoMessage, 14);
        printIsoField(receivedIsoMessage, 120);

    }

    private static void printIsoField(IsoMessage isoMessage, int fieldNumber) {
        IsoValue<Object> isoValue = isoMessage.getField(fieldNumber);
        System.out.println(fieldNumber + " : " + isoValue.getType() + " : " + isoValue.getLength() + " : " + isoValue.getValue());
    }

}
