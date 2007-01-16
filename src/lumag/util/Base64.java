package lumag.util;

import java.io.ByteArrayOutputStream;

public class Base64 {
	public static byte[] decode(String string) {
		int len = string.length();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream(len * 3/4);

		long currentValue = 0;
		int curChars = 0;
		int skipChars = 0;
		for (int i = 0; i < len; i++) {
			char ch = string.charAt(i);
			
			int nextBits;
			if (Character.isWhitespace(ch)) {
				continue;
			} else if (ch == '=') {
				nextBits = 0;
				skipChars ++;
			} else {
				nextBits = parseCharacter(ch);
			}

			currentValue = (currentValue << 6) + nextBits;
			curChars ++;

			if (curChars == 4) {
				output.write((int) ((currentValue >> 16) & 0xff));
				if (skipChars < 2) {
					output.write((int) ((currentValue >> 8) & 0xff));
				}
				if (skipChars < 2) {
					output.write((int) ((currentValue >> 0) & 0xff));
				}
				
				curChars = 0;
				skipChars = 0;
				currentValue = 0;
			}
		}
		return output.toByteArray();
	}

	private static int parseCharacter(char ch) {
		if ('A' <= ch && ch <= 'Z') {
			return ch - 'A';
		} else if ('a' <= ch && ch <= 'z') {
			return ch - 'a' + 26;
		} else if ('0' <= ch && ch <= '9') {
			return ch - '0' + 26 + 26;
		} else if (ch == '+') {
			return ch - '+' + 26 + 26 + 10;
		} else if (ch == '/') {
			return ch - '/' + 26 + 26 + 10 + 1;
		} else {
			throw new IllegalArgumentException(
					"Bad character encountered: '" + ch +
					"' (0x" + Integer.toHexString(ch));
		}
	}

}
