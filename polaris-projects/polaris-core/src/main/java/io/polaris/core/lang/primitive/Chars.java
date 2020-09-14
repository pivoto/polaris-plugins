package io.polaris.core.lang.primitive;

import io.polaris.core.consts.CharConsts;

/**
 * @author Qt
 * @since 1.8
 */
public class Chars {

	/**
	 * 是否为ASCII字符，ASCII字符位于0~127之间
	 *
	 * <pre>
	 *   Chars.isAscii('a')  = true
	 *   Chars.isAscii('A')  = true
	 *   Chars.isAscii('3')  = true
	 *   Chars.isAscii('-')  = true
	 *   Chars.isAscii('\n') = true
	 *   Chars.isAscii('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII字符，ASCII字符位于0~127之间
	 */
	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	/**
	 * 是否为可见ASCII字符，可见字符位于32~126之间
	 *
	 * <pre>
	 *   Chars.isAsciiPrintable('a')  = true
	 *   Chars.isAsciiPrintable('A')  = true
	 *   Chars.isAsciiPrintable('3')  = true
	 *   Chars.isAsciiPrintable('-')  = true
	 *   Chars.isAsciiPrintable('\n') = false
	 *   Chars.isAsciiPrintable('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII可见字符，可见字符位于32~126之间
	 */
	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
	 *
	 * <pre>
	 *   Chars.isAsciiControl('a')  = false
	 *   Chars.isAsciiControl('A')  = false
	 *   Chars.isAsciiControl('3')  = false
	 *   Chars.isAsciiControl('-')  = false
	 *   Chars.isAsciiControl('\n') = true
	 *   Chars.isAsciiControl('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为控制符，控制符位于0~31和127
	 */
	public static boolean isAsciiControl(final char ch) {
		return ch < 32 || ch == 127;
	}

	/**
	 * 判断是否为字母（包括大写字母和小写字母）<br>
	 * 字母包括A~Z和a~z
	 *
	 * <pre>
	 *   Chars.isLetter('a')  = true
	 *   Chars.isLetter('A')  = true
	 *   Chars.isLetter('3')  = false
	 *   Chars.isLetter('-')  = false
	 *   Chars.isLetter('\n') = false
	 *   Chars.isLetter('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
	 */
	public static boolean isLetter(char ch) {
		return isLetterUpper(ch) || isLetterLower(ch);
	}

	/**
	 * <p>
	 * 判断是否为大写字母，大写字母包括A~Z
	 * </p>
	 *
	 * <pre>
	 *   Chars.isLetterUpper('a')  = false
	 *   Chars.isLetterUpper('A')  = true
	 *   Chars.isLetterUpper('3')  = false
	 *   Chars.isLetterUpper('-')  = false
	 *   Chars.isLetterUpper('\n') = false
	 *   Chars.isLetterUpper('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为大写字母，大写字母包括A~Z
	 */
	public static boolean isLetterUpper(final char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * <p>
	 * 检查字符是否为小写字母，小写字母指a~z
	 * </p>
	 *
	 * <pre>
	 *   Chars.isLetterLower('a')  = true
	 *   Chars.isLetterLower('A')  = false
	 *   Chars.isLetterLower('3')  = false
	 *   Chars.isLetterLower('-')  = false
	 *   Chars.isLetterLower('\n') = false
	 *   Chars.isLetterLower('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为小写字母，小写字母指a~z
	 */
	public static boolean isLetterLower(final char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>
	 * 检查是否为数字字符，数字字符指0~9
	 * </p>
	 *
	 * <pre>
	 *   Chars.isNumber('a')  = false
	 *   Chars.isNumber('A')  = false
	 *   Chars.isNumber('3')  = true
	 *   Chars.isNumber('-')  = false
	 *   Chars.isNumber('\n') = false
	 *   Chars.isNumber('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为数字字符，数字字符指0~9
	 */
	public static boolean isNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * 是否为16进制规范的字符，判断是否为如下字符
	 * <pre>
	 * 1. 0~9
	 * 2. a~f
	 * 4. A~F
	 * </pre>
	 *
	 * @param c 字符
	 * @return 是否为16进制规范的字符
	 */
	public static boolean isHexChar(char c) {
		return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}

	/**
	 * 是否为字母或数字，包括A~Z、a~z、0~9
	 *
	 * <pre>
	 *   Chars.isLetterOrNumber('a')  = true
	 *   Chars.isLetterOrNumber('A')  = true
	 *   Chars.isLetterOrNumber('3')  = true
	 *   Chars.isLetterOrNumber('-')  = false
	 *   Chars.isLetterOrNumber('\n') = false
	 *   Chars.isLetterOrNumber('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为字母或数字，包括A~Z、a~z、0~9
	 */
	public static boolean isLetterOrNumber(final char ch) {
		return isLetter(ch) || isNumber(ch);
	}

	/**
	 * 字符转为字符串<br>
	 * 如果为ASCII字符，使用缓存
	 *
	 * @param c 字符
	 * @return 字符串
	 * @see ASCIIStrCache#toString(char)
	 */
	public static String toString(char c) {
		return ASCIIStrCache.toString(c);
	}

	/**
	 * 给定类名是否为字符类，字符类包括：
	 *
	 * <pre>
	 * Character.class
	 * char.class
	 * </pre>
	 *
	 * @param clazz 被检查的类
	 * @return true表示为字符类
	 */
	public static boolean isCharClass(Class<?> clazz) {
		return clazz == Character.class || clazz == char.class;
	}

	/**
	 * 给定对象对应的类是否为字符类，字符类包括：
	 *
	 * <pre>
	 * Character.class
	 * char.class
	 * </pre>
	 *
	 * @param value 被检查的对象
	 * @return true表示为字符类
	 */
	public static boolean isChar(Object value) {
		//noinspection ConstantConditions
		return value instanceof Character || value.getClass() == char.class;
	}

	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 *
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 */
	public static boolean isBlankChar(char c) {
		return isBlankChar((int) c);
	}

	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 *
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 */
	public static boolean isBlankChar(int c) {
		return Character.isWhitespace(c)
			|| Character.isSpaceChar(c)
			|| c == '\ufeff'
			|| c == '\u202a'
			|| c == '\u0000'
			// issue#I5UGSQ，Hangul Filler
			|| c == '\u3164'
			// Braille Pattern Blank
			|| c == '\u2800'
			// MONGOLIAN VOWEL SEPARATOR
			|| c == '\u180e';
	}

	/**
	 * 判断是否为emoji表情符<br>
	 *
	 * @param c 字符
	 * @return 是否为emoji
	 */
	public static boolean isEmoji(char c) {
		//noinspection ConstantConditions
		return false == ((c == 0x0) || //
			(c == 0x9) || //
			(c == 0xA) || //
			(c == 0xD) || //
			((c >= 0x20) && (c <= 0xD7FF)) || //
			((c >= 0xE000) && (c <= 0xFFFD)) || //
			((c >= 0x100000) && (c <= 0x10FFFF)));
	}

	/**
	 * 是否为Windows或者Linux（Unix）文件分隔符<br>
	 * Windows平台下分隔符为\，Linux（Unix）为/
	 *
	 * @param c 字符
	 * @return 是否为Windows或者Linux（Unix）文件分隔符
	 */
	public static boolean isFileSeparator(char c) {
		return CharConsts.SLASH == c || CharConsts.BACKSLASH == c;
	}

	/**
	 * 比较两个字符是否相同
	 *
	 * @param c1              字符1
	 * @param c2              字符2
	 * @param caseInsensitive 是否忽略大小写
	 * @return 是否相同
	 */
	public static boolean equals(char c1, char c2, boolean caseInsensitive) {
		if (caseInsensitive) {
			return Character.toLowerCase(c1) == Character.toLowerCase(c2);
		}
		return c1 == c2;
	}

	/**
	 * 获取字符类型
	 *
	 * @param c 字符
	 * @return 字符类型
	 */
	public static int getType(int c) {
		return Character.getType(c);
	}

	/**
	 * 获取给定字符的16进制数值
	 *
	 * @param b 字符
	 * @return 16进制字符
	 */
	public static int digit16(int b) {
		return Character.digit(b, 16);
	}

	/**
	 * 将字母、数字转换为带圈的字符：
	 * <pre>
	 *     '1' -》 '①'
	 *     'A' -》 'Ⓐ'
	 *     'a' -》 'ⓐ'
	 * </pre>
	 * <p>
	 * 获取带圈数字 /封闭式字母数字 ，从1-20,超过1-20报错
	 *
	 * @param c 被转换的字符，如果字符不支持转换，返回原字符
	 * @return 转换后的字符
	 * @see <a href="https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols">Unicode_symbols</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Enclosed_Alphanumerics">Alphanumerics</a>
	 */
	public static char toCloseChar(char c) {
		int result = c;
		if (c >= '1' && c <= '9') {
			result = '①' + c - '1';
		} else if (c >= 'A' && c <= 'Z') {
			result = 'Ⓐ' + c - 'A';
		} else if (c >= 'a' && c <= 'z') {
			result = 'ⓐ' + c - 'a';
		}
		return (char) result;
	}

	/**
	 * 将[1-20]数字转换为带圈的字符：
	 * <pre>
	 *     1 -》 '①'
	 *     12 -》 '⑫'
	 *     20 -》 '⑳'
	 * </pre>
	 * 也称作：封闭式字符，英文：Enclosed Alphanumerics
	 *
	 * @param number 被转换的数字
	 * @return 转换后的字符
	 * @author dazer
	 * @see <a href="https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols">维基百科wikipedia-Unicode_symbols</a>
	 * @see <a href="https://zh.wikipedia.org/wiki/Unicode%E5%AD%97%E7%AC%A6%E5%88%97%E8%A1%A8">维基百科wikipedia-Unicode字符列表</a>
	 * @see <a href="https://coolsymbol.com/">coolsymbol</a>
	 * @see <a href="https://baike.baidu.com/item/%E7%89%B9%E6%AE%8A%E5%AD%97%E7%AC%A6/112715?fr=aladdin">百度百科 特殊字符</a>
	 */
	public static char toCloseByNumber(int number) {
		if (number > 20) {
			throw new IllegalArgumentException("Number must be [1-20]");
		}
		return (char) ('①' + number - 1);
	}

	static class ASCIIStrCache {

		private static final int ASCII_LENGTH = 128;
		private static final String[] CACHE = new String[ASCII_LENGTH];

		static {
			for (char c = 0; c < ASCII_LENGTH; c++) {
				CACHE[c] = String.valueOf(c);
			}
		}

		/**
		 * 字符转为字符串<br>
		 * 如果为ASCII字符，使用缓存
		 *
		 * @param c 字符
		 * @return 字符串
		 */
		public static String toString(char c) {
			return c < ASCII_LENGTH ? CACHE[c] : String.valueOf(c);
		}
	}
}
