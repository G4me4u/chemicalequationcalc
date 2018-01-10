package com.g4mesoft.word;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;

@SuppressWarnings("deprecation")
public class WordTransferable implements Transferable {

	private static DataFlavor[] flavors;
	
	static {
		DataFlavor[] flavors = null;
		try {
			flavors = new DataFlavor[] {
				new DataFlavor("text/plain;class=java.lang.String"),
				new DataFlavor("text/plain;class=java.io.Reader"),
				new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream"),
					
				new DataFlavor("text/html;class=java.lang.String"),
				new DataFlavor("text/html;class=java.io.Reader"),
				new DataFlavor("text/html;charset=unicode;class=java.io.InputStream")
			};
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		WordTransferable.flavors = flavors;
	}

	private final String plain;
	private final String html;

	public WordTransferable(String plain, String html) {
		this.plain = plain;
		this.html = html;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor flav : flavors)
			if (flav.equals(flavor))
				return true;
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		String text;
		switch (flavor.getSubType()) {
		case "html":
			text = html;
			break;
		default:
			text = plain;
			break;
		}
		
		if (String.class.equals(flavor.getRepresentationClass())) {
			return text;
		} else if (Reader.class.equals(flavor.getRepresentationClass())) {
			return new StringReader(text);
		} else if (InputStream.class.equals(flavor.getRepresentationClass())) {
			return new StringBufferInputStream(text);
		}
		throw new UnsupportedFlavorException(flavor);
	}
}