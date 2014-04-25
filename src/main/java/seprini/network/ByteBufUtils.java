package seprini.network;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

/**
 * A bytebuf utility class for string reading/writing.
 * @author Jonathan
 *
 */
public final class ByteBufUtils {
	public static void writeString(ByteBuf buf, String string) {
		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}

	public static String readString(ByteBuf buf) {
		int length = buf.readShort();
		byte[] data = new byte[length];
		buf.readBytes(data);
		return new String(data, StandardCharsets.UTF_8);
	}
}
